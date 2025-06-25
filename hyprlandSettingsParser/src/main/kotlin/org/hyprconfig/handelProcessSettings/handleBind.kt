package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.BindModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Bind Settings :")

private val modKeysValidations = listOf("shift" ,"caps" ,"ctrl" ,"control" ,"alt" ,"mod2" ,"mod3" ,"super" ,"win" ,"logo" ,"mod4" ,"mod5")

private val hyprlandDispatchers = listOf(
    "exec",
    "execr",
    "pass",
    "sendshortcut",
    "sendkeystate",
    "killactive",
    "forcekillactive",
    "closewindow",
    "killwindow",
    "signal",
    "signalwindow",
    "workspace",
    "movetoworkspace",
    "movetoworkspacesilent",
    "togglefloating",
    "setfloating",
    "settiled",
    "fullscreen",
    "fullscreenstate",
    "dpms",
    "pin",
    "movefocus",
    "movewindow",
    "resizewindow",
    "swapwindow",
    "centerwindow",
    "resizeactive",
    "moveactive",
    "resizewindowpixel",
    "movewindowpixel",
    "cyclenext",
    "swapnext",
    "tagwindow",
    "focuswindow",
    "focusmonitor",
    "splitratio",
    "movecursortocorner",
    "movecursor",
    "renameworkspace",
    "exit",
    "forcerendererreload",
    "movecurrentworkspacetomonitor",
    "focusworkspaceoncurrentmonitor",
    "moveworkspacetomonitor",
    "swapactiveworkspaces",
    "bringactivetotop",
    "alterzorder",
    "togglespecialworkspace",
    "focusurgentorlast",
    "togglegroup",
    "changegroupactive",
    "focuscurrentorlast",
    "lockgroups",
    "lockactivegroup",
    "moveintogroup",
    "moveoutofgroup",
    "movewindoworgroup",
    "movegroupwindow",
    "denywindowfromgroup",
    "setignoregrouplock",
    "global",
    "submap",
    "event",
    "setprop",
    "toggleswallow",
    "togglesplit",
    "swapsplit"
)

private val bindPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bind.conf"

/**
 * This use to process and handel bind of hyprland. It will create the new hyprland settings file and provide a processed version of settings.
 *
 * @param keyBinds as list of strings
 * @return list of [BindModel]
 */
internal fun handleBindKeyword(keyBinds: List<String>): MutableList<BindModel> {

    logger.info("Start processing valid bind")

    val bindStore = mutableListOf<BindModel>()

    keyBinds.forEach {

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

    handlePaths(bindPath)

    val validSettingsTransfer = mutableListOf<String>()

    bindStore.forEach {

        val joinString = if (it.flags.contains('s')) "&" else " "

        validSettingsTransfer.add("bind${it.flags} = ${it.mod.joinToString(joinString)} ,${it.key.joinToString(joinString)}${if (it.description != null) " ,${it.description}" else ""} ,${it.dispatcher}${if (it.args != null) " ,${it.args}" else ""}".insertVariables())
    }

    logger.info("Creating bind settings file")

    Path.of(bindPath).writeText(validSettingsTransfer.joinToString("\n"))

    return bindStore
}

/**
 * Try to validate dispatcher is a valid one
 */
private fun String.validateDispatcher(): String? {
    return if (hyprlandDispatchers.contains(this)) this else null
}