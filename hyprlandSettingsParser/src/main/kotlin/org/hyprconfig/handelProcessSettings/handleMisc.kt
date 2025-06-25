package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.MiscModel
import org.hyprconfig.model.defaultMiscValues
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Misc Settings :")

private val miscPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/misc.conf"

/**
 * Use to process and create misc settings for hyprland
 *
 * @param misc list of string
 * @return list of [MiscModel]
 */
internal fun handleMisc(misc: List<String>): List<MiscModel> {

    logger.info("Try to process and create hyprland misc settings")

    val miscStore = defaultMiscValues

    misc.forEach {

        val processMisc = it.split(":=").map { misc -> misc.trim() }

        val name = processMisc.getOrNull(0) ?: return@forEach

        val value = processMisc.getOrNull(1)?.split("=")?.map { misc -> misc.trim() } ?: return@forEach

        val miscName = "$name:${value.getOrNull(0) ?: return@forEach}".trim()

        val miscValue = value.getOrNull(1)?.trim() ?: return@forEach

        miscStore.forEach innerLoop@{ misc ->
            if (misc.name == miscName) {
                val validateMiscValue = miscValue.hyprlandTypeCheck(misc.type) ?: return@forEach

                if (validateMiscValue != miscValue) misc.value = validateMiscValue
            }
        }
    }

    logger.info("Creating misc hyprland file")

    handlePaths(miscPath)

    val miscSettings = generateMiscSettings(miscStore)

    Path.of(miscPath).writeText(miscSettings)

    return miscStore.toList()
}

private fun generateMiscSettings(misc: List<MiscModel>): String {
    return """
    misc {
        disable_hyprland_logo = ${misc.find { it.name == "misc:disable_hyprland_logo" }!!.value}
        disable_splash_rendering = ${misc.find { it.name == "misc:disable_splash_rendering" }!!.value}
        col.splash = ${misc.find { it.name == "misc:col.splash" }!!.value}
        splash_font_family = ${misc.find { it.name == "misc:splash_font_family" }!!.value}
        font_family = ${misc.find { it.name == "misc:font_family" }!!.value}
        force_default_wallpaper = ${misc.find { it.name == "misc:force_default_wallpaper" }!!.value}
        vfr = ${misc.find { it.name == "misc:vfr" }!!.value}
        vrr = ${misc.find { it.name == "misc:vrr" }!!.value}
        mouse_move_enables_dpms = ${misc.find { it.name == "misc:mouse_move_enables_dpms" }!!.value}
        key_press_enables_dpms = ${misc.find { it.name == "misc:key_press_enables_dpms" }!!.value}
        always_follow_on_dnd = ${misc.find { it.name == "misc:always_follow_on_dnd" }!!.value}
        layers_hog_keyboard_focus = ${misc.find { it.name == "misc:layers_hog_keyboard_focus" }!!.value}
        animate_manual_resizes = ${misc.find { it.name == "misc:animate_manual_resizes" }!!.value}
        animate_mouse_windowdragging = ${misc.find { it.name == "misc:animate_mouse_windowdragging" }!!.value}
        disable_autoreload = ${misc.find { it.name == "misc:disable_autoreload" }!!.value}
        enable_swallow = ${misc.find { it.name == "misc:enable_swallow" }!!.value}
        swallow_regex = ${misc.find { it.name == "misc:swallow_regex" }!!.value}
        swallow_exception_regex = ${misc.find { it.name == "misc:swallow_exception_regex" }!!.value}
        focus_on_activate = ${misc.find { it.name == "misc:focus_on_activate" }!!.value}
        mouse_move_focuses_monitor = ${misc.find { it.name == "misc:mouse_move_focuses_monitor" }!!.value}
        render_ahead_of_time = ${misc.find { it.name == "misc:render_ahead_of_time" }!!.value}
        render_ahead_safezone = ${misc.find { it.name == "misc:render_ahead_safezone" }!!.value}
        allow_session_lock_restore = ${misc.find { it.name == "misc:allow_session_lock_restore" }!!.value}
        close_special_on_empty = ${misc.find { it.name == "misc:close_special_on_empty" }!!.value}
        background_color = ${misc.find { it.name == "misc:background_color" }!!.value}
        new_window_takes_over_fullscreen = ${misc.find { it.name == "misc:new_window_takes_over_fullscreen" }!!.value}
        exit_window_retains_fullscreen = ${misc.find { it.name == "misc:exit_window_retains_fullscreen" }!!.value}
        initial_workspace_tracking = ${misc.find { it.name == "misc:initial_workspace_tracking" }!!.value}
        middle_click_paste = ${misc.find { it.name == "misc:middle_click_paste" }!!.value}
        render_unfocused_fps = ${misc.find { it.name == "misc:render_unfocused_fps" }!!.value}
        disable_xdg_env_checks = ${misc.find { it.name == "misc:disable_xdg_env_checks" }!!.value}
        disable_hyprland_qtutils_check = ${misc.find { it.name == "misc:disable_hyprland_qtutils_check" }!!.value}
        lockdead_screen_delay = ${misc.find { it.name == "misc:lockdead_screen_delay" }!!.value}
        enable_anr_dialog = ${misc.find { it.name == "misc:enable_anr_dialog" }!!.value}
        anr_missed_pings = ${misc.find { it.name == "misc:anr_missed_pings" }!!.value}
    }""".insertVariables().trimIndent()
}