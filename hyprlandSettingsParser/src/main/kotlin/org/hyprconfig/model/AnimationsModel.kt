package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class AnimationsModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultAnimationsSettings = listOf(
    AnimationsModel("animations:enabled", "true", HyprlandTypes.BOOL),
    AnimationsModel("animations:first_launch_animation", "true", HyprlandTypes.BOOL),
    AnimationsModel("animations:workspace_wraparound", "false", HyprlandTypes.BOOL)
)
