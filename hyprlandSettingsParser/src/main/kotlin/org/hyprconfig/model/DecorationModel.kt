package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class DecorationModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultDecorationSettings = listOf(
    DecorationModel("decoration:rounding", "0", HyprlandTypes.INT),
    DecorationModel("decoration:rounding_power", "2.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:enabled", "1", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:size", "8", HyprlandTypes.INT),
    DecorationModel("decoration:blur:passes", "1", HyprlandTypes.INT),
    DecorationModel("decoration:blur:ignore_opacity", "1", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:new_optimizations", "1", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:xray", "0", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:contrast", "0.8916", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:brightness", "1.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:vibrancy", "0.1696", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:vibrancy_darkness", "0.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:noise", "0.0117", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:special", "0", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:popups", "0", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:popups_ignorealpha", "0.2", HyprlandTypes.FLOAT),
    DecorationModel("decoration:blur:input_methods", "0", HyprlandTypes.BOOL),
    DecorationModel("decoration:blur:input_methods_ignorealpha", "0.2", HyprlandTypes.FLOAT),
    DecorationModel("decoration:active_opacity", "1.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:inactive_opacity", "1.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:fullscreen_opacity", "1.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:shadow:enabled", "1", HyprlandTypes.BOOL),
    DecorationModel("decoration:shadow:range", "4", HyprlandTypes.INT),
    DecorationModel("decoration:shadow:render_power", "3", HyprlandTypes.INT),
    DecorationModel("decoration:shadow:ignore_window", "1", HyprlandTypes.BOOL),
    DecorationModel("decoration:shadow:offset", "0 0", HyprlandTypes.VEC2),
    DecorationModel("decoration:shadow:scale", "1.0", HyprlandTypes.FLOAT),
    DecorationModel("decoration:shadow:sharp", "0", HyprlandTypes.BOOL),
    DecorationModel("decoration:shadow:color", "0xee1a1a1a", HyprlandTypes.COLOR),
    DecorationModel("decoration:shadow:color_inactive", "rgba(229, 229, 229, 0.93)", HyprlandTypes.COLOR),
    DecorationModel("decoration:dim_inactive", "0", HyprlandTypes.BOOL),
    DecorationModel("decoration:dim_strength", "0.5", HyprlandTypes.FLOAT),
    DecorationModel("decoration:dim_special", "0.2", HyprlandTypes.FLOAT),
    DecorationModel("decoration:dim_around", "0.4", HyprlandTypes.FLOAT),
    DecorationModel("decoration:screen_shader", "", HyprlandTypes.STR),
    DecorationModel("decoration:border_part_of_window", "1", HyprlandTypes.BOOL)
)
