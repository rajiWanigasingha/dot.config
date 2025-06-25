package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class PermissionModel(
    val regex: String,
    val permission: String,
    val mode: String
)
