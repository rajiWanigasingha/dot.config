package org.hyprconfig.model

import org.hyprconfig.helpers.HyprlandTypes
import kotlinx.serialization.Serializable

@Serializable
data class MiscModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

val defaultMiscValues = listOf(
    MiscModel("misc:disable_hyprland_logo", "0", HyprlandTypes.BOOL),
    MiscModel("misc:disable_splash_rendering", "0", HyprlandTypes.BOOL),
    MiscModel("misc:col.splash", "0x55ffffff", HyprlandTypes.COLOR),
    MiscModel("misc:splash_font_family", "", HyprlandTypes.STR),
    MiscModel("misc:font_family", "Sans", HyprlandTypes.STR),
    MiscModel("misc:force_default_wallpaper", "-1", HyprlandTypes.INT),
    MiscModel("misc:vfr", "1", HyprlandTypes.BOOL),
    MiscModel("misc:vrr", "0", HyprlandTypes.INT),
    MiscModel("misc:mouse_move_enables_dpms", "0", HyprlandTypes.BOOL),
    MiscModel("misc:key_press_enables_dpms", "0", HyprlandTypes.BOOL),
    MiscModel("misc:always_follow_on_dnd", "1", HyprlandTypes.BOOL),
    MiscModel("misc:layers_hog_keyboard_focus", "1", HyprlandTypes.BOOL),
    MiscModel("misc:animate_manual_resizes", "0", HyprlandTypes.BOOL),
    MiscModel("misc:animate_mouse_windowdragging", "0", HyprlandTypes.BOOL),
    MiscModel("misc:disable_autoreload", "0", HyprlandTypes.BOOL),
    MiscModel("misc:enable_swallow", "0", HyprlandTypes.BOOL),
    MiscModel("misc:swallow_regex", "", HyprlandTypes.STR),
    MiscModel("misc:swallow_exception_regex", "", HyprlandTypes.STR),
    MiscModel("misc:focus_on_activate", "0", HyprlandTypes.BOOL),
    MiscModel("misc:mouse_move_focuses_monitor", "1", HyprlandTypes.BOOL),
    MiscModel("misc:render_ahead_of_time", "0", HyprlandTypes.BOOL),
    MiscModel("misc:render_ahead_safezone", "1", HyprlandTypes.INT),
    MiscModel("misc:allow_session_lock_restore", "0", HyprlandTypes.BOOL),
    MiscModel("misc:close_special_on_empty", "1", HyprlandTypes.BOOL),
    MiscModel("misc:background_color", "0xff111111", HyprlandTypes.COLOR),
    MiscModel("misc:new_window_takes_over_fullscreen", "0", HyprlandTypes.INT),
    MiscModel("misc:exit_window_retains_fullscreen", "0", HyprlandTypes.INT),
    MiscModel("misc:initial_workspace_tracking", "1", HyprlandTypes.INT),
    MiscModel("misc:middle_click_paste", "1", HyprlandTypes.BOOL),
    MiscModel("misc:render_unfocused_fps", "15", HyprlandTypes.INT),
    MiscModel("misc:disable_xdg_env_checks", "0", HyprlandTypes.BOOL),
    MiscModel("misc:disable_hyprland_qtutils_check", "0", HyprlandTypes.BOOL),
    MiscModel("misc:lockdead_screen_delay", "1000", HyprlandTypes.INT),
    MiscModel("misc:enable_anr_dialog", "1", HyprlandTypes.BOOL),
    MiscModel("misc:anr_missed_pings", "1", HyprlandTypes.INT)
)

