package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class GeneralModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

internal val defaultGeneralValues = listOf(
    GeneralModel("general:border_size", "1", HyprlandTypes.INT),
    GeneralModel("general:no_border_on_floating", "0", HyprlandTypes.BOOL),
    GeneralModel("general:gaps_in", "5", HyprlandTypes.INT),
    GeneralModel("general:gaps_out", "20", HyprlandTypes.INT),
    GeneralModel("general:gaps_workspaces", "0", HyprlandTypes.INT),
    GeneralModel("general:no_focus_fallback", "0", HyprlandTypes.BOOL),
    GeneralModel("general:resize_on_border", "0", HyprlandTypes.BOOL),
    GeneralModel("general:extend_border_grab_area", "15", HyprlandTypes.INT),
    GeneralModel("general:hover_icon_on_border", "1", HyprlandTypes.BOOL),
    GeneralModel("general:layout", "dwindle", HyprlandTypes.STR),
    GeneralModel("general:allow_tearing", "0", HyprlandTypes.BOOL),
    GeneralModel("general:resize_corner", "0", HyprlandTypes.INT),
    GeneralModel("general:snap:enabled", "0", HyprlandTypes.BOOL),
    GeneralModel("general:snap:window_gap", "10", HyprlandTypes.INT),
    GeneralModel("general:snap:monitor_gap", "10", HyprlandTypes.INT),
    GeneralModel("general:snap:border_overlap", "0", HyprlandTypes.BOOL),
    GeneralModel("general:col.active_border", "0xffffffff", HyprlandTypes.GRADIENT),
    GeneralModel("general:col.inactive_border", "0xff444444", HyprlandTypes.GRADIENT),
    GeneralModel("general:col.nogroup_border", "0xffffaaff", HyprlandTypes.GRADIENT),
    GeneralModel("general:col.nogroup_border_active", "0xffff00ff", HyprlandTypes.GRADIENT)
)
