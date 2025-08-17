package controllers

import logger
import model.tables.BezierModel
import write.bezier.WriteBezier

/**
 * These uses to process and create bezier settings for hyprland
 *
 * @param bezier as list of strings
 * @return as list of [BezierModel]
 */
internal fun parseBezier(bezier: List<String>): Result<List<BezierModel>> {

    logger.info("Try to process and create hyprland bezier settings")

    val bezierStore = mutableListOf<BezierModel>()

    bezier.forEach {

        if (!it.startsWith("bezier")) return@forEach

        val processBezier = it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { bezier -> bezier.trim() } ?: return@forEach

        if (processBezier.size != 5) return@forEach

        val name = processBezier.getOrNull(0)?.validateStringIsNotEmpty() ?: return@forEach

        val arg1 = processBezier.getOrNull(1)?.validateStringIsNotEmpty() ?: return@forEach

        val arg2 = processBezier.getOrNull(2)?.validateStringIsNotEmpty() ?: return@forEach

        val arg3 = processBezier.getOrNull(3)?.validateStringIsNotEmpty() ?: return@forEach

        val arg4 = processBezier.getOrNull(4)?.validateStringIsNotEmpty() ?: return@forEach

        bezierStore.add(BezierModel(name, arg1, arg2, arg3, arg4))
    }

    val write = WriteBezier()

    write.writeIntoHyprland(bezierStore).getOrThrow()
    write.writeIntoDotConfig(bezierStore).getOrThrow()

    return Result.success(bezierStore.toList())
}

private fun String.validateStringIsNotEmpty(): String? {
    return this.ifEmpty { null }
}