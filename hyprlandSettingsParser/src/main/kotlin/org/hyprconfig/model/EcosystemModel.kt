package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class EcosystemModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultEcosystemSettings = listOf(
    EcosystemModel("ecosystem:no_update_news", "0", HyprlandTypes.BOOL),
    EcosystemModel("ecosystem:no_donation_nag", "0", HyprlandTypes.BOOL),
    EcosystemModel("ecosystem:enforce_permissions", "0", HyprlandTypes.BOOL)
)
