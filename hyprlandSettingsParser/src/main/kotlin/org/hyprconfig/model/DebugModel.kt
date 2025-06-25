package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class DebugModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultDebugSettings = listOf(
    DebugModel("debug:overlay", "0", HyprlandTypes.BOOL),
    DebugModel("debug:damage_blink", "0", HyprlandTypes.BOOL),
    DebugModel("debug:pass", "0", HyprlandTypes.BOOL),
    DebugModel("debug:disable_logs", "1", HyprlandTypes.BOOL),
    DebugModel("debug:disable_time", "1", HyprlandTypes.BOOL),
    DebugModel("debug:enable_stdout_logs", "0", HyprlandTypes.BOOL),
    DebugModel("debug:damage_tracking", "2", HyprlandTypes.INT),
    DebugModel("debug:manual_crash", "0", HyprlandTypes.INT),
    DebugModel("debug:suppress_errors", "0", HyprlandTypes.BOOL),
    DebugModel("debug:error_limit", "5", HyprlandTypes.INT),
    DebugModel("debug:error_position", "0", HyprlandTypes.INT),
    DebugModel("debug:disable_scale_checks", "0", HyprlandTypes.BOOL),
    DebugModel("debug:colored_stdout_logs", "1", HyprlandTypes.BOOL),
    DebugModel("debug:full_cm_proto", "0", HyprlandTypes.BOOL)
)
