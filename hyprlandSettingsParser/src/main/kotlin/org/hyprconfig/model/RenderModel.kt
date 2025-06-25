package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class RenderModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultRenderSettings = listOf(
    RenderModel("render:explicit_sync", "2", HyprlandTypes.INT),
    RenderModel("render:explicit_sync_kms", "2", HyprlandTypes.INT),
    RenderModel("render:direct_scanout", "0", HyprlandTypes.INT),
    RenderModel("render:expand_undersized_textures", "1", HyprlandTypes.BOOL),
    RenderModel("render:xp_mode", "0", HyprlandTypes.BOOL),
    RenderModel("render:ctm_animation", "2", HyprlandTypes.INT),
    RenderModel("render:cm_fs_passthrough", "2", HyprlandTypes.INT),
    RenderModel("render:cm_enabled", "1", HyprlandTypes.BOOL),
    RenderModel("render:send_content_type", "1", HyprlandTypes.BOOL)
)
