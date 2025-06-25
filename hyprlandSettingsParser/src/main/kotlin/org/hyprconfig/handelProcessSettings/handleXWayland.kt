package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.XwaylandModel
import org.hyprconfig.model.defaultXwaylandSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Xwayland Settings :")

private val XwaylandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/xwayland.conf"

internal fun handleXWayland(xwayland: List<String>): List<XwaylandModel> {

    logger.info("Try to process and create xwayland hyprland settings.")

    val xwaylandStore = defaultXwaylandSettings

    xwayland.forEach {

        val processXWayland = it.split(":=").map { xwayland -> xwayland.trim() }

        val name = processXWayland.getOrNull(0) ?: return@forEach

        val value = processXWayland.getOrNull(1)?.split("=")?.map { xwayland -> xwayland.trim() } ?: return@forEach

        val xwaylandName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val xwaylandValue = value.getOrNull(1) ?: return@forEach

        xwaylandStore.forEach innerLoop@ { xwayland ->
            if (xwayland.name == xwaylandName) {
                val validateXwaylandValue = xwaylandValue.hyprlandTypeCheck(xwayland.type) ?: return@forEach

                if (validateXwaylandValue != xwayland.value) xwayland.value = validateXwaylandValue
            }
        }
    }

    logger.info("Creating xwayland hyprland settings file")

    handlePaths(XwaylandPath)

    val xwaylandSettings = generateXwaylandSettings(xwaylandStore)

    Path.of(XwaylandPath).writeText(xwaylandSettings)

    return xwaylandStore.toList()
}

private fun generateXwaylandSettings(xwayland: List<XwaylandModel>): String {
    return """
        xwayland {
            enabled = ${xwayland.find { it.name == "xwayland:enabled" }!!.value}
            use_nearest_neighbor = ${xwayland.find { it.name == "xwayland:use_nearest_neighbor" }!!.value}
            force_zero_scaling = ${xwayland.find { it.name == "xwayland:force_zero_scaling" }!!.value}
            create_abstract_socket = ${xwayland.find { it.name == "xwayland:create_abstract_socket" }!!.value}
        }
    """.insertVariables().trimIndent()
}