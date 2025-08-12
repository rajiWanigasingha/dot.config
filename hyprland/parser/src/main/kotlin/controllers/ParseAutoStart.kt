package controllers

import logger
import model.tables.ExecuteModel
import write.autoStart.WriteAutoStart

/**
 * Use to process all `exec` settings and create it hyprland settings
 *
 * @param execute as a list of strings
 * @return list of [ExecuteModel]
 */
internal fun parseExecute(execute: List<String>): Result<List<ExecuteModel>> {

    logger.info("Try to process and create hyprland executes settings")

    val executeStore = mutableListOf<ExecuteModel>()

    execute.forEach {

        val executes = it.split("#")[0].split("=").map { exe -> exe.trim() }

        val exeKeyword = executes.getOrNull(0)?.validateKeyword() ?: return@forEach

        val exeValue = executes.getOrNull(1) ?: return@forEach

        executeStore.add(ExecuteModel(exeKeyword, exeValue))
    }

    val write = WriteAutoStart()

    write.writeIntoHyprland(executeStore).getOrThrow()
    write.writeIntoDotConfig(executeStore).getOrThrow()

    return Result.success(executeStore.toList())
}

private fun String.validateKeyword(): String? {
    return when(this) {
        "exec" ,"exec-once" ,"exec-shutdown" ,"execr-once" ,"execr" -> this
        else -> null
    }
}