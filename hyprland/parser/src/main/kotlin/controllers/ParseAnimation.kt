package controllers

import logger
import model.tables.AnimationModel
import write.animation.WriteAnimation

/**
 * These uses to process and create animation settings of hyprland
 *
 * @param animation as list of strings
 * @return list of [AnimationModel]
 */
internal fun parseAnimation(animation: List<String>): Result<List<AnimationModel>> {

    logger.info("Try to process and create hyprland animation settings")

    val animationStore = mutableListOf<AnimationModel>()

    animation.forEach {

        if (!it.startsWith("animation")) return@forEach

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

    val write = WriteAnimation()

    write.writeIntoHyprland(animationStore).getOrThrow()
    write.writeIntoDotConfig(animationStore).getOrThrow()

    return Result.success(animationStore.toList())
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