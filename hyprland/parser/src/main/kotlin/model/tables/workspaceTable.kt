package model.tables

import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceModel(
    val name: String,
    val rules: List<String>
)