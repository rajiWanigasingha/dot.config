package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class BindsModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultBindsSettings = listOf(
    BindsModel("binds:pass_mouse_when_bound", "0", HyprlandTypes.BOOL),
    BindsModel("binds:scroll_event_delay", "300", HyprlandTypes.INT),
    BindsModel("binds:workspace_back_and_forth", "0", HyprlandTypes.BOOL),
    BindsModel("binds:hide_special_on_workspace_change", "0", HyprlandTypes.BOOL),
    BindsModel("binds:allow_workspace_cycles", "0", HyprlandTypes.BOOL),
    BindsModel("binds:workspace_center_on", "0", HyprlandTypes.INT),
    BindsModel("binds:focus_preferred_method", "0", HyprlandTypes.INT),
    BindsModel("binds:ignore_group_lock", "0", HyprlandTypes.BOOL),
    BindsModel("binds:movefocus_cycles_fullscreen", "0", HyprlandTypes.BOOL),
    BindsModel("binds:movefocus_cycles_groupfirst", "0", HyprlandTypes.BOOL),
    BindsModel("binds:disable_keybind_grabbing", "0", HyprlandTypes.BOOL),
    BindsModel("binds:window_direction_monitor_fallback", "1", HyprlandTypes.BOOL),
    BindsModel("binds:allow_pin_fullscreen", "0", HyprlandTypes.BOOL),
    BindsModel("binds:drag_threshold", "0", HyprlandTypes.INT),
)