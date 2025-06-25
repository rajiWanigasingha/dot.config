package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.GesturesModel
import org.hyprconfig.model.defaultGesturesSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Gestures Settings :")

private val gesturesPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/gestures.conf"

internal fun handleGestures(gestures: List<String>): List<GesturesModel> {

    logger.info("Try to process and create gestures hyprland settings")

    val gesturesStore = defaultGesturesSettings

    gestures.forEach {

        val processGestures = it.split(":=").map { gestures -> gestures.trim() }

        val name = processGestures.getOrNull(0) ?: return@forEach

        val value = processGestures.getOrNull(1)?.split("=")?.map { gestures -> gestures.trim() } ?: return@forEach

        val gestureName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val gesturesValues = value.getOrNull(1) ?: return@forEach

        gesturesStore.forEach innerLoop@{ gesture ->
            if (gesture.name == gestureName) {
                val validateGestureValue = gesturesValues.hyprlandTypeCheck(gesture.type) ?: return@forEach

                if (validateGestureValue != gesture.value) gesture.value = validateGestureValue
            }
        }
    }

    logger.info("Creating gesture hyprland settings")

    handlePaths(gesturesPath)

    val gesturesSettings = generateGestureSettings(gesturesStore)

    Path.of(gesturesPath).writeText(gesturesSettings)

    return gesturesStore.toList()
}

private fun generateGestureSettings(gestures: List<GesturesModel>): String {
    return """
        gestures {
            workspace_swipe = ${gestures.find { it.name == "gestures:workspace_swipe" }!!.value}
            workspace_swipe_fingers = ${gestures.find { it.name == "gestures:workspace_swipe_fingers" }!!.value}
            workspace_swipe_min_fingers = ${gestures.find { it.name == "gestures:workspace_swipe_min_fingers" }!!.value}
            workspace_swipe_distance = ${gestures.find { it.name == "gestures:workspace_swipe_distance" }!!.value}
            workspace_swipe_invert = ${gestures.find { it.name == "gestures:workspace_swipe_invert" }!!.value}
            workspace_swipe_min_speed_to_force = ${gestures.find { it.name == "gestures:workspace_swipe_min_speed_to_force" }!!.value}
            workspace_swipe_cancel_ratio = ${gestures.find { it.name == "gestures:workspace_swipe_cancel_ratio" }!!.value}
            workspace_swipe_create_new = ${gestures.find { it.name == "gestures:workspace_swipe_create_new" }!!.value}
            workspace_swipe_direction_lock = ${gestures.find { it.name == "gestures:workspace_swipe_direction_lock" }!!.value}
            workspace_swipe_direction_lock_threshold = ${gestures.find { it.name == "gestures:workspace_swipe_direction_lock_threshold" }!!.value}
            workspace_swipe_forever = ${gestures.find { it.name == "gestures:workspace_swipe_forever" }!!.value}
            workspace_swipe_use_r = ${gestures.find { it.name == "gestures:workspace_swipe_use_r" }!!.value}
            workspace_swipe_touch = ${gestures.find { it.name == "gestures:workspace_swipe_touch" }!!.value}
            workspace_swipe_touch_invert = ${gestures.find { it.name == "gestures:workspace_swipe_touch_invert" }!!.value}
        }
    """.insertVariables().trimIndent()
}