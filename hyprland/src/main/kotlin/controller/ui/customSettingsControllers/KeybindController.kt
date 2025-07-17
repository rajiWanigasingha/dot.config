package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.dot.config.model.SendAndReceive
import org.dot.config.model.Tables
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.append
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.drop
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.last
import org.jetbrains.kotlinx.dataframe.api.parser
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.jetbrains.kotlinx.dataframe.size
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class KeybindController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/keybind.csv"

    fun getHelpForKeys(): String? {
        logger.info("Getting Keybind Help Page")

        val content = object {}.javaClass.getResource("/help/KEYBINDS/Keys.md")?.readText()

        return content
    }

    fun getDispatcherHelp(command: String): String? {
        logger.info("Getting Dispatcher ${command}, Help Page")

        val content = object {}.javaClass.getResource("/dispatchers/help/${command}.md")?.readText()

        return content
    }

    fun getHelpForDispatchers(): String? {
        logger.info("Getting Dispatcher Help Page")

        val content = object {}.javaClass.getResource("/help/KEYBINDS/dispatcher.md")?.readText()

        return content
    }

    fun getDispatchers(): List<Tables.KeybindDispatcher>? {
        logger.info("Getting Dispatchers")

        val resourceStream = object {}.javaClass.getResourceAsStream("/dispatchers/keybind.csv") ?: return null

        val dataFrame = DataFrame.readCsv(
            inputStream = resourceStream,
            colTypes = mapOf("name" to ColType.String, "command" to ColType.String, "description" to ColType.String)
        )

        val dispatchers = mutableListOf<Tables.KeybindDispatcher>()

        dataFrame.forEach { row ->
            dispatchers.add(
                Tables.KeybindDispatcher(
                    name = row["name"].toString(),
                    command = row["command"].toString(),
                    description = row["description"].toString()
                )
            )
        }

        return dispatchers.toList()
    }

    fun createNewBinds(keybinds: Tables.KeybindTable): Boolean {
        logger.info("Creating keybinds")

        val keybind = readKeybinds()

        val newKeybinds = keybind.append(
            keybinds.flags.toString(),
            keybinds.mod.toString(),
            keybinds.keys.toString(),
            keybinds.description,
            keybinds.dispatcher,
            keybinds.args
        )

        writeAllToCsv(newKeybinds)

        writeIntoHyprland(data = newKeybinds)

        return true
    }

    fun deleteKeybind(delete: SendAndReceive.KeybindDelete): Boolean {
        logger.info("Deleting Keybind")

        val keybind = readKeybinds()

        val deleteBinds = keybind.drop { "mod"<String>() == delete.mod.toString() && "keys"<String>() == delete.key.toString() && "flags"<String>() == delete.flags.toString() }

        writeAllToCsv(keybinds = deleteBinds)

        writeIntoHyprland(data = deleteBinds)

        return true
    }

    fun editKeybind(keybinds: Tables.KeybindTable ,oldKeybinds: Tables.KeybindTable): Boolean {
        logger.info("Edit Keybinds")

        val keybind = readKeybinds().turnIntoValidKotlin()

        val updated = keybind.map {
            if (it.mod == oldKeybinds.mod && it.keys == oldKeybinds.keys && it.flags == oldKeybinds.flags) {
                it.copy(
                    mod = keybinds.mod,
                    keys = keybinds.keys,
                    flags = keybinds.flags,
                    description = keybinds.description,
                    dispatcher = keybinds.dispatcher,
                    args = keybinds.args
                )
            } else it
        }

        val updatedKeybinds = updated.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(updatedKeybinds)

        writeIntoHyprland(data = updatedKeybinds)

        return true
    }

    private fun readKeybinds(): DataFrame<*> {
        return DataFrame.readCsv(
            fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/keybind.csv",
            colTypes = mapOf(
                "flags" to ColType.String,
                "mod" to ColType.String,
                "keys" to ColType.String,
                "description" to ColType.String,
                "dispatcher" to ColType.String,
                "args" to ColType.String
            )
        )
    }

    private fun writeAllToCsv(keybinds: DataFrame<*>) {
        keybinds.writeCsv(path)
    }

    private fun writeIntoHyprland(data: DataFrame<*>) {

        logger.info("Write Keybind Into Hyprland")

        val value = WriteIntoHyprland().writeKeybind(keybind = data)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bind.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }

    private fun DataFrame<*>.turnIntoValidKotlin(): MutableList<Tables.KeybindTable> {
        val keybindsSettings = mutableListOf<Tables.KeybindTable>()

        this.forEach { row ->
            keybindsSettings.add(
                Tables.KeybindTable(
                    flags = row["flags"].toString().removePrefix("[").removeSuffix("]").split(",").mapNotNull { it.trim().toCharArray().getOrNull(0) },
                    mod = row["mod"].toString().removePrefix("[").removeSuffix("]").split(",").map { it.trim() },
                    keys = row["keys"].toString().removePrefix("[").removeSuffix("]").split(",").map { it.trim() },
                    description = row["description"].toString(),
                    dispatcher = row["dispatcher"].toString(),
                    args = row["args"].toString()
                )
            )
        }

        return keybindsSettings
    }
}