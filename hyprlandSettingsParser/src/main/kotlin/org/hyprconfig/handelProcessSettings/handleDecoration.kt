package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.DecorationModel
import org.hyprconfig.model.defaultDecorationSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel decoration Settings :")

private val decorationPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/decoration.conf"

internal fun handleDecoration(decoration: List<String>): List<DecorationModel> {

    logger.info("Try to process and create hyprland decoration settings.")

    val decorationStore = defaultDecorationSettings

    decoration.forEach {

        val processDecoration = it.split(":=").map { decoration -> decoration.trim() }

        val name = processDecoration.getOrNull(0)?.trim() ?: return@forEach

        val value =
            processDecoration.getOrNull(1)?.split("=")?.map { decoration -> decoration.trim() } ?: return@forEach

        val decorationName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val decorationValue = value.getOrNull(1)?.trim() ?: return@forEach

        decorationStore.forEach innerLoop@{ decoration ->
            if (decoration.name == decorationName) {
                val validateDecoration = decorationValue.hyprlandTypeCheck(decoration.type) ?: return@forEach

                if (validateDecoration != decoration.value) decoration.value = validateDecoration
            }
        }
    }

    logger.info("Creating hyprland decoration settings")

    handlePaths(decorationPath)

    val decorationSettings = generateDecorationSettings(decorationStore)

    Path.of(decorationPath).writeText(decorationSettings)

    return decorationStore.toList()
}

private fun generateDecorationSettings(decoration: List<DecorationModel>): String {
    return """
        decoration {
            rounding = ${decoration.find { it.name == "decoration:rounding" }!!.value}
            rounding_power = ${decoration.find { it.name == "decoration:rounding_power" }!!.value}
            active_opacity = ${decoration.find { it.name == "decoration:active_opacity" }!!.value}
            inactive_opacity = ${decoration.find { it.name == "decoration:inactive_opacity" }!!.value}
            fullscreen_opacity = ${decoration.find { it.name == "decoration:fullscreen_opacity" }!!.value}
            dim_inactive = ${decoration.find { it.name == "decoration:dim_inactive" }!!.value}
            dim_strength = ${decoration.find { it.name == "decoration:dim_strength" }!!.value}
            dim_special = ${decoration.find { it.name == "decoration:dim_special" }!!.value}
            dim_around = ${decoration.find { it.name == "decoration:dim_around" }!!.value}
            screen_shader = ${decoration.find { it.name == "decoration:screen_shader" }!!.value}
            border_part_of_window = ${decoration.find { it.name == "decoration:border_part_of_window" }!!.value}
            
            blur {
                enabled = ${decoration.find { it.name == "decoration:blur:enabled" }!!.value}
                size = ${decoration.find { it.name == "decoration:blur:size" }!!.value}
                passes = ${decoration.find { it.name == "decoration:blur:passes" }!!.value}
                ignore_opacity = ${decoration.find { it.name == "decoration:blur:ignore_opacity" }!!.value}
                new_optimizations = ${decoration.find { it.name == "decoration:blur:new_optimizations" }!!.value}
                xray = ${decoration.find { it.name == "decoration:blur:xray" }!!.value}
                contrast = ${decoration.find { it.name == "decoration:blur:contrast" }!!.value}
                brightness = ${decoration.find { it.name == "decoration:blur:brightness" }!!.value}
                vibrancy = ${decoration.find { it.name == "decoration:blur:vibrancy" }!!.value}
                vibrancy_darkness = ${decoration.find { it.name == "decoration:blur:vibrancy_darkness" }!!.value}
                noise = ${decoration.find { it.name == "decoration:blur:noise" }!!.value}
                special = ${decoration.find { it.name == "decoration:blur:special" }!!.value}
                popups = ${decoration.find { it.name == "decoration:blur:popups" }!!.value}
                popups_ignorealpha = ${decoration.find { it.name == "decoration:blur:popups_ignorealpha" }!!.value}
                input_methods = ${decoration.find { it.name == "decoration:blur:input_methods" }!!.value}
                input_methods_ignorealpha = ${decoration.find { it.name == "decoration:blur:input_methods_ignorealpha" }!!.value}
            }

            shadow {
                enabled = ${decoration.find { it.name == "decoration:shadow:enabled" }!!.value}
                range = ${decoration.find { it.name == "decoration:shadow:range" }!!.value}
                render_power = ${decoration.find { it.name == "decoration:shadow:render_power" }!!.value}
                ignore_window = ${decoration.find { it.name == "decoration:shadow:ignore_window" }!!.value}
                offset = ${decoration.find { it.name == "decoration:shadow:offset" }!!.value}
                scale = ${decoration.find { it.name == "decoration:shadow:scale" }!!.value}
                sharp = ${decoration.find { it.name == "decoration:shadow:sharp" }!!.value}
                color = ${decoration.find { it.name == "decoration:shadow:color" }!!.value}
                color_inactive = ${decoration.find { it.name == "decoration:shadow:color_inactive" }!!.value}
            }
            
        }
    """.insertVariables().trimIndent()
}

