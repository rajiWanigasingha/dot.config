package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.append
import org.jetbrains.kotlinx.dataframe.api.last
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class VariableController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/variable.csv"

    fun createNewVariable(name: String, value: String): Boolean {

        logger.info("Create New Variable")

        val allVariables = readAllFromCsv()

        val newVariables = allVariables.append(
            name, value
        )

        writeAllToCsv(newVariables)

        writeIntoHyprland(data = newVariables)

        return true
    }

    fun updateExisting(name: String, updateName: String, updateValue: String): Boolean {

        logger.info("Update Existing Variable")

        val allVariables = readAllFromCsv()

        val updated = allVariables.update { "name"<String>() }.where { "name"<String>() == name }
            .with { updateName }

        val updateValue = updated.update { "value"<String>() }.where { "name"<String>() == updateName }.with { updateValue }

        writeAllToCsv(variables = updateValue)

        writeIntoHyprland(data = updateValue)

        return true
    }

    fun deleteIfExist(name: String) {
        logger.info("Delete $name If It Exist")
    }

    private fun readAllFromCsv(): DataFrame<*> {

        logger.info("Read all from variable csv")

        val variable =
            DataFrame.readCsv(fileOrUrl = path, colTypes = mapOf("name" to ColType.String, "value" to ColType.String))

        writeIntoHyprland(data = variable)

        return variable
    }

    private fun writeIntoHyprland(data: DataFrame<*>) {

        logger.info("Write Variables Into Hyprland")

        val value = WriteIntoHyprland().writeVariables(variables = data)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/variable.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }

    private fun writeAllToCsv(variables: DataFrame<*>) {
        variables.writeCsv(path)
    }
}