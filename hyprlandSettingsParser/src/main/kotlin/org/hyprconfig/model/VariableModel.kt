package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class VariableModel(
    val name: String,
    val value: String
)