package controllers

import logger
import model.tables.WorkspaceModel
import write.workspace.WriteWorkspace

/**
 * Try to parse workspaces
 *
 * @return [Result]<[List]<[WorkspaceModel]>>, a result of success and failer if success list of [WorkspaceModel]
 * @param workspace as list of [String]
 */
internal fun parseWorkspace(workspace: List<String>): Result<List<WorkspaceModel>> {

    logger.info("Try to process and handle workspace settings")

    val workspaceStore = mutableListOf<WorkspaceModel>()

    workspace.forEach {

        if (!it.startsWith("workspace")) return@forEach

        val processWorkspace = it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { workspace -> workspace.trim() } ?: return@forEach

        val workspaceName = processWorkspace.getOrNull(0) ?: return@forEach

        if (processWorkspace.size < 2) return@forEach

        val rules = processWorkspace.subList(1 ,processWorkspace.size).map { rule -> rule.trim() }.validateRules()

        workspaceStore.add(
            WorkspaceModel(workspaceName, rules)
        )
    }

    val write = WriteWorkspace()

    write.writeIntoHyprland(workspaceStore).getOrThrow()
    write.writeIntoDotConfig(workspaceStore).getOrThrow()

    return Result.success(workspaceStore.toList())
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