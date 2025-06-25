package org.hyprconfig.parseSingleSettings

import org.hyprconfig.handelProcessSettings.handleMonitors
import org.hyprconfig.model.MonitorModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.readText

private val logger = LoggerFactory.getLogger("Re parsing Monitors:")

fun reParsingMonitors(): List<MonitorModel> {
    logger.info("Parsing all the monitors")

    val path = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/monitor.conf"

    val allSettings = Path.of(path).readText().split("\n").map { it.trim() }

    val monitorSettings = handleMonitors(allSettings)

    return monitorSettings
}