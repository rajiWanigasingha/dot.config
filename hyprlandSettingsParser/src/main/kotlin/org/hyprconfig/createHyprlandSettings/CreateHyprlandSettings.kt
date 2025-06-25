package org.hyprconfig.createHyprlandSettings

import org.hyprconfig.helpers.allPaths
import org.hyprconfig.helpers.handlePaths
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("create hyprland :")

private val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprland.conf"

internal fun createHyprlandFile() {

    logger.info("Creating hyprland settings file")

    handlePaths(hyprlandPath)

    Path.of(hyprlandPath).writeText(allPaths)
}