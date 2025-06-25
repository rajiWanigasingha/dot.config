package org.hyprconfig.helpers

enum class HyprlandTypes {
    INT,
    BOOL,
    FLOAT,
    COLOR,
    VEC2,
    MOD,
    STR,
    GRADIENT,
    FONT_WEIGHT
}

internal fun String.hyprlandTypeCheck(type: HyprlandTypes): String? {

    val typeOfHyprland = this.trim()

    if (type == HyprlandTypes.INT) {
        return typeOfHyprland.toIntOrNull()?.toString()
    } else if (type == HyprlandTypes.BOOL) {

        return if (typeOfHyprland == "true" || typeOfHyprland == "false" || typeOfHyprland == "yes" || typeOfHyprland == "no" || typeOfHyprland == "on" || typeOfHyprland == "off" || typeOfHyprland == "0" || typeOfHyprland == "1") {
            this
        } else {
            null
        }

    } else if (type == HyprlandTypes.FLOAT) {
        return typeOfHyprland.toFloatOrNull()?.toString()
    } else if (type == HyprlandTypes.COLOR) {

        val rgbaRegex = Regex("""rgba\(\s*(?:[0-9a-fA-F]{8}|(?:\d{1,3}\s*,\s*){3}(?:\d(\.\d+)?|1(\.0+)?))\s*\)""")
        val rgbRegex = Regex("""rgb\(\s*(?:[0-9a-fA-F]{6}|(?:\d{1,3}\s*,\s*){2}\d{1,3})\s*\)""")
        val legacyRegex = Regex("""0x[0-9a-fA-F]{8}""")

        when {
            typeOfHyprland.matches(rgbaRegex) -> return this
            typeOfHyprland.matches(rgbRegex) -> return this
            typeOfHyprland.matches(legacyRegex) -> return this
        }

    } else if (type == HyprlandTypes.MOD) {

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
            "MOD5"
        )

        return if (modifierKeys.contains(typeOfHyprland)) this else null
    } else if (type == HyprlandTypes.VEC2) {

        val vec = typeOfHyprland.split(" ").map { it.trim() }

        val vac1 = vec.getOrNull(0)?.toFloatOrNull() ?: return null

        val vec2 = vec.getOrNull(1)?.toFloatOrNull() ?: return null
    } else if (type == HyprlandTypes.GRADIENT) {

        return this
    } else if (type == HyprlandTypes.FONT_WEIGHT) {

        if (typeOfHyprland.toIntOrNull() != null) {

            val intType = typeOfHyprland.toInt()

            if (intType in 100..1000) return this

            val fontWeights = listOf(
                "thin",
                "ultralight",
                "light",
                "semilight",
                "book",
                "normal",
                "medium",
                "semibold",
                "bold",
                "ultrabold",
                "heavy",
                "ultraheavy"
            )

            return if (fontWeights.contains(typeOfHyprland)) this else null

        }
    } else if (type == HyprlandTypes.STR) {

        return this
    }

    return null
}