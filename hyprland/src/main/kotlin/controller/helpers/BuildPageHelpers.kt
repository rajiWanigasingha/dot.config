package org.dot.config.controller.helpers

import org.dot.config.model.Helpers
import org.dot.config.model.Tables
import org.dot.config.view.basicComponents.InputComponents
import org.dot.config.view.basicComponents.InputComponents.ValidationObj
import org.slf4j.LoggerFactory

object BuildPageHelpers {

    private val logger = LoggerFactory.getLogger(javaClass.name)

    fun createValidationObject(
        validation: String,
        typeOfInput: InputComponents.TypesOfInputs
    ): InputComponents.ValidationObj {
        if (validation == "" || validation == "null") return InputComponents.ValidationObj()

        when (typeOfInput) {

            // This tries to validate if there are input is equal to one of int inside these objects
            InputComponents.TypesOfInputs.INPUT_INT -> {

                val intValidation = validation.split(",").mapNotNull { it.toIntOrNull() }

                return ValidationObj(ints = intValidation)
            }

            // There no validation for boolean, need to handle that when taking input from the ui
            InputComponents.TypesOfInputs.INPUT_TOGGLE -> return ValidationObj()

            // This tries to validate if input is on the range of minimum and maximum values.
            InputComponents.TypesOfInputs.INPUT_RANGE -> {

                val minAndMax =
                    validation.split(",").map { it.toFloatOrNull() ?: return ValidationObj() }

                if (minAndMax.size != 2) return ValidationObj()

                return ValidationObj(range = minAndMax)
            }

            // This tries to validate what option of strings can be selected
            InputComponents.TypesOfInputs.INPUT_STR_SELECT -> {

                val validateStr = validation.split(",").map { it.trim() }

                return ValidationObj(optionStr = validateStr)
            }

            // This tries to validate what kind of pattern needs to be this string
            InputComponents.TypesOfInputs.INPUT_STR -> return ValidationObj(regex = validation)

            // This tries to validate if there are input is equal to one of int inside these objects
            InputComponents.TypesOfInputs.INPUT_FLOAT -> {

                val floatValidation = validation.split(",").mapNotNull { it.toFloatOrNull() }

                return ValidationObj(floats = floatValidation)
            }

            // This tries to validate what option of int can be selected
            InputComponents.TypesOfInputs.INPUT_INT_SELECT -> {

                val validateInt =
                    validation.split(",").map { it.trim().toIntOrNull() ?: return ValidationObj() }

                return ValidationObj(optionsInt = validateInt)
            }

            InputComponents.TypesOfInputs.INPUT_VEC -> return ValidationObj()

            InputComponents.TypesOfInputs.INPUT_COLOR -> return ValidationObj()
        }
    }


    fun createUI(
        inputTypes: InputComponents.TypesOfInputs,
        content: Tables.StandedHyprlandLangTable,
        validate: String,
        special: String
    ): InputComponents.InputShouldHave {
        when (inputTypes) {
            InputComponents.TypesOfInputs.INPUT_INT -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_INT)

                val inputUI = InputComponents.InputInt(
                    value = (content.value as Helpers.HyprValue.IntVal).value,
                    placeholder = content.description,
                    typeOfHyprland = content.typeOfHyprland,
                    validation = validateOption,
                    validationError = "Invalid Integer Inputs, Must Be One Of ${validateOption.ints?.joinToString(",")}"
                )

                return inputUI
            }

            InputComponents.TypesOfInputs.INPUT_TOGGLE -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_TOGGLE)

                val inputUI = InputComponents.InputToggle(
                    value = (content.value as Helpers.HyprValue.BoolVal).value,
                    placeholder = content.description,
                    typeOfHyprland = content.typeOfHyprland,
                    validation = validateOption,
                    validationError = ""
                )

                return inputUI
            }

            InputComponents.TypesOfInputs.INPUT_RANGE -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_RANGE)

                val inputUI = InputComponents.InputRange(
                    value = (content.value as Helpers.HyprValue.FloatVal).value,
                    minVal = validateOption.range!![0],
                    maxVal = validateOption.range[1],
                    placeholder = content.description,
                    typeOfHyprland = content.typeOfHyprland,
                    validation = validateOption,
                    validationError = "Invalid Float Inputs, Must In Range Of ${validateOption.range[0]} - ${validateOption.range[1]}"
                )

                return inputUI
            }


            InputComponents.TypesOfInputs.INPUT_STR_SELECT -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_STR_SELECT)

                val options = special.split(",").map { it.trim() }

                val inputUI = InputComponents.InputSelectStrings(
                    value = (content.value as Helpers.HyprValue.StrVal).value,
                    valueOptions = validateOption.optionStr!!,
                    optionExplain = options,
                    placeholder = content.description,
                    validation = validateOption,
                    validationError = "Invalid Select String Input.Must Be One Of ${
                        validateOption.optionStr.joinToString(
                            ","
                        )
                    }",
                    typeOfHyprland = content.typeOfHyprland
                )

                return inputUI
            }


            InputComponents.TypesOfInputs.INPUT_STR -> {
                val validationOption = createValidationObject(validation = validate , typeOfInput = InputComponents.TypesOfInputs.INPUT_STR)

                val inputUI = InputComponents.InputString(
                    value = (content.value as Helpers.HyprValue.StrVal).value,
                    placeholder = content.description,
                    validation = validationOption,
                    validationError = "Invalid String Inputs, Must Be With Regex $validate",
                    typeOfHyprland = content.typeOfHyprland
                )

                return inputUI
            }

            InputComponents.TypesOfInputs.INPUT_FLOAT -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_FLOAT)

                val inputUI = InputComponents.InputFloat(
                    value = (content.value as Helpers.HyprValue.FloatVal).value,
                    placeholder = content.description,
                    typeOfHyprland = content.typeOfHyprland,
                    validation = validateOption,
                    validationError = "Invalid Float Inputs, Must Be One Of ${validateOption.floats?.joinToString(",")}"
                )

                return inputUI
            }

            InputComponents.TypesOfInputs.INPUT_INT_SELECT -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_INT_SELECT)

                val options = special.split(",").map { it.trim() }

                val inputUI = InputComponents.InputSelectInt(
                    value = (content.value as Helpers.HyprValue.IntVal).value,
                    valueOptions = validateOption.optionsInt!!,
                    optionExplain = options,
                    placeholder = content.description,
                    validation = validateOption,
                    validationError = "Invalid Select Int Input. Must Be One Of ${
                        validateOption.optionsInt.joinToString(
                            ","
                        )
                    }",
                    typeOfHyprland = content.typeOfHyprland
                )

                return inputUI
            }

            InputComponents.TypesOfInputs.INPUT_VEC -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_VEC)

                val inputUI = InputComponents.InputVec(
                    value = (content.value as Helpers.HyprValue.VecVal).value,
                    placeholder = content.description,
                    typeOfHyprland = content.typeOfHyprland,
                    validation = validateOption,
                    validationError = ""
                )

                return inputUI
            }

            InputComponents.TypesOfInputs.INPUT_COLOR -> {
                val validateOption =
                    createValidationObject(validation = validate, typeOfInput = InputComponents.TypesOfInputs.INPUT_COLOR)

                val inputUI = InputComponents.InputColor(
                    value = (content.value as Helpers.HyprValue.ColorVal).value,
                    placeholder = content.description,
                    typeOfHyprland = content.typeOfHyprland,
                    validation = validateOption,
                    validationError = ""
                )

                return inputUI
            }
        }
    }

}