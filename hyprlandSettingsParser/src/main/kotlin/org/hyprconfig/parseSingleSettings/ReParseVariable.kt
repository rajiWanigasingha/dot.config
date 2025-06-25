package org.hyprconfig.parseSingleSettings

import org.hyprconfig.model.VariableModel
import org.hyprconfig.variables.parseVariables
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.readText

private val logger = LoggerFactory.getLogger("Re parsing variables:")

fun reParserVariables(): List<VariableModel> {

    logger.info("Parsing all the variables")

    val path = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/variable.conf"

    val allSettings = Path.of(path).readText().split("\n").map { it.trim() }

    val variableSettings = parseVariables(allSettings)

    return variableSettings.first
}