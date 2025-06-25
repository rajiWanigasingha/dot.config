package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.OpenGLModel
import org.hyprconfig.model.defaultOpenGLSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel openGL Settings :")

private val openGLPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/openGL.conf"

internal fun handleOpenGl(openGL: List<String>): List<OpenGLModel> {

    logger.info("Try to process and create openGL hyprland settings")

    val openGLStore = defaultOpenGLSettings

    openGL.forEach {

        val processOpenGL = it.split(":=").map { openGL -> openGL.trim() }

        val name = processOpenGL.getOrNull(0) ?: return@forEach

        val value = processOpenGL.getOrNull(1)?.split("=")?.map { openGL -> openGL.trim() } ?: return@forEach

        val openGLName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val openGLValue = value.getOrNull(1) ?: return@forEach

        openGLStore.forEach { openGL ->
            if (openGL.name == openGLName) {
                val validateOpenGLValue = openGLValue.hyprlandTypeCheck(openGL.type) ?: return@forEach

                if (validateOpenGLValue != openGL.value) openGL.value = validateOpenGLValue
            }
        }
    }

    logger.info("Creating OpenGL hyprland settings")

    handlePaths(openGLPath)

    val openGLSettings = generateOpenGLSettings(openGLStore)

    Path.of(openGLPath).writeText(openGLSettings)

    return openGLStore.toList()
}


private fun generateOpenGLSettings(openGL: List<OpenGLModel>): String {
    return """
        opengl {
            nvidia_anti_flicker = ${openGL.find { it.name == "opengl:nvidia_anti_flicker" }!!.value}
        }
    """.insertVariables().trimIndent()
}