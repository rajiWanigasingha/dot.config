package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.BezierModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Bezier Settings :")

private val bezierPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bezier.conf"

/**
 * These uses to process and create bezier settings for hyprland
 *
 * @param bezier as list of strings
 * @return as list of [BezierModel]
 */
internal fun handleBezier(bezier: List<String>): List<BezierModel> {

    logger.info("Try to process and create hyprland bezier settings")

    val bezierStore = mutableListOf<BezierModel>()

    bezier.forEach {

        val processBezier = it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { bezier -> bezier.trim() } ?: return@forEach

        if (processBezier.size != 5) return@forEach

        val name = processBezier.getOrNull(0)?.validateStringIsNotEmpty() ?: return@forEach

        val arg1 = processBezier.getOrNull(1)?.validateStringIsNotEmpty() ?: return@forEach

        val arg2 = processBezier.getOrNull(2)?.validateStringIsNotEmpty() ?: return@forEach

        val arg3 = processBezier.getOrNull(3)?.validateStringIsNotEmpty() ?: return@forEach

        val arg4 = processBezier.getOrNull(4)?.validateStringIsNotEmpty() ?: return@forEach

        bezierStore.add(BezierModel(name, arg1, arg2, arg3, arg4))
    }

    logger.info("Creating bezier hyprland settings")

    val bezierSettings = mutableListOf<String>()

    handlePaths(bezierPath)

    bezierStore.forEach {
        bezierSettings.add("bezier = ${it.name}, ${it.x0} ,${it.y0} ,${it.x1} ,${it.y1}".insertVariables())
    }

    Path.of(bezierPath).writeText(bezierSettings.joinToString("\n"))

    return bezierStore.toList()
}

private fun String.validateStringIsNotEmpty(): String? {
    return this.ifEmpty { null }
}