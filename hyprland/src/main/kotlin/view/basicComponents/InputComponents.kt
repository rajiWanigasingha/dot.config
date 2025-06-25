@file:OptIn(ExperimentalSerializationApi::class)

package org.dot.config.view.basicComponents

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyprconfig.helpers.HyprlandTypes
import org.slf4j.LoggerFactory

object InputComponents {

    private val logger = LoggerFactory.getLogger(javaClass.name)

    @Serializable
    enum class TypesOfInputs {
        INPUT_INT,
        INPUT_TOGGLE,
        INPUT_RANGE,
        INPUT_STR_SELECT,
        INPUT_STR,
        INPUT_FLOAT,
        INPUT_INT_SELECT,
        INPUT_VEC
    }

    @Serializable
    data class ValidationObj(
        val regex: String? = null,
        val range: List<Float>? = null,
        val optionsInt: List<Int>? = null,
        val optionStr: List<String>? = null,
        val ints: List<Int>? = null,
        val floats: List<Float>? = null
    ) {
        init {
            if (range != null) require(range.size == 2) { "Range of validation is out of bound" }
        }
    }

    @Serializable
    sealed interface InputShouldHave {
        val placeholder: String
        val validation: ValidationObj
        val validationError: String
        val typeOfHyprland: HyprlandTypes
    }

    @Serializable
    @SerialName("INPUT_INT")
    data class InputInt(
        var value: Int? = null,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.INT
    ) : InputShouldHave

    @Serializable
    @SerialName("INPUT_TOGGLE")
    data class InputToggle(
        var value: Boolean? = null,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.BOOL
    ) : InputShouldHave

    @Serializable
    @SerialName("INPUT_RANGE")
    data class InputRange(
        var value: Float? = null,
        val minVal: Float,
        val maxVal: Float,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.FLOAT
    ) : InputShouldHave

    @Serializable
    @SerialName("INPUT_STR_SELECT")
    data class InputSelectStrings(
        var value: String? = null,
        val valueOptions: List<String>,
        val optionExplain: List<String>,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.STR
    ): InputShouldHave

    @Serializable
    @SerialName("INPUT_STR")
    data class InputString(
        var value: String? = null,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.STR
    ) : InputShouldHave

    @Serializable
    @SerialName("INPUT_FLOAT")
    data class InputFloat(
        var value: Float? = null,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.FLOAT
    ) : InputShouldHave

    @Serializable
    @SerialName("INPUT_INT_SELECT")
    data class InputSelectInt(
        var value: Int? = null,
        val valueOptions: List<Int>,
        val optionExplain: List<String>,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.INT
    ): InputShouldHave

    @Serializable
    @SerialName("INPUT_VEC")
    data class InputVec(
        var value: List<Int>,
        override val placeholder: String,
        override val validation: ValidationObj,
        override val validationError: String,
        @EncodeDefault
        override val typeOfHyprland: HyprlandTypes = HyprlandTypes.VEC2
    ) : InputShouldHave

}