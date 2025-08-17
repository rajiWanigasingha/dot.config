package model.tables

import kotlinx.serialization.Serializable

@Serializable
data class HyprlandCreateTable(
    val name: String,
    val date: String,
)