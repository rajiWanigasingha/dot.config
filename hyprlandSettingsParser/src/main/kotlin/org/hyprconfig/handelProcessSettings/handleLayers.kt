package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.LayerRulesModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Layer Settings :")

private val layerPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/layer.conf"

/**
 * These use to process and create layer rules of hyprland
 *
 * @param layer as list of string
 * @return list of [LayerRulesModel]
 */
internal fun handleLayers(layer: List<String>): List<LayerRulesModel> {

    logger.info("Try to process and create hyprland layer settings")

    val layerRuleStore = mutableListOf<LayerRulesModel>()

    layer.forEach {

        val processLayers = it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { layers -> layers.trim() } ?: return@forEach

        val rules = processLayers.getOrNull(0) ?: return@forEach

        val values = processLayers.getOrNull(1) ?: return@forEach

        layerRuleStore.add(LayerRulesModel(rules, values))
    }

    logger.info("Creating hyprland layer rule settings")

    handlePaths(layerPath)

    val layers = mutableListOf<String>()

    layerRuleStore.forEach { layers.add("layerrule = ${it.rule} ,${it.value}".insertVariables()) }

    Path.of(layerPath).writeText(layers.joinToString("\n"))

    return layerRuleStore.toList()
}