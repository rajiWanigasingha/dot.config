import java.nio.file.Path

internal object ProjectPaths {
    val userName: String? = System.getProperty("user.home")

    val defaultHyprlandPaths = "${userName}/.config/dot.config.hyprland"

    val folder = listOf(
        "/path",
        "/default",
        "/backups",
        "/store"
    )

    val ruleFolders = listOf(
        "dynamicRules",
        "staticRules",
        "props"
    )

    const val STATIC_FILES = "/static"

    val hyprland_config_file = "${userName}/.config/hypr/hyprland.conf"

    val defaultHyprlandGen = "${userName}/.config/hypr/dot.config.hyprland"

    val defaultHyprlandKeywordPaths = "$defaultHyprlandPaths/default/hyprlandDefault/"

    val defaultHyprlandStore = "$defaultHyprlandPaths/store/"

    val keybindDispatchers = "${defaultHyprlandPaths}/default/helpers/keybind.csv"

    val keybindFlags = "${defaultHyprlandPaths}/default/helpers/flags.csv"

    val rules = "${defaultHyprlandPaths}/default/helpers"

    val hyprFolder = listOf(
        "animation",
        "animations",
        "autoStart",
        "bezier",
        "bind",
        "binds",
        "cursor",
        "debug",
        "decoration",
        "device",
        "dwindle",
        "ecosystem",
        "env",
        "execute",
        "experimental",
        "general",
        "gestures",
        "group",
        "inputs",
        "layer",
        "master",
        "misc",
        "monitor",
        "openGL",
        "permission",
        "render",
        "submap",
        "unbind",
        "variable",
        "windowRules",
        "workspace",
        "xwayland"
    )

    fun String.toPath(): Path {
        return Path.of(this)
    }

    val storeFolder = listOf(
        "general",
        "generalSnap",
        "decoration",
        "decorationBlur",
        "decorationShadow",
        "animations",
        "inputs",
        "inputsTouchpad",
        "inputsTouchDevice",
        "inputsTablet",
        "cursor",
        "gestures",
        "group",
        "groupBar",
        "misc",
        "binds",
        "xwayland",
        "openGl",
        "render",
        "ecosystem",
        "dwindle",
        "master",
        "debug",
        "experimental",
        "variable",
        "keybind",
        "monitor",
        "autoStart",
        "windowRules",
        "workspace",
        "env",
        "layer",
        "unbind",
        "submap",
        "permission",
        "bezier",
        "animation",
        "device"
    )
}