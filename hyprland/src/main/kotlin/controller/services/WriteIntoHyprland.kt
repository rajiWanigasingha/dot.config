package org.dot.config.controller.services

import kotlinx.datetime.Instant
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import kotlin.io.path.getLastModifiedTime

class WriteIntoHyprland {

    private val dataStorePath = "${System.getProperty("user.home")}/.dot.config/data"
    private val updatePath = "$dataStorePath/parsedTime.csv"
    private val logger = LoggerFactory.getLogger(javaClass.name)

    fun write(paths: List<String>): MutableList<String> {
        logger.info("Write into hyprland")

        val data = mutableListOf<List<String>>()

        val settings = mutableListOf<String>()

        paths.forEach {
            val settings = mutableListOf<String>()

            DataFrame.readCsv(
                fileOrUrl = "$dataStorePath/$it",
                colTypes = mapOf("value" to ColType.String, "validate" to ColType.String)
            ).forEach { row -> settings.add("${row["settingsName"]} = ${row["value"] ?: ""}") }

            data.add(settings)
        }

        data.forEach {
            settings.add(it.joinToString("\n"))
        }

        return settings
    }

    fun updateTime(hyprlandPath: String) {
        val modifiedTime = Path.of(hyprlandPath).getLastModifiedTime().toKotlinInstant()

        logger.info("Update time is $modifiedTime")

        val updateTime = DataFrame.read(updatePath).update { "time"<Instant>() }
            .where { "path"<String>() == hyprlandPath }.with { modifiedTime }

        logger.info("Time is updated")

        updateTime.writeCsv(updatePath)
    }

    fun FileTime.toKotlinInstant(): Instant = Instant.fromEpochMilliseconds(this.toMillis())
}