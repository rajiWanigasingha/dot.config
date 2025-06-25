package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class UnbindModel(
    val mods: List<String>,
    val key: List<String>
)
