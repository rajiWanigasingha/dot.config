package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class XwaylandModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultXwaylandSettings = listOf(
    XwaylandModel("xwayland:enabled", "1", HyprlandTypes.BOOL),
    XwaylandModel("xwayland:use_nearest_neighbor", "1", HyprlandTypes.BOOL),
    XwaylandModel("xwayland:force_zero_scaling", "0", HyprlandTypes.BOOL),
    XwaylandModel("xwayland:create_abstract_socket", "0", HyprlandTypes.BOOL),
)
