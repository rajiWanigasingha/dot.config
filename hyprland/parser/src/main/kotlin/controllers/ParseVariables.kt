package controllers

import logger
import model.tables.VariableModel
import write.variables.WriteVariable
import java.util.regex.Matcher

/**
 *
 * This will separate all variables and swap variables with its value
 *
 * @param allSettings as a list of string variables
 * @return result pare of, list of variables and list of processed hyprland settings as strings
 *
 */
internal fun parseVariables(allSettings: List<String>) : Result<List<String>> {

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

    logger.info("Create variable settings file")

    val writer = WriteVariable()

    writer.writeIntoHyprland(variables.toList())
    writer.writeIntoDotConfig(variables.toList())

    return Result.success(updatedSettings.toList())
}