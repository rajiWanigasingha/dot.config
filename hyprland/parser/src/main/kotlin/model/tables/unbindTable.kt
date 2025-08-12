package model.tables

import kotlinx.serialization.Serializable

@Serializable
data class UnbindModel(
    val mods: List<String>,
    val key: List<String>
)