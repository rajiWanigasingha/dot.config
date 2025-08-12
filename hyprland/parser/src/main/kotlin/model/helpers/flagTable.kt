package model.helpers

import kotlinx.serialization.Serializable

@Serializable
data class FlagTable(
    val name: String,
    val settingsName: String,
    val description: String,
    var have: Boolean
)