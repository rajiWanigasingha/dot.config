package controllers

import logger
import model.tables.UnbindModel
import write.unbind.WriteUnbind

private val modKeysValidations =
    listOf("shift", "caps", "ctrl", "control", "alt", "mod2", "mod3", "super", "win", "logo", "mod4", "mod5")

/**
 * These uses to process and create unbind settings form bind settings in
 * hyprland
 *
 * @param unbind as string
 * @return as list of [UnbindModel]
 */
internal fun parseUnbind(unbind: List<String>): Result<List<UnbindModel>> {

    logger.info("Try process and create hyprland unbind settings")

    val unbindStore = mutableListOf<UnbindModel>()

    unbind.forEach {

        if (!it.startsWith("unbind")) return@forEach

        val processUnbind =
            it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { unbind -> unbind.trim() } ?: return@forEach

        val modKey = processUnbind.getOrNull(0)?.split(" ")?.map { mod -> mod.trim() } ?: return@forEach

        val key = processUnbind.getOrNull(1)?.split(" ")?.map { key -> key.trim() } ?: return@forEach

        modKey.forEach innerLoop@{ mod ->
            if (!modKeysValidations.contains(mod)) return@forEach
        }

        unbindStore.add(UnbindModel(modKey, key))
    }

    val write = WriteUnbind()

    write.writeIntoHyprland(unbindStore).getOrThrow()
    write.writeIntoDotConfig(unbindStore).getOrThrow()

    return Result.success(unbindStore.toList())
}