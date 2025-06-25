package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class CursorModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultCursorSettings = listOf(
    CursorModel("cursor:no_hardware_cursors", "2", HyprlandTypes.INT),
    CursorModel("cursor:no_break_fs_vrr", "2", HyprlandTypes.INT),
    CursorModel("cursor:min_refresh_rate", "24", HyprlandTypes.INT),
    CursorModel("cursor:hotspot_padding", "0", HyprlandTypes.INT),
    CursorModel("cursor:inactive_timeout", "0.0", HyprlandTypes.FLOAT),
    CursorModel("cursor:no_warps", "0", HyprlandTypes.INT),
    CursorModel("cursor:persistent_warps", "0", HyprlandTypes.INT),
    CursorModel("cursor:warp_on_change_workspace", "0", HyprlandTypes.INT),
    CursorModel("cursor:warp_on_toggle_special", "0", HyprlandTypes.INT),
    CursorModel("cursor:default_monitor", "", HyprlandTypes.STR),
    CursorModel("cursor:zoom_factor", "1.0", HyprlandTypes.FLOAT),
    CursorModel("cursor:zoom_rigid", "0", HyprlandTypes.BOOL),
    CursorModel("cursor:enable_hyprcursor", "1", HyprlandTypes.BOOL),
    CursorModel("cursor:sync_gsettings_theme", "1", HyprlandTypes.BOOL),
    CursorModel("cursor:hide_on_key_press", "0", HyprlandTypes.BOOL),
    CursorModel("cursor:hide_on_touch", "1", HyprlandTypes.BOOL),
    CursorModel("cursor:use_cpu_buffer", "2", HyprlandTypes.INT),
    CursorModel("cursor:warp_back_after_non_mouse_input", "0", HyprlandTypes.BOOL)
)
