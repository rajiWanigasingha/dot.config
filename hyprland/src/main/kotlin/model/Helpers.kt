package org.dot.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyprconfig.helpers.HyprlandTypes

object Helpers {

    private val modifierKeys = listOf(
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

    data class TableCsv(
        val category: String,
        val displayName: String,
        val settingsName: String,
        val description: String,
        val type: String,
        val value: String,
        val actionLink: String,
        val tab: String,
        val method: String,
        val validate: String,
        val special: String
    )

    @Serializable
    sealed class HyprValue {
        @SerialName("STR")
        @Serializable
        data class StrVal(val value: String) : HyprValue()

        @SerialName("INT")
        @Serializable
        data class IntVal(val value: Int) : HyprValue()

        @SerialName("FLOAT")
        @Serializable
        data class FloatVal(val value: Float) : HyprValue()

        @SerialName("BOOLEAN")
        @Serializable
        data class BoolVal(val value: Boolean) : HyprValue()

        @SerialName("VEC2")
        @Serializable
        data class VecVal(var value: List<Int>) : HyprValue() {
            init {
                require(value.size == 2) { value = listOf(0, 0) }
            }
        }

        @SerialName("COLOR")
        @Serializable
        data class ColorVal(var value: String) : HyprValue()
    }

    fun validateHyprlandTypesForValidHyprValues(value: String, type: HyprlandTypes): HyprValue? {
        when (type) {
            HyprlandTypes.INT -> {
                val valueOf = value.toIntOrNull()

                return valueOf?.let { return@let HyprValue.IntVal(it) }
            }

            HyprlandTypes.BOOL -> {
                val valueOf = value.toBooleanStrictOrNull() ?: getBoolean(value)

                return valueOf?.let { return@let HyprValue.BoolVal(it) }
            }

            HyprlandTypes.FLOAT -> {
                val valueOf = value.toFloatOrNull()

                return valueOf?.let { return@let HyprValue.FloatVal(it) }
            }

            HyprlandTypes.COLOR -> {
                val valueOf = parseColorToRgba(value)?.let { return@let it }

                return valueOf?.let { return@let HyprValue.ColorVal(it) }
            }

            HyprlandTypes.VEC2 -> {
                val valueOf = value.split(",").map { it.trim().toIntOrNull() ?: 0 }

                return valueOf.let { return@let HyprValue.VecVal(it) }
            }

            HyprlandTypes.MOD -> {

                val valueOf = value.takeIf { modifierKeys.contains(value.uppercase()) }

                return valueOf?.let { return@let HyprValue.StrVal(it) }
            }

            HyprlandTypes.STR -> {

                return value.let { return@let HyprValue.StrVal(it) }
            }

            HyprlandTypes.GRADIENT -> {

                return value.let { return@let HyprValue.StrVal(it) }
            }

            HyprlandTypes.FONT_WEIGHT -> {
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

                val valueOf = value.takeIf { fontWeights.contains(it) }

                return valueOf?.let { return@let HyprValue.StrVal(it) }
            }
        }
    }

    private fun parseColorToRgba(input: String): String? {
        val rgbaHexPattern = Regex("""rgba\(([0-9a-fA-F]{8})\)""")
        val rgbaDecimalPattern = Regex("""rgba\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d*\.?\d+)\s*\)""")
        val rgbHexPattern = Regex("""rgb\(([0-9a-fA-F]{6})\)""")
        val rgbDecimalPattern = Regex("""rgb\(\s*(\d{1,3})\s*,\s*(\d{1,3})\s*,\s*(\d{1,3})\s*\)""")
        val legacyArgbPattern = Regex("""0x([0-9a-fA-F]{8})""")

        return when {
            rgbaHexPattern.matches(input) -> {
                val hex = rgbaHexPattern.find(input)!!.groupValues[1]
                val r = hex.substring(0, 2).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                val a = hex.substring(6, 8).toInt(16) / 255.0
                "rgba($r, $g, $b, $a)"
            }

            rgbaDecimalPattern.matches(input) -> {
                val (r, g, b, a) = rgbaDecimalPattern.find(input)!!.destructured
                "rgba(${r.toInt()}, ${g.toInt()}, ${b.toInt()}, ${a.toDouble()})"
            }

            rgbHexPattern.matches(input) -> {
                val hex = rgbHexPattern.find(input)!!.groupValues[1]
                val r = hex.substring(0, 2).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                "rgba($r, $g, $b, 1.0)"
            }

            rgbDecimalPattern.matches(input) -> {
                val (r, g, b) = rgbDecimalPattern.find(input)!!.destructured
                "rgba(${r.toInt()}, ${g.toInt()}, ${b.toInt()}, 1.0)"
            }

            legacyArgbPattern.matches(input) -> {
                val hex = legacyArgbPattern.find(input)!!.groupValues[1]
                val a = hex.substring(0, 2).toInt(16) / 255.0
                val r = hex.substring(2, 4).toInt(16)
                val g = hex.substring(4, 6).toInt(16)
                val b = hex.substring(6, 8).toInt(16)
                "rgba($r, $g, $b, $a)"
            }

            else -> null
        }
    }


    private fun getBoolean(value: String): Boolean? {
        return when (value) {
            "0" ,"no" -> false
            "1" ,"yes" -> true
            else -> null
        }
    }

}