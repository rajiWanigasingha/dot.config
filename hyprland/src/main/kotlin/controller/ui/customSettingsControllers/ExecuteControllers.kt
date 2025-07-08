package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.append
import org.jetbrains.kotlinx.dataframe.api.drop
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class ExecuteControllers {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/autoStart.csv"

    fun createNewExecute(keyword: String ,command: String): Boolean {

        logger.info("Creating New Execute Settings")

        val autoStartSettings = readAllFromCsv()

        val updatedAutoStart = autoStartSettings.append(
            keyword ,command
        )

        writeAllToCsv(autostart = updatedAutoStart)

        writeIntoHyprland(data = updatedAutoStart)

        return true
    }

    fun updateExecute(keyword: String ,command: String ,oldCommand: String): Boolean {

        logger.info("Updating Existing Execute Settings")

        val autoStartSettings = readAllFromCsv()

        val update = autoStartSettings.update { "keyword"<String>() }.where { "command"<String>() == oldCommand }.with { keyword }

        val updateAutoStart = update.update { "command"<String>() }.where { "command"<String>() == oldCommand }.with { command }

        writeAllToCsv(autostart = updateAutoStart)

        writeIntoHyprland(data = updateAutoStart)

        return true
    }

    fun deleteExecute(keyword: String ,command: String): Boolean {

        logger.info("Delete Execute Settings.")

        val autoStartSettings = readAllFromCsv()

        val update = autoStartSettings.drop { row -> row["command"] == command && row["keyword"] == keyword }

        writeAllToCsv(autostart = update)

        writeIntoHyprland(data = update)

        return true
    }

    private fun readAllFromCsv(): DataFrame<*> {

        logger.info("Read all from autostart csv")

        val autostart =
            DataFrame.readCsv(fileOrUrl = path, colTypes = mapOf("keyword" to ColType.String, "command" to ColType.String))

        return autostart
    }

    private fun writeIntoHyprland(data: DataFrame<*>) {

        logger.info("Write AutoStart Into Hyprland")

        val value = WriteIntoHyprland().writeAutoStart(autoStart = data)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/autoStart.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }

    private fun writeAllToCsv(autostart: DataFrame<*>) {
        autostart.writeCsv(path)
    }
}