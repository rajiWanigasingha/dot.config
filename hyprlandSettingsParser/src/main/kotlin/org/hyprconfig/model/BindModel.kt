package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class BindModel(
    val flags: String,
    val mod: List<String>,
    val key: List<String>,
    val description: String?,
    val dispatcher: String,
    val args: String?
)
