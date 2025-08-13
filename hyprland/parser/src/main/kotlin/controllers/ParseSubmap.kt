package controllers

import logger
import model.tables.SubmapModel
import write.submap.WriteSubmap

/**
 * These uses to process and create submap hyprland settings
 *
 * @param submap as list of string
 * @return list of [SubmapModel]
 */
internal fun parseSubmap(submap: List<String>): Result<List<SubmapModel>> {

    logger.info("Try process and create hyprland submap settings.")

    val submapStore = mutableListOf<SubmapModel>()

    submap.forEach {

        if (!it.startsWith("submap")) return@forEach

        if (!it.startsWith("unbind")) return@forEach

        val processSubmap = it.split("#")[0].split("=").getOrNull(1)?.trim() ?: return@forEach

        submapStore.add(SubmapModel(processSubmap))

    }

    val write = WriteSubmap()

    write.writeIntoHyprland(submapStore).getOrThrow()
    write.writeIntoDotConfig(submapStore).getOrThrow()

    return Result.success(submapStore.toList())
}