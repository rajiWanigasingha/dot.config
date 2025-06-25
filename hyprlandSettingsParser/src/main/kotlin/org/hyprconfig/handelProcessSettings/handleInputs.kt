package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.InputsModel
import org.hyprconfig.model.defaultInputSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Inputs Settings :")

private val inputsPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/inputs.conf"

internal fun handleInputs(inputs: List<String>): List<InputsModel> {

    logger.info("Try to process and create inputs hyprland settings.")

    val inputsStore = defaultInputSettings

    inputs.forEach {

        val processInputs = it.split(":=").map { input -> input.trim() }

        val name = processInputs.getOrNull(0) ?: return@forEach

        val value = processInputs.getOrNull(1)?.split("=")?.map { input -> input.trim() } ?: return@forEach

        val inputName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val inputValue = value.getOrNull(1) ?: return@forEach

        inputsStore.forEach innerLoop@ { input ->
            if (input.name == inputName) {

                val validateInputs = inputValue.hyprlandTypeCheck(input.type) ?: return@forEach

                if (validateInputs != input.value) input.value = validateInputs
            }
        }

    }

    logger.info("Creating inputs hyprland file")

    handlePaths(inputsPath)

    val inputsSettings = generateInputSettings(inputsStore)

    Path.of(inputsPath).writeText(inputsSettings)

    return inputsStore.toList()
}

fun generateInputSettings(settings: List<InputsModel>): String {
    return """
        input {
            follow_mouse = ${settings.find { it.name == "input:follow_mouse" }!!.value}
            follow_mouse_threshold = ${settings.find { it.name == "input:follow_mouse_threshold" }!!.value}
            focus_on_close = ${settings.find { it.name == "input:focus_on_close" }!!.value}
            mouse_refocus = ${settings.find { it.name == "input:mouse_refocus" }!!.value}
            special_fallthrough = ${settings.find { it.name == "input:special_fallthrough" }!!.value}
            off_window_axis_events = ${settings.find { it.name == "input:off_window_axis_events" }!!.value}
            sensitivity = ${settings.find { it.name == "input:sensitivity" }!!.value}
            accel_profile = ${settings.find { it.name == "input:accel_profile" }!!.value}
            kb_file = ${settings.find { it.name == "input:kb_file" }!!.value}
            kb_layout = ${settings.find { it.name == "input:kb_layout" }!!.value}
            kb_variant = ${settings.find { it.name == "input:kb_variant" }!!.value}
            kb_options = ${settings.find { it.name == "input:kb_options" }!!.value}
            kb_rules = ${settings.find { it.name == "input:kb_rules" }!!.value}
            kb_model = ${settings.find { it.name == "input:kb_model" }!!.value}
            repeat_rate = ${settings.find { it.name == "input:repeat_rate" }!!.value}
            repeat_delay = ${settings.find { it.name == "input:repeat_delay" }!!.value}
            natural_scroll = ${settings.find { it.name == "input:natural_scroll" }!!.value}
            numlock_by_default = ${settings.find { it.name == "input:numlock_by_default" }!!.value}
            resolve_binds_by_sym = ${settings.find { it.name == "input:resolve_binds_by_sym" }!!.value}
            force_no_accel = ${settings.find { it.name == "input:force_no_accel" }!!.value}
            float_switch_override_focus = ${settings.find { it.name == "input:float_switch_override_focus" }!!.value}
            left_handed = ${settings.find { it.name == "input:left_handed" }!!.value}
            scroll_method = ${settings.find { it.name == "input:scroll_method" }!!.value}
            scroll_button = ${settings.find { it.name == "input:scroll_button" }!!.value}
            scroll_button_lock = ${settings.find { it.name == "input:scroll_button_lock" }!!.value}
            scroll_factor = ${settings.find { it.name == "input:scroll_factor" }!!.value}
            scroll_points = ${settings.find { it.name == "input:scroll_points" }!!.value}
            emulate_discrete_scroll = ${settings.find { it.name == "input:emulate_discrete_scroll" }!!.value}

            touchpad {
                natural_scroll = ${settings.find { it.name == "input:touchpad:natural_scroll" }!!.value}
                disable_while_typing = ${settings.find { it.name == "input:touchpad:disable_while_typing" }!!.value}
                clickfinger_behavior = ${settings.find { it.name == "input:touchpad:clickfinger_behavior" }!!.value}
                tap_button_map = ${settings.find { it.name == "input:touchpad:tap_button_map" }!!.value}
                middle_button_emulation = ${settings.find { it.name == "input:touchpad:middle_button_emulation" }!!.value}
                tap-to-click = ${settings.find { it.name == "input:touchpad:tap-to-click" }!!.value}
                tap-and-drag = ${settings.find { it.name == "input:touchpad:tap-and-drag" }!!.value}
                drag_lock = ${settings.find { it.name == "input:touchpad:drag_lock" }!!.value}
                scroll_factor = ${settings.find { it.name == "input:touchpad:scroll_factor" }!!.value}
                flip_x = ${settings.find { it.name == "input:touchpad:flip_x" }!!.value}
                flip_y = ${settings.find { it.name == "input:touchpad:flip_y" }!!.value}
            }

            touchdevice {
                transform = ${settings.find { it.name == "input:touchdevice:transform" }!!.value}
                output = ${settings.find { it.name == "input:touchdevice:output" }!!.value}
                enabled = ${settings.find { it.name == "input:touchdevice:enabled" }!!.value}
            }

            tablet {
                transform = ${settings.find { it.name == "input:tablet:transform" }!!.value}
                output = ${settings.find { it.name == "input:tablet:output" }!!.value}
                region_position = ${settings.find { it.name == "input:tablet:region_position" }!!.value}
                absolute_region_position = ${settings.find { it.name == "input:tablet:absolute_region_position" }!!.value}
                region_size = ${settings.find { it.name == "input:tablet:region_size" }!!.value}
                relative_input = ${settings.find { it.name == "input:tablet:relative_input" }!!.value}
                left_handed = ${settings.find { it.name == "input:tablet:left_handed" }!!.value}
                active_area_position = ${settings.find { it.name == "input:tablet:active_area_position" }!!.value}
                active_area_size = ${settings.find { it.name == "input:tablet:active_area_size" }!!.value}
            }
        }
    """.insertVariables().trimIndent()
}
