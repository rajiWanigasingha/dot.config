package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class ExecuteModel(
    val keyword: String,
    val command: String
)