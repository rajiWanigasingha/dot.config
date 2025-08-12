package model.tables

import kotlinx.serialization.Serializable

@Serializable
data class PermissionModel(
    val regex: String,
    val permission: String,
    val mode: String
)
