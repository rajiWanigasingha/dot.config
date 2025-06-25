package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class LayerRulesModel(
    val rule: String,
    val value: String
)
