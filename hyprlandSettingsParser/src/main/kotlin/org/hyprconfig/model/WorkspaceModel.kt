package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceModel(
    val name: String,
    val rules: List<String>
)