package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.AnimationsModel
import org.hyprconfig.model.defaultAnimationsSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Animations Settings :")

private val animationsPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/animations.conf"

internal fun handleAnimations(animations: List<String>): List<AnimationsModel> {

    logger.info("Try and process and create animations hyprland settings")

    val animationsStore = defaultAnimationsSettings

    animations.forEach {

        val processAnimations = it.split(":=").map { animation -> animation.trim() }

        val name = processAnimations.getOrNull(0) ?: return@forEach

        val value = processAnimations.getOrNull(1)?.split("=")?.map { animation -> animation.trim() } ?: return@forEach

        val animationsName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val animationsValue = value.getOrNull(1) ?: return@forEach

        animationsStore.forEach innerLoop@{ animation ->
            if (animation.name == animationsName) {

                val validateAnimationValue = animationsValue.hyprlandTypeCheck(animation.type) ?: return@forEach

                if (validateAnimationValue != animation.value) animation.value = validateAnimationValue
            }
        }
    }

    logger.info("Creating animations hyprland settings")

    handlePaths(animationsPath)

    val animationsSettings = generateAnimationsSettings(animationsStore)

    Path.of(animationsPath).writeText(animationsSettings)

    return animationsStore.toList()
}

private fun generateAnimationsSettings(animations: List<AnimationsModel>): String {
    return """
        animations {
            enabled = ${animations.find { it.name == "animations:enabled" }!!.value}
            first_launch_animation = ${animations.find { it.name == "animations:first_launch_animation" }!!.value}
            workspace_wraparound = ${animations.find { it.name == "animations:workspace_wraparound" }!!.value}
        }
    """.insertVariables().trimIndent()
}