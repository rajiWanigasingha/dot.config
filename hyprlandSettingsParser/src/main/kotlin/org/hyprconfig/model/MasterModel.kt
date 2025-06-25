package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class MasterModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultMasterSettings = listOf(
    MasterModel("master:special_scale_factor", "1.0", HyprlandTypes.FLOAT),
    MasterModel("master:mfact", "0.55", HyprlandTypes.FLOAT),
    MasterModel("master:new_status", "slave", HyprlandTypes.STR),
    MasterModel("master:slave_count_for_center_master", "2", HyprlandTypes.INT),
    MasterModel("master:center_master_fallback", "left", HyprlandTypes.STR),
    MasterModel("master:center_ignores_reserved", "0", HyprlandTypes.INT),
    MasterModel("master:new_on_active", "none", HyprlandTypes.STR),
    MasterModel("master:new_on_top", "0", HyprlandTypes.BOOL),
    MasterModel("master:orientation", "left", HyprlandTypes.STR),
    MasterModel("master:inherit_fullscreen", "1", HyprlandTypes.BOOL),
    MasterModel("master:allow_small_split", "0", HyprlandTypes.BOOL),
    MasterModel("master:smart_resizing", "1", HyprlandTypes.BOOL),
    MasterModel("master:drop_at_cursor", "1", HyprlandTypes.BOOL),
    MasterModel("master:always_keep_position", "0", HyprlandTypes.BOOL)
)
