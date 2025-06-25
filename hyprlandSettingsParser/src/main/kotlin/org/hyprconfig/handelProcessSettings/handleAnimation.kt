package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.AnimationModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Animation Settings :")

private val animationPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/animation.conf"

/**
 * These uses to process and create animation settings of hyprland
 *
 * @param animation as list of strings
 * @return list of [AnimationModel]
 */
internal fun handleAnimation(animation: List<String>): List<AnimationModel> {

    logger.info("Try to process and create hyprland animation settings")

    val animationStore = mutableListOf<AnimationModel>()

    animation.forEach {

        val processAnimation =
            it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { animation -> animation.trim() }
                ?: return@forEach

        val name = processAnimation.getOrNull(0) ?: return@forEach

        val active = processAnimation.getOrNull(1)?.animationActiveValidate() ?: return@forEach

        val speed = processAnimation.getOrNull(2)?.validateSpeed()

        val bezier = processAnimation.getOrNull(3)

        val animations = processAnimation.getOrNull(4)

        animationStore.add(AnimationModel(name, active, speed, bezier, animations))
    }

    logger.info("Creating animation Settings in hyprland")

    handlePaths(animationPath)

    val animationSettings = mutableListOf<String>()

    animationStore.forEach {
        animationSettings.add("animation = ${it.name}, ${it.active}${if (it.speed != null) ", ${it.speed}" else ""}${if (it.bezier != null) ", ${it.bezier}" else ""}${if (it.animation != null) ", ${it.animation}" else ""}".insertVariables())
    }

    Path.of(animationPath).writeText(animationSettings.joinToString("\n"))

    return animationStore.toList()
}

private fun String.animationActiveValidate(): String? {
    return when (this) {
        "1" -> this
        "0" -> this
        "yes" -> "1"
        "no" -> "0"
        "true" -> "1"
        "false" -> "0"
        else -> null
    }
}

private fun String.validateSpeed(): String {
    val speed = this.toIntOrNull()

    if (speed == null) {
        return "10"
    } else if (0 >= speed) {
        return "1"
    } else {
        return this
    }
}