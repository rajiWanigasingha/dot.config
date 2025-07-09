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

class EnvController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/env.csv"

    fun createNewEnv(keyword: String ,value: String): Boolean {

        logger.info("Create New Env Variable")

        val allSettings = readAllFromCsv()

        val newCsv = allSettings.append(keyword ,value)

        writeAllToCsv(env = newCsv)

        writeIntoHyprland(data = newCsv)

        return true
    }

    fun updateExistingEnv(keyword: String ,value: String ,oldValue: String): Boolean {

        logger.info("Update Env $keyword = $oldValue")

        val allSettings = readAllFromCsv()

        val update = allSettings.update { "keyword"<String>() }.where { "keyword"<String>() == keyword && "value"<String>() == oldValue }.with { keyword }

        val updateSettings = update.update { "value"<String>() }.where { "keyword"<String>() == keyword && "value"<String>() == oldValue }.with { value }

        writeAllToCsv(env = updateSettings)

        writeIntoHyprland(data = updateSettings)

        return true
    }

    fun deleteEnv(keyword: String ,value: String): Boolean {

        logger.info("Deleting Env $keyword = $value")

        val allSettings = readAllFromCsv()

        val deleted = allSettings.drop { "keyword"<String>() == keyword && "value"<String>() == value }

        writeAllToCsv(env = deleted)

        writeIntoHyprland(data = deleted)

        return true
    }

    private fun readAllFromCsv(): DataFrame<*> {

        logger.info("Read all from env csv")

        val env =
            DataFrame.readCsv(fileOrUrl = path, colTypes = mapOf("keyword" to ColType.String, "command" to ColType.String))

        return env
    }

    private fun writeIntoHyprland(data: DataFrame<*>) {

        logger.info("Write AutoStart Into Hyprland")

        val value = WriteIntoHyprland().writeEnv(env = data)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/env.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }

    private fun writeAllToCsv(env: DataFrame<*>) {
        env.writeCsv(path)
    }
}