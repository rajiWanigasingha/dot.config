package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimationModel(
    val name: String,
    val active: String,
    val speed: String?,
    val bezier: String?,
    val animation: String?
)