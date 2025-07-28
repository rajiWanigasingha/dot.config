package org.dot.config.controller.services

import kotlinx.datetime.Instant
import org.dot.config.model.Tables
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.forEach
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

    fun writeVariables(variables: DataFrame<*>): String {

        val variableSettings = mutableListOf<Tables.Variables>()

        variables.forEach { row ->
            variableSettings.add(
                Tables.Variables(
                    name = row["name"].toString(),
                    value = row["value"].toString()
                )
            )
        }

        val newHyprlandSettings = mutableListOf<String>()

        variableSettings.forEach {
            newHyprlandSettings.add("${it.name} = ${it.value}")
        }

        return newHyprlandSettings.joinToString("\n")
    }

    fun writeAutoStart(autoStart: DataFrame<*>): String {

        val autoStartSettings = mutableListOf<Tables.AutoStart>()

        autoStart.forEach { row ->
            autoStartSettings.add(
                Tables.AutoStart(
                    keyword = row["keyword"].toString(),
                    command = row["command"].toString()
                )
            )
        }

        val newHyprlandSettings = mutableListOf<String>()

        autoStartSettings.forEach {
            newHyprlandSettings.add("${it.keyword} = ${it.command}")
        }

        return newHyprlandSettings.joinToString("\n")
    }

    fun writeEnv(env: DataFrame<*>): String {

        val envSettings = mutableListOf<Tables.Env>()

        env.forEach { row ->
            envSettings.add(
                Tables.Env(
                    keyword = row["keyword"].toString(),
                    value = row["value"].toString()
                )
            )
        }

        val newHyprlandSettings = mutableListOf<String>()

        envSettings.forEach {
            newHyprlandSettings.add("${it.keyword} = ${it.value}")
        }

        return newHyprlandSettings.joinToString("\n")
    }

    fun writeKeybind(keybind: DataFrame<*>): String {

        val keybindsSettings = mutableListOf<Tables.KeybindTable>()

        keybind.forEach { row ->
            keybindsSettings.add(
                Tables.KeybindTable(
                    flags = row["flags"].toString().removePrefix("[").removeSuffix("]").split(",")
                        .mapNotNull { it.trim().toCharArray().getOrNull(0) },
                    mod = row["mod"].toString().removePrefix("[").removeSuffix("]").split(",").map { it.trim() },
                    keys = row["keys"].toString().removePrefix("[").removeSuffix("]").split(",").map { it.trim() },
                    description = row["description"].toString(),
                    dispatcher = row["dispatcher"].toString(),
                    args = row["args"].toString()
                )
            )
        }

        val hyprlandKey = mutableListOf<String>()

        keybindsSettings.forEach {
            hyprlandKey.add(
                "bind${it.flags.joinToString("")} = ${it.mod.joinToString(" ")} ,${it.keys.joinToString(" ")}${if (it.description != "null" && it.description != "") " ,${it.description}" else ""} ,${it.dispatcher}${if (it.args != "null" && it.args != "") " ,${it.args}" else ""}"
            )
        }

        return hyprlandKey.joinToString("\n")
    }

    fun writeMonitor(monitor: List<Tables.MonitorTable>): String {
        val hyprlandMonitor = mutableListOf<String>()

        monitor.forEach {
            if (it.disable) {
                hyprlandMonitor.add("monitor = ${it.name} ,disable")
                return@forEach
            } else if (it.addreserved != null) {
                hyprlandMonitor.add(
                    "monitor = ${it.name} ,addreserved ,${it.addreserved.getOrNull(0) ?: 0} ,${
                        it.addreserved.getOrNull(
                            1
                        ) ?: 0
                    } ,${it.addreserved.getOrNull(2) ?: 0} ,${it.addreserved.getOrNull(3) ?: 0}"
                )
                return@forEach
            } else {
                val mirror = if (it.mirror != null) " ,mirror ,${it.mirror}" else ""
                val bitdepth = if (it.bitDepth != null) " ,bitdepth ,${it.bitDepth}" else ""
                val transform = if (it.transform != null) " ,transform ,${it.transform}" else ""
                val cm = if (it.cm != null) " ,cm ,${it.cm}" else ""
                val sdrbrightness = if (it.sdrbrightness != null) " ,sdrbrightness ,${it.sdrbrightness}" else ""
                val sdrsaturation = if (it.sdrsaturation != null) " ,sdrsaturation ,${it.sdrsaturation}" else ""
                val vvr = if (it.vrr != null) " ,vrr ,${it.vrr}" else ""

                hyprlandMonitor.add("monitor = ${it.name} ,${it.resolution} ,${it.position} ,${it.scale}${mirror}${bitdepth}${transform}${cm}${sdrbrightness}${sdrsaturation}${vvr}")
            }
        }

        return hyprlandMonitor.joinToString("\n")
    }

    fun writeBezier(bezier: List<Tables.BezierTable>): String {
        val curves = mutableListOf<String>()

        bezier.forEach {
            curves.add(
                "bezier = ${it.name} ,${it.x0} ,${it.y0} ,${it.x1} ,${it.y1}"
            )
        }

        return curves.joinToString("\n")
    }

    fun writeAnimation(animation: List<Tables.AnimationTable>): String {
        val animationTree = mutableListOf<String>()

        animation.forEach {

            animationTree.add(
                "animation = ${it.name}, ${it.onOff}${if (it.speed != null) ", ${it.speed}" else ""}${if (it.curve != null && it.curve != "" && it.curve != "null") ", ${it.curve}" else ""}${if (it.style != null && it.style != "" && it.style != "null") " ,${it.style}" else ""}"
            )
        }

        return animationTree.joinToString("\n")
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