package org.dot.config.controller.ui

import org.dot.config.controller.services.MouseAndTouchpadService
import org.hyprconfig.helpers.HyprlandTypes

class MainPageController {

    fun mouseAndTouchpad(name: String ,value: String ,type: HyprlandTypes ,category: String): Boolean {

        val mouseAndTouchpad = MouseAndTouchpadService()

        val mouseAndTouchpadUpdate = mouseAndTouchpad.update(name ,value ,type ,category)

        mouseAndTouchpad.writeIntoHyprland()

        return mouseAndTouchpadUpdate
    }

}