package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.BindsModel
import org.hyprconfig.model.defaultBindsSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Binds Settings :")

private val bindsPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/binds.conf"

internal fun handleBinds(binds: List<String>): List<BindsModel> {

    logger.info("Try to process and create binds hyprland settings")

    val bindsStore = defaultBindsSettings

    binds.forEach {

        val processBinds = it.split(":=").map { bind -> bind.trim() }

        val name = processBinds.getOrNull(0) ?: return@forEach

        val value = processBinds.getOrNull(1)?.split("=")?.map { bind -> bind.trim() } ?: return@forEach

        val bindsName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val bindsValue = value.getOrNull(1) ?: return@forEach

        bindsStore.forEach innerLoop@ { binds ->
            if (bindsName == binds.name) {
                val validateBindSettings = bindsValue.hyprlandTypeCheck(binds.type) ?: return@forEach

                if (validateBindSettings != binds.value) binds.value = validateBindSettings
            }
        }
    }

    logger.info("Creating hyprland binds settings")

    handlePaths(bindsPath)

    val bindsSettings = generateBindsSettings(bindsStore)

    Path.of(bindsPath).writeText(bindsSettings)

    return bindsStore.toList()
}

private fun generateBindsSettings(binds: List<BindsModel>): String {
    return """
        binds {
            pass_mouse_when_bound = ${binds.find { it.name == "binds:pass_mouse_when_bound" }!!.value}
            scroll_event_delay = ${binds.find { it.name == "binds:scroll_event_delay" }!!.value}
            workspace_back_and_forth = ${binds.find { it.name == "binds:workspace_back_and_forth" }!!.value}
            hide_special_on_workspace_change = ${binds.find { it.name == "binds:hide_special_on_workspace_change" }!!.value}
            allow_workspace_cycles = ${binds.find { it.name == "binds:allow_workspace_cycles" }!!.value}
            workspace_center_on = ${binds.find { it.name == "binds:workspace_center_on" }!!.value}
            focus_preferred_method = ${binds.find { it.name == "binds:focus_preferred_method" }!!.value}
            ignore_group_lock = ${binds.find { it.name == "binds:ignore_group_lock" }!!.value}
            movefocus_cycles_fullscreen = ${binds.find { it.name == "binds:movefocus_cycles_fullscreen" }!!.value}
            movefocus_cycles_groupfirst = ${binds.find { it.name == "binds:movefocus_cycles_groupfirst" }!!.value}
            disable_keybind_grabbing = ${binds.find { it.name == "binds:disable_keybind_grabbing" }!!.value}
            window_direction_monitor_fallback = ${binds.find { it.name == "binds:window_direction_monitor_fallback" }!!.value}
            allow_pin_fullscreen = ${binds.find { it.name == "binds:allow_pin_fullscreen" }!!.value}
            drag_threshold = ${binds.find { it.name == "binds:drag_threshold" }!!.value}
        }
    """.insertVariables().trimIndent()
}