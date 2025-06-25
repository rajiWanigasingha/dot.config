package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.SubmapModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel submap Settings :")

private val submapPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/submap.conf"

/**
 * These uses to process and create submap hyprland settings
 *
 * @param submap as list of string
 * @return list of [SubmapModel]
 */
internal fun handleSubmap(submap: List<String>): List<SubmapModel> {

    logger.info("Try process and create hyprland submap settings.")

    val submapStore = mutableListOf<SubmapModel>()

    submap.forEach {

        val processSubmap = it.split("#")[0].split("=").getOrNull(1)?.trim() ?: return@forEach

        submapStore.add(SubmapModel(processSubmap))

    }

    logger.info("Creating submap hyprland settings")

    handlePaths(submapPath)

    val submapValues = mutableListOf<String>()

    submapStore.forEach { submapValues.add("submap = ${it.submap}".insertVariables()) }

    Path.of(submapPath).writeText(submapValues.joinToString("\n"))

    return submapStore.toList()
}