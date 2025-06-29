package org.dot.config.view.errors

import org.hyprconfig.helpers.HyprlandTypes

object ErrorsBasicInputComponent {

    class InputIntInvalidType() : IllegalArgumentException("Value that trying to set into input int field is not a Int value")

    class InputStrInvalidType() : IllegalArgumentException("Value that trying to set into input strings field is not a string value")

    class InputToggleInvalidType() : IllegalArgumentException("Value that trying to set into input toggle field is not a boolean value")

    class InputFloatInvalidType() : IllegalArgumentException("Value that trying to set into input float field is not a floating point value")

    class InputVecInvalidType() : IllegalArgumentException("Value that trying to set into input vec filed is not a vector value")

    class InputColorInvalidType() : IllegalArgumentException("Value that trying to set into input color filed is not a color value")

    class InputGradiantInvalidType() : IllegalArgumentException("Value that trying to set into input gradiant of colors filed is not a gradiant colors value")

    class UpdateMainPageStandedCategoryCouldNotFound(name: String ,category: String ,type: HyprlandTypes) : Exception("Couldn't update because this $name settings didn't exist or something didn't match $category ,$type")
}