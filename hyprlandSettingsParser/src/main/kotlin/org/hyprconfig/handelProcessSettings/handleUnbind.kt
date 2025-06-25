package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.UnbindModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Unbind Settings :")

private val modKeysValidations =
    listOf("shift", "caps", "ctrl", "control", "alt", "mod2", "mod3", "super", "win", "logo", "mod4", "mod5")

private val unbindPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/unbind.conf"

/**
 * These uses to process and create unbind settings form bind settings in
 * hyprland
 *
 * @param unbind as string
 * @return as list of [UnbindModel]
 */
internal fun handleUnbind(unbind: List<String>): List<UnbindModel> {

    logger.info("Try process and create hyprland unbind settings")

    val unbindStore = mutableListOf<UnbindModel>()

    unbind.forEach {

        val processUnbind =
            it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { unbind -> unbind.trim() } ?: return@forEach

        val modKey = processUnbind.getOrNull(0)?.split(" ")?.map { mod -> mod.trim() } ?: return@forEach

        val key = processUnbind.getOrNull(1)?.split(" ")?.map { key -> key.trim() } ?: return@forEach

        modKey.forEach innerLoop@{ mod ->
            if (!modKeysValidations.contains(mod)) return@forEach
        }

        unbindStore.add(UnbindModel(modKey, key))
    }

    logger.info("Creating unbind hyprland settings file")

    handlePaths(unbindPath)

    val unbinds = mutableListOf<String>()

    unbindStore.forEach {
        unbinds.add("unbind = ${it.mods.joinToString(" ")} ,${it.key.joinToString(" ")}".insertVariables())
    }

    Path.of(unbindPath).writeText(unbinds.joinToString("\n"))

    return unbindStore.toList()
}