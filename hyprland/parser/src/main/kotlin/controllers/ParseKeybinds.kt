package controllers

import model.tables.BindModel
import org.slf4j.LoggerFactory
import read.readHelpers.readDispatchers
import write.keybinds.WriteKeybinds

private val logger = LoggerFactory.getLogger("Handel Bind Settings :")

private val modKeysValidations = listOf("shift" ,"caps" ,"ctrl" ,"control" ,"alt" ,"mod2" ,"mod3" ,"super" ,"win" ,"logo" ,"mod4" ,"mod5")

private val dispatchers = readDispatchers()

/**
 * This use to process and handel bind of hyprland. It will create the new hyprland settings file and provide a processed version of settings.
 *
 * @param keyBinds as list of strings
 * @return list of [BindModel]
 */
internal fun parserKeybinds(keyBinds: List<String>): Result<List<BindModel>> {

    logger.info("Start processing valid bind and parsing it.")

    val bindStore = mutableListOf<BindModel>()

    keyBinds.forEach {

        if (!it.startsWith("bind")) {
            return@forEach
        }

        var locked = false
        var release = false
        var repeat = false
        var mouse = false
        var nonConsuming = false
        var transparent = false
        var ignoreMods = false
        var multiKey = false
        var longPress = false
        var hasDescription = false
        var dontInhibit = false
        var click = false
        var drag = false

        val processBind = it.trim().split("#")[0].split("=").map { value -> value.trim() }

        val command = processBind.getOrNull(0) ?: return@forEach

        val value = processBind.getOrNull(1)?.split(",")?.map { bindValue -> bindValue.trim() } ?: return@forEach

        val bindArgs = command.substring(4)

        bindArgs.forEach innerLoop@ { args ->
            when (args) {
                'l' -> locked = true
                'r' -> release = true
                'e' -> repeat = true
                'm' -> mouse = true
                'n' -> nonConsuming = true
                't' -> transparent = true
                'i' -> ignoreMods = true
                's' -> multiKey = true
                'o' -> longPress = true
                'd' -> hasDescription = true
                'p' -> dontInhibit = true
                'c' -> {
                    click = true
                    release = true
                }

                'g' -> {
                    drag = true
                    release = true
                }

                else -> return@forEach
            }
        }

        if ((longPress || release) && repeat) return@forEach

        if (mouse && (repeat || release || locked)) return@forEach

        if (click && drag) return@forEach

        val descOffset = if (hasDescription) 1 else 0

        val keySplit = if (multiKey) "&" else " "

        if (value.size !in 3..6) return@forEach

        val modKeys = value[0].split(keySplit).map { modKey -> modKey.trim() }

        modKeys.forEach innerLoop@ { modKey ->
            if (modKey != "") {
                if (!modKeysValidations.contains(modKey.lowercase())) return@forEach
            }
        }

        val key = value[1].split(keySplit).map { key -> key.trim() }

        val description = if (hasDescription) value[2] else null

        val dispatcher = value[2 + descOffset].validateDispatcher() ?: return@forEach

        val args = value.getOrNull(3 + descOffset)?.trim()

        bindStore.add(BindModel(bindArgs, modKeys, key, description, dispatcher, args))
    }

    logger.info("Write keybinds")

    val writeBinds = WriteKeybinds()

    writeBinds.writeIntoHyprland(hypr = bindStore).getOrThrow()

    writeBinds.writeIntoDotConfig(conf = bindStore).getOrThrow()

    return Result.success(bindStore.toList())
}

/**
 * Try to validate dispatcher is a valid one
 */
private fun String.validateDispatcher(): String? {
    dispatchers.forEach {
        if (it.command == this) {
            return this
        }
    }

    return null
}