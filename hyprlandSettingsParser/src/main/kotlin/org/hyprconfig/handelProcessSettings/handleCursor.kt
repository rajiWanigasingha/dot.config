package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.CursorModel
import org.hyprconfig.model.defaultCursorSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Cursor Settings :")

private val cursorPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/cursor.conf"

internal fun handleCursor(cursor: List<String>): List<CursorModel> {

    logger.info("Try to process and create cursor hyprland settings.")

    val cursorStore = defaultCursorSettings

    cursor.forEach {

        val processCursor = it.split(":=").map { cursor -> cursor.trim() }

        val name = processCursor.getOrNull(0) ?: return@forEach

        val value = processCursor.getOrNull(1)?.split("=")?.map { cursor -> cursor.trim() } ?: return@forEach

        val cursorName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val cursorValue = value.getOrNull(1) ?: return@forEach

        cursorStore.forEach innerLoop@{ cursor ->
            if (cursor.name == cursorName) {
                val validateCursorValue = cursorValue.hyprlandTypeCheck(cursor.type) ?: return@forEach

                if (validateCursorValue != cursor.value) cursor.value = validateCursorValue
            }
        }
    }

    logger.info("Creating cursor hyprland settings file")

    handlePaths(cursorPath)

    val cursorSettings = generateCursorSettings(cursorStore)

    Path.of(cursorPath).writeText(cursorSettings)

    return cursorStore.toList()
}

fun generateCursorSettings(cursor: List<CursorModel>): String {
    return """
        cursor {
            no_hardware_cursors = ${cursor.find { it.name == "cursor:no_hardware_cursors" }!!.value}
            no_break_fs_vrr = ${cursor.find { it.name == "cursor:no_break_fs_vrr" }!!.value}
            min_refresh_rate = ${cursor.find { it.name == "cursor:min_refresh_rate" }!!.value}
            hotspot_padding = ${cursor.find { it.name == "cursor:hotspot_padding" }!!.value}
            inactive_timeout = ${cursor.find { it.name == "cursor:inactive_timeout" }!!.value}
            no_warps = ${cursor.find { it.name == "cursor:no_warps" }!!.value}
            persistent_warps = ${cursor.find { it.name == "cursor:persistent_warps" }!!.value}
            warp_on_change_workspace = ${cursor.find { it.name == "cursor:warp_on_change_workspace" }!!.value}
            warp_on_toggle_special = ${cursor.find { it.name == "cursor:warp_on_toggle_special" }!!.value}
            default_monitor = "${cursor.find { it.name == "cursor:default_monitor" }!!.value}"
            zoom_factor = ${cursor.find { it.name == "cursor:zoom_factor" }!!.value}
            zoom_rigid = ${(cursor.find { it.name == "cursor:zoom_rigid" }!!.value)}
            enable_hyprcursor = ${(cursor.find { it.name == "cursor:enable_hyprcursor" }!!.value)}
            sync_gsettings_theme = ${(cursor.find { it.name == "cursor:sync_gsettings_theme" }!!.value)}
            hide_on_key_press = ${(cursor.find { it.name == "cursor:hide_on_key_press" }!!.value)}
            hide_on_touch = ${(cursor.find { it.name == "cursor:hide_on_touch" }!!.value)}
            use_cpu_buffer = ${cursor.find { it.name == "cursor:use_cpu_buffer" }!!.value}
            warp_back_after_non_mouse_input = ${(cursor.find { it.name == "cursor:warp_back_after_non_mouse_input" }!!.value)}
        }
    """.insertVariables().trimIndent()
}
