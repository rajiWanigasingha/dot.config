package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.ExecuteModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Execute Settings :")

private val executePath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/execute.conf"


/**
 * Use to process all `exec` settings and create it hyprland settings
 *
 * @param execute as a list of strings
 * @return list of [ExecuteModel]
 */
internal fun handleExecute(execute: List<String>): List<ExecuteModel> {

    logger.info("Try to process and create hyprland executes settings")

    val executeStore = mutableListOf<ExecuteModel>()

    execute.forEach {

        val executes = it.split("#")[0].split("=").map { exe -> exe.trim() }

        val exeKeyword = executes.getOrNull(0)?.validateKeyword() ?: return@forEach

        val exeValue = executes.getOrNull(1) ?: return@forEach

        executeStore.add(ExecuteModel(exeKeyword, exeValue))
    }

    val executeHyprlandSettings = mutableListOf<String>()

    handlePaths(executePath)

    executeStore.forEach {
        executeHyprlandSettings.add("${it.keyword} = ${it.command}".insertVariables())
    }

    Path.of(executePath).writeText(executeHyprlandSettings.joinToString("\n"))

    return executeStore.toList()
}

private fun String.validateKeyword(): String? {
    return when(this) {
        "exec" ,"exec-once" ,"exec-shutdown" ,"execr-once" ,"execr" -> this
        else -> null
    }
}