package org.dot.config.view.errors

object ErrorsBasicInputComponent {

    class InputIntInvalidType() : IllegalArgumentException("Value that trying to set into input int field is not a Int value")

    class InputStrInvalidType() : IllegalArgumentException("Value that trying to set into input strings field is not a string value")

    class InputToggleInvalidType() : IllegalArgumentException("Value that trying to set into input toggle field is not a boolean value")

    class InputFloatInvalidType() : IllegalArgumentException("Value that trying to set into input float field is not a floating point value")

    class InputVecInvalidType() : IllegalArgumentException("Value that trying to set into input vec filed is not a vector value")
}