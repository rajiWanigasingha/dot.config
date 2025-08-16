package model

import kotlinx.serialization.Serializable

@Serializable
data class ParsedModels(
    val name: String,
    val success: Boolean,
    val description: String,
    val found: Int
)