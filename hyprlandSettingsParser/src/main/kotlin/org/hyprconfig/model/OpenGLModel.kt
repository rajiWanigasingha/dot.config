package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class OpenGLModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultOpenGLSettings = listOf(
    OpenGLModel(name = "opengl:nvidia_anti_flicker", value = "true", type = HyprlandTypes.BOOL)
)