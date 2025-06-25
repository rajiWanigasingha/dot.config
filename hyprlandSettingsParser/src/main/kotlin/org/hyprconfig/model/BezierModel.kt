package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class BezierModel(
    val name: String,
    val x0: String,
    val y0: String,
    val x1: String,
    val y1: String
)
