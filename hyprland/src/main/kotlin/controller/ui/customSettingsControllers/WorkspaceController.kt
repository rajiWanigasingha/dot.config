package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.dot.config.controller.ui.SidebarController
import org.dot.config.model.SendAndReceive
import org.dot.config.model.Tables
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class WorkspaceController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/workspaceRules.csv"

    fun edit(workspace: Tables.Workspace): Boolean {
        logger.info("Edit Workspace Rules")
        val allWorkspace = SidebarController().getWorkspace().toMutableList()

        val newWorkspace = allWorkspace.map {
            if (it.name == workspace.name) {
                workspace
            } else {
                it
            }
        }

        val workspaceDF = mutableListOf<Tables.WorkspaceRules>()

        newWorkspace.forEach {
            workspaceDF.add(
                Tables.WorkspaceRules(
                    name = it.name ,rules = WriteIntoHyprland().writeWorkspaceHelper(it).replaceFirst("," ,"").trim().split(',').map { s -> s.trim() }
                )
            )
        }

        writeAllToCsv(workspace = workspaceDF.toDataFrame().convert { all() }.with { it.toString() })
        writeIntoHyprland(workspace = newWorkspace)

        return true
    }

    fun delete(workspace: Tables.Workspace ,delete: SendAndReceive.WorkspaceDeleteOder): Boolean {
        logger.info("Delete Workspace Rules")
        val allWorkspace = SidebarController().getWorkspace().toMutableList()

        if (delete == SendAndReceive.WorkspaceDeleteOder.ALL) {
            val deleteWorkspace = allWorkspace.filter { it.name != workspace.name }

            val deleteWorkspaceDF = mutableListOf<Tables.WorkspaceRules>()

            deleteWorkspace.forEach { deleteWorkspaceDF.add(Tables.WorkspaceRules(name = it.name , rules = WriteIntoHyprland().writeWorkspaceHelper(it).replaceFirst("," ,"").trim().split(',').map { s -> s.trim() })) }

            writeAllToCsv(workspace = deleteWorkspaceDF.toDataFrame().convert { all() }.with { it.toString() })
            writeIntoHyprland(workspace = deleteWorkspace)
        } else {
            val deleteWorkspace = allWorkspace.map {
                if (it.name == workspace.name) {
                    workspace
                } else {
                    it
                }
            }

            val deleteWorkspaceDF = mutableListOf<Tables.WorkspaceRules>()

            deleteWorkspace.forEach { deleteWorkspaceDF.add(Tables.WorkspaceRules(name = it.name , rules = WriteIntoHyprland().writeWorkspaceHelper(it).replaceFirst("," ,"").trim().split(',').map { s -> s.trim() })) }

            writeAllToCsv(workspace = deleteWorkspaceDF.toDataFrame().convert { all() }.with { it.toString() })
            writeIntoHyprland(workspace = deleteWorkspace)
        }

        return true
    }

    fun addNew(workspace: Tables.Workspace): Boolean {
        logger.info("Add New Workspace Rules")
        val allWorkspace = SidebarController().getWorkspace().toMutableList()

        allWorkspace.add(workspace)

        val workspaceDF = mutableListOf<Tables.WorkspaceRules>()

        allWorkspace.forEach { workspaceDF.add(Tables.WorkspaceRules(name = it.name , rules = WriteIntoHyprland().writeWorkspaceHelper(it).replaceFirst("," ,"").trim().split(',').map { s -> s.trim() })) }

        writeAllToCsv(workspace = workspaceDF.toDataFrame().convert { all() }.with { it.toString() })
        writeIntoHyprland(workspace = allWorkspace)

        return true
    }

    private fun writeAllToCsv(workspace: DataFrame<*>) {
        workspace.writeCsv(path)
    }

    private fun writeIntoHyprland(workspace: List<Tables.Workspace>) {

        logger.info("Write Workspace Rules Into Hyprland")

        val value = WriteIntoHyprland().writeWorkspace(workspace = workspace)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/workspace.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }
}