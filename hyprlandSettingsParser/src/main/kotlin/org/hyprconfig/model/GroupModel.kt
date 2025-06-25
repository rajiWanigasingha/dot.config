package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class GroupModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultGroupSettings = listOf(
    GroupModel("group:insert_after_current", "1", HyprlandTypes.BOOL),
    GroupModel("group:focus_removed_window", "1", HyprlandTypes.BOOL),
    GroupModel("group:merge_groups_on_drag", "1", HyprlandTypes.BOOL),
    GroupModel("group:merge_groups_on_groupbar", "1", HyprlandTypes.BOOL),
    GroupModel("group:merge_floated_into_tiled_on_groupbar", "0", HyprlandTypes.BOOL),
    GroupModel("group:auto_group", "1", HyprlandTypes.BOOL),
    GroupModel("group:drag_into_group", "1", HyprlandTypes.INT),
    GroupModel("group:group_on_movetoworkspace", "0", HyprlandTypes.BOOL),
    GroupModel("group:col.border_active", "0x66ffff00", HyprlandTypes.GRADIENT),
    GroupModel("group:col.border_inactive", "0x66777700", HyprlandTypes.GRADIENT),
    GroupModel("group:col.border_locked_active", "0x66ff5500", HyprlandTypes.GRADIENT),
    GroupModel("group:col.border_locked_inactive", "0x66775500", HyprlandTypes.GRADIENT),
    GroupModel("group:groupbar:enabled", "1", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:font_family", "", HyprlandTypes.STR),
    GroupModel("group:groupbar:font_weight_active", "normal", HyprlandTypes.FONT_WEIGHT),
    GroupModel("group:groupbar:font_weight_inactive", "normal", HyprlandTypes.FONT_WEIGHT),
    GroupModel("group:groupbar:font_size", "8", HyprlandTypes.INT),
    GroupModel("group:groupbar:gradients", "0", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:height", "14", HyprlandTypes.INT),
    GroupModel("group:groupbar:indicator_gap", "0", HyprlandTypes.INT),
    GroupModel("group:groupbar:indicator_height", "3", HyprlandTypes.INT),
    GroupModel("group:groupbar:priority", "3", HyprlandTypes.INT),
    GroupModel("group:groupbar:render_titles", "1", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:scrolling", "1", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:text_color", "0xffffffff", HyprlandTypes.COLOR),
    GroupModel("group:groupbar:stacked", "0", HyprlandTypes.INT),
    GroupModel("group:groupbar:rounding", "1", HyprlandTypes.INT),
    GroupModel("group:groupbar:gradient_rounding", "2", HyprlandTypes.INT),
    GroupModel("group:groupbar:round_only_edges", "1", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:gradient_round_only_edges", "1", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:gaps_out", "2", HyprlandTypes.INT),
    GroupModel("group:groupbar:gaps_in", "2", HyprlandTypes.INT),
    GroupModel("group:groupbar:keep_upper_gap", "1", HyprlandTypes.BOOL),
    GroupModel("group:groupbar:col.active", "0x66ffff00", HyprlandTypes.GRADIENT),
    GroupModel("group:groupbar:col.inactive", "0x66777700", HyprlandTypes.GRADIENT),
    GroupModel("group:groupbar:col.locked_active", "0x66ff5500", HyprlandTypes.GRADIENT),
    GroupModel("group:groupbar:col.locked_inactive", "0x66775500", HyprlandTypes.GRADIENT)
)
