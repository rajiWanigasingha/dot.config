package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class DwindleModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultDwindleSettings = listOf(
    DwindleModel("dwindle:pseudotile", "0", HyprlandTypes.BOOL),
    DwindleModel("dwindle:force_split", "0", HyprlandTypes.INT),
    DwindleModel("dwindle:permanent_direction_override", "0", HyprlandTypes.BOOL),
    DwindleModel("dwindle:preserve_split", "0", HyprlandTypes.BOOL),
    DwindleModel("dwindle:special_scale_factor", "1.0", HyprlandTypes.FLOAT),
    DwindleModel("dwindle:split_width_multiplier", "1.0", HyprlandTypes.FLOAT),
    DwindleModel("dwindle:use_active_for_splits", "1", HyprlandTypes.BOOL),
    DwindleModel("dwindle:default_split_ratio", "1.0", HyprlandTypes.FLOAT),
    DwindleModel("dwindle:split_bias", "0", HyprlandTypes.INT),
    DwindleModel("dwindle:smart_split", "0", HyprlandTypes.BOOL),
    DwindleModel("dwindle:smart_resizing", "1", HyprlandTypes.BOOL)
)
