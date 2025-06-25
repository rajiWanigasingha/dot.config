package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class ExperimentalModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultExperimentalSettings = listOf(
    ExperimentalModel("experimental:xx_color_management_v4", "0", HyprlandTypes.BOOL)
)
