package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class GesturesModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultGesturesSettings = listOf(
    GesturesModel("gestures:workspace_swipe", "0", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_fingers", "3", HyprlandTypes.INT),
    GesturesModel("gestures:workspace_swipe_min_fingers", "0", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_distance", "300", HyprlandTypes.INT),
    GesturesModel("gestures:workspace_swipe_invert", "1", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_min_speed_to_force", "30", HyprlandTypes.INT),
    GesturesModel("gestures:workspace_swipe_cancel_ratio", "0.5", HyprlandTypes.FLOAT),
    GesturesModel("gestures:workspace_swipe_create_new", "1", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_direction_lock", "1", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_direction_lock_threshold", "10", HyprlandTypes.INT),
    GesturesModel("gestures:workspace_swipe_forever", "0", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_use_r", "0", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_touch", "0", HyprlandTypes.BOOL),
    GesturesModel("gestures:workspace_swipe_touch_invert", "0", HyprlandTypes.BOOL),
)
