package org.dot.config.controller.services

import kotlinx.datetime.Instant
import org.hyprconfig.helpers.HyprlandTypes
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.isEmpty
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import kotlin.io.path.Path
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.writeText

class MouseAndTouchpadService {

    private val at = listOf("inputs.csv", "inputsTouchpad.csv")

    private val user = System.getProperty("user.home")

    private val dataStorePath = "${user}/.dot.config/data"

    private val updatePath = "$dataStorePath/parsedTime.csv"

    private val logger = LoggerFactory.getLogger(javaClass.name)

    fun update(name: String, value: String, type: HyprlandTypes ,category: String): Boolean {

        logger.info("Begin Updating Mouse And Touchpad")

        var update = false

        at.forEach {
            val path = "$dataStorePath/$it"

            val inputs = DataFrame.readCsv(fileOrUrl = path)

            val findRow =
                inputs.filter { "settingsName"<String>() == name && "type"<String>() == type.toString().lowercase() && "category"<String>() == category }

            findRow.print()

            if (!findRow.isEmpty()) update = true

            val update = inputs.update { "value"<String?>() }
                .where { "settingsName"<String>() == name && "type"<String>() == type.toString().lowercase() && "category"<String>() == category }
                .with { _: String? -> value }

            update.writeCsv(path)
        }

        return update
    }

    fun writeIntoHyprland() {

        logger.info("Begin creating update hyprland input settings")

        val inputs = "$dataStorePath/inputs.csv"
        val inputsTouchpad = "$dataStorePath/inputsTouchpad.csv"
        val inputsTable = "$dataStorePath/inputsTablet.csv"
        val inputTouchDevice = "$dataStorePath/inputsTouchDevice.csv"

        val hyprlandPath = Path.of("$user/.config/hypr/hyprConfigAutoGen/inputs.conf")

        val inputsData = mutableListOf<String>()
        val inputsTouchpadData = mutableListOf<String>()
        val inputsTableData = mutableListOf<String>()
        val inputTouchDeviceData = mutableListOf<String>()

        DataFrame.read(inputs).forEach { row -> inputsData.add("${row["settingsName"]} = ${row["value"] ?: ""}") }

        DataFrame.read(inputsTouchpad)
            .forEach { row -> inputsTouchpadData.add("${row["settingsName"]} = ${row["value"] ?: ""}") }

        DataFrame.read(inputsTable).forEach { row -> inputsTableData.add("${row["settingsName"]} = ${row["value"] ?: ""}") }

        DataFrame.read(inputTouchDevice)
            .forEach { row -> inputTouchDeviceData.add("${row["settingsName"]} = ${row["value"] ?: ""}") }

        val hyprland = """
input {
    ${inputsData.joinToString("\n") { "\t$it" }}
                
    touchpad {
        ${inputsTouchpadData.joinToString("\n") { "\t$it" }}
    }
                
    touchdevice {
        ${inputTouchDeviceData.joinToString("\n") { "\t$it" }}
    }
                
    tablet {
        ${inputsTableData.joinToString("\n") { "\t\t$it" }}
    }
}""".trimIndent()

        hyprlandPath.writeText(hyprland)

        val modifiedTime = hyprlandPath.getLastModifiedTime().toKotlinInstant()

        logger.info("Update time is $modifiedTime")

        val updateTime = DataFrame.read(updatePath).update { "time"<Instant>() }
            .where { "path"<String>() == "$user/.config/hypr/hyprConfigAutoGen/inputs.conf" }.with { modifiedTime }

        logger.info("Time is updated")

        updateTime.writeCsv(updatePath)
    }

    fun FileTime.toKotlinInstant(): Instant = Instant.fromEpochMilliseconds(this.toMillis())
}