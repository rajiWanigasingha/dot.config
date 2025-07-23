package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.dot.config.controller.ui.SidebarController
import org.dot.config.model.Tables
import org.hyprconfig.model.AddReserved
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class MonitorController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/monitor.csv"

    fun changeGeneral(general: Tables.MonitorTable): Boolean {
        logger.info("Changing General Monitor Settings")

        val monitorSettings = SidebarController().getMonitors().toMutableList()

        var updated = false

        val monitorChange: MutableList<Tables.MonitorTable> = monitorSettings.map {
            if (it.name == general.name) {
                updated = true
                general
            } else {
                it
            }
        }.toMutableList()

        monitorChange.forEach { logger.info(it.toString()) }

        if (!updated) {
            monitorChange.add(general)
        }

        val monitorDataFrame = monitorChange.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(monitor = monitorDataFrame)

        writeIntoHyprland(data = monitorChange)

        return true
    }

    fun disableMonitor(general: Tables.MonitorTable, disable: Boolean): Boolean {
        logger.info("Disable ${general.name} this monitor")
        val monitorSettings = SidebarController().getMonitors().toMutableList()

        if (disable) {
            monitorSettings.add(general)

            val monitorDataFrame = monitorSettings.toDataFrame().convert { all() }.with { it.toString() }

            writeAllToCsv(monitor = monitorDataFrame)

            writeIntoHyprland(data = monitorSettings)

        } else {
            val monitorRemoveDisable =
                monitorSettings.filter { !(it.disable && it.name == general.name) }.toMutableList()

            val monitorDataFrame = monitorRemoveDisable.toDataFrame().convert { all() }.with { it.toString() }

            writeAllToCsv(monitor = monitorDataFrame)

            writeIntoHyprland(data = monitorRemoveDisable)

        }

        return true
    }

    fun addReserved(reserved: Tables.MonitorTable, reset: Boolean): Boolean {
        logger.info("Add Reserved area for ${reserved.name}")

        val monitorSettings = SidebarController().getMonitors().toMutableList()

        if (reset) {

            var update = false

            val updated = monitorSettings.map {
                if (it.name == reserved.name && it.addreserved != null) {
                    update = true
                    it.copy(addreserved = reserved.addreserved)
                } else {
                    it
                }
            }.toMutableList()

            logger.info("Update If Exist")
            updated.forEach { logger.info(it.toString()) }

            if (!update) {
                updated.add(reserved)
            }

            logger.info("Add If Not Exit")
            updated.forEach { logger.info(it.toString()) }

            val addReservedDataFrame = updated.toDataFrame().convert { all() }.with { it.toString() }

            writeAllToCsv(monitor = addReservedDataFrame)

            writeIntoHyprland(data = updated)

        } else {

            val updated = monitorSettings.filter { !(it.name == reserved.name && it.addreserved != null) }

            val addReservedDataFrame = updated.toDataFrame().convert { all() }.with { it.toString() }

            writeAllToCsv(monitor = addReservedDataFrame)

            writeIntoHyprland(data = updated)
        }

        return true
    }

    fun mirror(mirror: Tables.MonitorTable): Boolean {
        logger.info("Add Or Remove Mirror Settings")

        val monitorSettings = SidebarController().getMonitors().toMutableList()

        val mirrored = monitorSettings.map {
            if (it.name == mirror.name) {
                it.copy(mirror = mirror.mirror)
            } else {
                it
            }
        }.toMutableList()

        val mirrorMonitor = mirrored.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(monitor = mirrorMonitor)

        writeIntoHyprland(data = mirrored)

        return true
    }

    private fun writeAllToCsv(monitor: DataFrame<*>) {
        monitor.writeCsv(path)
    }

    private fun writeIntoHyprland(data: List<Tables.MonitorTable>) {

        logger.info("Write AutoStart Into Hyprland")

        val value = WriteIntoHyprland().writeMonitor(monitor = data)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/monitor.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }
}