package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.DwindleModel
import org.hyprconfig.model.defaultDwindleSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel dwindle Settings :")

private val dwindlePath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/dwindle.conf"

internal fun handleDwindle(dwindle: List<String>): List<DwindleModel> {

    logger.info("Try to process and create hyprland dwindle settings")

    val dwindleStore = defaultDwindleSettings

    dwindle.forEach {

        val processDwindle = it.split("=").map { dwindle -> dwindle.trim() }

        val name = processDwindle.getOrNull(0) ?: return@forEach

        val value = processDwindle.getOrNull(1)?.split("=")?.map { dwindle -> dwindle.trim() } ?: return@forEach

        val dwindleName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val dwindleValue = value.getOrNull(1) ?: return@forEach

        dwindleStore.forEach innerLoop@ { dwindle ->
            if (dwindle.name == dwindleName) {
                val validateDwindle = dwindleValue.hyprlandTypeCheck(dwindle.type) ?: return@forEach

                if (validateDwindle != dwindle.value) dwindle.value = validateDwindle
            }
        }
    }

    logger.info("Creating dwindle hyprland settings")

    handlePaths(dwindlePath)

    val dwindleSettings = generateDwindleSettings(dwindleStore)

    Path.of(dwindlePath).writeText(dwindleSettings)

    return dwindleStore.toList()
}

private fun generateDwindleSettings(dwindle: List<DwindleModel>): String {
    return """
        dwindle {
            pseudotile = ${dwindle.find { it.name == "dwindle:pseudotile" }!!.value}
            force_split = ${dwindle.find { it.name == "dwindle:force_split" }!!.value}
            permanent_direction_override = ${dwindle.find { it.name == "dwindle:permanent_direction_override" }!!.value}
            preserve_split = ${dwindle.find { it.name == "dwindle:preserve_split" }!!.value}
            special_scale_factor = ${dwindle.find { it.name == "dwindle:special_scale_factor" }!!.value}
            split_width_multiplier = ${dwindle.find { it.name == "dwindle:split_width_multiplier" }!!.value}
            use_active_for_splits = ${dwindle.find { it.name == "dwindle:use_active_for_splits" }!!.value}
            default_split_ratio = ${dwindle.find { it.name == "dwindle:default_split_ratio" }!!.value}
            split_bias = ${dwindle.find { it.name == "dwindle:split_bias" }!!.value}
            smart_split = ${dwindle.find { it.name == "dwindle:smart_split" }!!.value}
            smart_resizing = ${dwindle.find { it.name == "dwindle:smart_resizing" }!!.value}
        }
    """.insertVariables().trimIndent()
}