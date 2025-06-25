package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.WorkspaceModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Workspace Settings :")

private val workspacePath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/workspace.conf"

internal fun handleWorkspaces(workspace: List<String>): List<WorkspaceModel> {

    logger.info("Try to process and handle workspace settings")

    val workspaceStore = mutableListOf<WorkspaceModel>()

    workspace.forEach {

        val processWorkspace = it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { workspace -> workspace.trim() } ?: return@forEach

        val workspaceName = processWorkspace.getOrNull(0) ?: return@forEach

        if (processWorkspace.size < 2) return@forEach

        val rules = processWorkspace.subList(1 ,processWorkspace.size).map { rule -> rule.trim() }.validateRules()

        workspaceStore.add(
            WorkspaceModel(workspaceName, rules)
        )
    }

    logger.info("Creating workspace settings")

    val workspaceSettings = mutableListOf<String>()

    handlePaths(workspacePath)

    workspaceStore.forEach {
        workspaceSettings.add("workspace = ${it.name} ,${it.rules.joinToString(" ,")}".insertVariables())
    }

    Path.of(workspacePath).writeText(workspaceSettings.joinToString("\n"))

    return workspaceStore.toList()
}

private fun List<String>.validateRules(): List<String> {

    val validateRules = mutableListOf<String>()

    this.forEach {
        when {
            it.startsWith("monitor:") -> validateRules.add(it)
            it.startsWith("default:") -> validateRules.add(it)
            it.startsWith("gapsin:") -> validateRules.add(it)
            it.startsWith("gapsout:") -> validateRules.add(it)
            it.startsWith("bordersize:") -> validateRules.add(it)
            it.startsWith("border:") -> validateRules.add(it)
            it.startsWith("shadow:") -> validateRules.add(it)
            it.startsWith("rounding:") -> validateRules.add(it)
            it.startsWith("decorate:") -> validateRules.add(it)
            it.startsWith("persistent:") -> validateRules.add(it)
            it.startsWith("on-created-empty:") -> validateRules.add(it)
            it.startsWith("defaultName:") -> validateRules.add(it)
        }
    }

    return validateRules.toList()
}