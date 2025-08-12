package model.tables

import kotlinx.serialization.Serializable

@Serializable
data class VariableModel(
    val name: String,
    val value: String
)