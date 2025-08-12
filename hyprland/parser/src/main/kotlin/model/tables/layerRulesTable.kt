package model.tables

import kotlinx.serialization.Serializable

@Serializable
data class LayerRulesModel(
    val rule: String,
    val value: String
)
