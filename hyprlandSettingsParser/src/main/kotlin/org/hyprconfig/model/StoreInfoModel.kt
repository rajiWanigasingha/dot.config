package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreInfoModel(
    val path: String,
    val time: String
)
