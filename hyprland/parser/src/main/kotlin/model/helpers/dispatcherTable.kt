package model.helpers

import kotlinx.serialization.Serializable

@Serializable
data class DispatcherTable(
    val name: String,
    val command: String,
    val description: String
)