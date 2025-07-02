package org.dot.config.model

import kotlinx.serialization.Serializable
import org.hyprconfig.helpers.HyprlandTypes

object Tables {

    @Serializable
    data class StandedHyprlandLangTable(
        val category: String,
        val name: String,
        val settingsName: String,
        val description: String,
        val typeOfHyprland: HyprlandTypes,
        val value: Helpers.HyprValue
    )

    @Serializable
    data class StandedHyprlandParsedSettings(
        val name: String,
        val type: HyprlandTypes,
        var value: String,
    )

    @Serializable
    data class KeybindTable(
        val flags: List<Char>,
        val mod: List<String>,
        val keys: List<String>,
        val description: String?,
        val dispatcher: String,
        val args: String?,
    ) {
        init {

            val modifierKeys = listOf(
                "SHIFT",
                "CAPS",
                "CTRL",
                "CONTROL",
                "ALT",
                "MOD2",
                "MOD3",
                "SUPER",
                "WIN",
                "LOGO",
                "MOD4",
                "MOD5",
                ""
            )

            val hyprlandDispatchers = listOf(
                "exec",
                "execr",
                "pass",
                "sendshortcut",
                "sendkeystate",
                "killactive",
                "forcekillactive",
                "closewindow",
                "killwindow",
                "signal",
                "signalwindow",
                "workspace",
                "movetoworkspace",
                "movetoworkspacesilent",
                "togglefloating",
                "setfloating",
                "settiled",
                "fullscreen",
                "fullscreenstate",
                "dpms",
                "pin",
                "movefocus",
                "movewindow",
                "resizewindow",
                "swapwindow",
                "centerwindow",
                "resizeactive",
                "moveactive",
                "resizewindowpixel",
                "movewindowpixel",
                "cyclenext",
                "swapnext",
                "tagwindow",
                "focuswindow",
                "focusmonitor",
                "splitratio",
                "movecursortocorner",
                "movecursor",
                "renameworkspace",
                "exit",
                "forcerendererreload",
                "movecurrentworkspacetomonitor",
                "focusworkspaceoncurrentmonitor",
                "moveworkspacetomonitor",
                "swapactiveworkspaces",
                "bringactivetotop",
                "alterzorder",
                "togglespecialworkspace",
                "focusurgentorlast",
                "togglegroup",
                "changegroupactive",
                "focuscurrentorlast",
                "lockgroups",
                "lockactivegroup",
                "moveintogroup",
                "moveoutofgroup",
                "movewindoworgroup",
                "movegroupwindow",
                "denywindowfromgroup",
                "setignoregrouplock",
                "global",
                "submap",
                "event",
                "setprop",
                "toggleswallow",
                "togglesplit",
                "swapsplit"
            )

            val hyprlandFlags = listOf('l', 'r', 'e', 'm', 'n', 't', 'i', 's', 'o', 'd', 'p', 'c', 'g')

            require(modifierKeys.containsAll(mod)) { "Invalid Mod For Keybind" }
            require(hyprlandDispatchers.contains(dispatcher)) { "Invalid Dispatcher For Keybind" }
            require(hyprlandFlags.containsAll(flags)) { "Invalid Flags For Keybind" }
        }
    }

    @Serializable
    data class AnimationTable(
        val name: String,
        val onOff: Int,
        val speed: String?,
        val curve: String?,
        val style: String?
    ) {
        init {
            require(onOff == 0 || onOff == 1) { "Invalid Animation For Keybind" }
        }
    }

    @Serializable
    data class BezierTable(
        val name: String,
        val x0: String,
        val y0: String,
        val x1: String,
        val y1: String
    )

    @Serializable
    data class MonitorTable(
        val name: String,
        val disable: Boolean,
        val addreserved: List<Int>?,
        val resolution: String,
        val position: String,
        val scale: String,
        val mirror: String?,
        val bitDepth: Int?,
        val transform: Int?,
        val cm: String?,
        val sdrsaturation: Float?,
        val sdrbrightness: Float?,
        val vrr: Int?
    )

    @Serializable
    data class WindowRules(
        val rules: String,
        val params: List<String>
    )

    @Serializable
    data class WorkspaceRules(
        val name: String,
        val rules: List<String>
    )

    @Serializable
    data class AutoStart(
        val keyword: String,
        val command: String
    ) {
        init {
            require(keyword == "exec" || keyword == "execr" || keyword == "exec-once" || keyword == "execr-once" || keyword == "exec-shutdown") { "Invalid Keyword In AutoStart" }
        }
    }

    @Serializable
    data class Env(
        val keyword: String,
        val value: String,
    ) {
        init {
            require(keyword == "env" || keyword == "envd")
        }
    }

    @Serializable
    data class LayerRules(
        val rule: String,
        val value: String
    )

    @Serializable
    data class ParsedTime(
        val path: String,
        val time: String
    )
}