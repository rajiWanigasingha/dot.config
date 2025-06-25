package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.RenderModel
import org.hyprconfig.model.defaultRenderSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Render Settings :")

private val renderPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/render.conf"

internal fun handleRender(render: List<String>): List<RenderModel> {

    logger.info("Try to process and create hyprland render settings")

    val renderStore = defaultRenderSettings

    render.forEach {

        val processRender = it.split(":=").map { render -> render.trim() }

        val name = processRender.getOrNull(0) ?: return@forEach

        val value = processRender.getOrNull(1)?.split("=")?.map { render -> render.trim() } ?: return@forEach

        val renderName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val renderValue = value.getOrNull(1) ?: return@forEach

        renderStore.forEach innerLoop@{ render ->
            if (render.name == renderName) {
                val validateRenderValue = renderValue.hyprlandTypeCheck(render.type) ?: return@forEach

                if (validateRenderValue != render.value) render.value = validateRenderValue
            }
        }
    }

    logger.info("Creating hyprland render settings")

    handlePaths(renderPath)

    val renderSettings = generateRenderSettings(renderStore)

    Path.of(renderPath).writeText(renderSettings)

    return renderStore.toList()
}

fun generateRenderSettings(render: List<RenderModel>): String {
    return """
        render {
            explicit_sync = ${render.find { it.name == "render:explicit_sync" }!!.value}
            explicit_sync_kms = ${render.find { it.name == "render:explicit_sync_kms" }!!.value}
            direct_scanout = ${render.find { it.name == "render:direct_scanout" }!!.value}
            expand_undersized_textures = ${render.find { it.name == "render:expand_undersized_textures" }!!.value}
            xp_mode = ${render.find { it.name == "render:xp_mode" }!!.value}
            ctm_animation = ${render.find { it.name == "render:ctm_animation" }!!.value}
            cm_fs_passthrough = ${render.find { it.name == "render:cm_fs_passthrough" }!!.value}
            cm_enabled = ${render.find { it.name == "render:cm_enabled" }!!.value}
            send_content_type = ${render.find { it.name == "render:send_content_type" }!!.value}
        }
    """.insertVariables().trimIndent()
}
