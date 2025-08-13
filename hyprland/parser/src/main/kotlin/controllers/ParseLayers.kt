package controllers

import logger
import model.tables.LayerRulesModel
import write.layers.WriteLayers

/**
 * These use to process and create layer rules of hyprland
 *
 * @param layer as list of string
 * @return list of [LayerRulesModel]
 */
internal fun parseLayers(layer: List<String>): Result<List<LayerRulesModel>> {

    logger.info("Try to process and create hyprland layer settings")

    val layerRuleStore = mutableListOf<LayerRulesModel>()

    layer.forEach {

        if (!it.startsWith("layerrule")) return@forEach

        val processLayers = it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { layers -> layers.trim() } ?: return@forEach

        val rules = processLayers.getOrNull(0) ?: return@forEach

        val values = processLayers.getOrNull(1) ?: return@forEach

        layerRuleStore.add(LayerRulesModel(rules, values))
    }

    val write = WriteLayers()

    write.writeIntoHyprland(layerRuleStore).getOrThrow()
    write.writeIntoDotConfig(layerRuleStore).getOrThrow()

    return Result.success(layerRuleStore.toList())
}