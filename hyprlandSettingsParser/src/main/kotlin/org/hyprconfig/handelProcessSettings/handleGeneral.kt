package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.GeneralModel
import org.hyprconfig.model.defaultGeneralValues
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel general Settings :")

private val generalPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/general.conf"

/**
 * Use to process and create general hyprland settings
 *
 * @param general as list of string
 * @return list of [GeneralModel]
 */
internal fun handleGeneral(general: List<String>): List<GeneralModel> {

    logger.info("Try to process and create hyprland general settings")

    val generalStore = defaultGeneralValues

    general.forEach {

        val processGeneral = it.split(":=").map { value -> value.trim() }

        val name = processGeneral.getOrNull(0) ?: return@forEach

        val value = processGeneral.getOrNull(1)?.split("=")?.map { values -> values.trim() } ?: return@forEach

        val valueName = value.getOrNull(0) ?: return@forEach

        val generalName = "$name:$valueName"

        val generalValue = value.getOrNull(1) ?: return@forEach

        generalStore.forEach innerLoop@{ general ->

            if (general.name == generalName) {

                val validatedGeneralValue = generalValue.hyprlandTypeCheck(general.type) ?: return@forEach

                if (validatedGeneralValue != general.value) general.value = validatedGeneralValue
            }

        }

    }

    logger.info("Creating hyprland general settings")

    handlePaths(generalPath)

    val generalSettings = generateGeneralSettings(generalStore)

    Path.of(generalPath).writeText(generalSettings)

    return generalStore.toList()
}

private fun generateGeneralSettings(general: List<GeneralModel>): String {
    return """
    general {
        border_size = ${general.find { it.name == "general:border_size" }!!.value}
        no_border_on_floating = ${general.find { it.name == "general:no_border_on_floating" }!!.value}
        gaps_in = ${general.find { it.name == "general:gaps_in" }!!.value}
        gaps_out = ${general.find { it.name == "general:gaps_out" }!!.value}
        gaps_workspaces = ${general.find { it.name == "general:gaps_workspaces" }!!.value}
        no_focus_fallback = ${general.find { it.name == "general:no_focus_fallback" }!!.value}
        resize_on_border = ${general.find { it.name == "general:resize_on_border" }!!.value}
        extend_border_grab_area = ${general.find { it.name == "general:extend_border_grab_area" }!!.value}
        hover_icon_on_border = ${general.find { it.name == "general:hover_icon_on_border" }!!.value}
        layout = ${general.find { it.name == "general:layout" }!!.value}
        allow_tearing = ${general.find { it.name == "general:allow_tearing" }!!.value}
        resize_corner = ${general.find { it.name == "general:resize_corner" }!!.value}
        col.active_border = ${general.find { it.name == "general:col.active_border" }!!.value}
        col.inactive_border = ${general.find { it.name == "general:col.inactive_border" }!!.value}
        col.nogroup_border = ${general.find { it.name == "general:col.nogroup_border" }!!.value}
        col.nogroup_border_active = ${general.find { it.name == "general:col.nogroup_border_active" }!!.value}
        snap {
            enabled = ${general.find { it.name == "general:snap:enabled" }!!.value}
            window_gap = ${general.find { it.name == "general:snap:window_gap" }!!.value}
            monitor_gap = ${general.find { it.name == "general:snap:monitor_gap" }!!.value}
            border_overlap = ${general.find { it.name == "general:snap:border_overlap" }!!.value}
        }
    }""".insertVariables().trimIndent()
}