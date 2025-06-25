package org.hyprconfig.variables


import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.model.VariableModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.regex.Matcher
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Parsing Variables :")

private val variablePath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/variable.conf"

/**
 *
 * This will separate all variables and swap variables with its value
 *
 * @param allSettings as a list of string variables
 * @return pare of, list of variables and list of processed hyprland settings as strings
 *
 */
internal fun parseVariables(allSettings: List<String>) :Pair<List<VariableModel> ,List<String>> {

    logger.info("Begin to parse variables")

    val variables = mutableListOf<VariableModel>()

    val otherSettings = mutableListOf<String>()

    allSettings.forEach {
        if (it.startsWith("$")) {

            val variable = it.split("#")[0].split("=").map { variable -> variable.trim() }

            val name = variable.getOrNull(0)

            val value = variable.getOrNull(1)

            if (name == null || value == null) return@forEach

            variables.add(VariableModel(name, value))
        } else {
            otherSettings.add(it)
        }
    }

    val updatedSettings = otherSettings.map { other ->
        var updated = other
        variables.sortedByDescending { it.name.length }.forEach { variable ->
            val pattern = Regex("(?<!\\w)" + Regex.escape(variable.name) + "(?!\\w)")
            if (pattern.containsMatchIn(updated)) {
                updated = pattern.replace(updated, Matcher.quoteReplacement(variable.value))
            }
        }
        updated
    }

    handlePaths(variablePath)

    logger.info("Create variable settings file")

    val variableSettings = mutableListOf<String>()

    variables.forEach { variableSettings.add("${it.name} = ${it.value}") }

    Path.of(variablePath).writeText(variableSettings.joinToString("\n"))

    return Pair(variables.toList() ,updatedSettings.toList())
}