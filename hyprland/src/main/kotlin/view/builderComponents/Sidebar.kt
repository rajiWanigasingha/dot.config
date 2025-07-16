package org.dot.config.view.builderComponents

import kotlinx.serialization.Serializable

object Sidebar {

    @Serializable
    enum class ActionLinks {
        MOUSE_AND_TOUCHPAD,
        CURSOR,
        GESTURES,
        KEYBOARD,
        KEYBINDS,
        DISPLAY_AND_MONITOR,
        TOUCH,
        LAYOUT_GENERAL,
        DWINDLE_LAYOUT,
        MASTER_LAYOUT,
        WORKSPACE_RULES,
        WINDOW_RULES,
        GROUPS,
        GAPS_AND_BORDERS,
        WINDOW_DECORATION,
        BLUR,
        SHADOW,
        ANIMATIONS,
        ENV,
        AUTOSTART,
        MISC,
        GRAPHICS,
        ECOSYSTEM,
        EXPERIMENT,
        DEBUG,
        VARIABLES
    }

    @Serializable
    data class SideBarComponent(
        val componentTitle: String,
        val navigationSettings: List<NavigationSettings>
    )

    @Serializable
    data class NavigationSettings(
        val icon: String,
        val name: String,
        val actionLink: ActionLinks
    )

    val navigationSidebar = listOf(
        SideBarComponent(
            componentTitle = "Input & Output",
            navigationSettings = listOf(
                NavigationSettings(
                    icon = "mouseAndTouchpad",
                    name = "Mouse & Touchpad",
                    actionLink = ActionLinks.MOUSE_AND_TOUCHPAD
                ),
                NavigationSettings(
                    icon = "cursor",
                    name = "Cursor Configuration",
                    actionLink = ActionLinks.CURSOR
                ),
                NavigationSettings(
                    icon = "gestures",
                    name = "Mouse Gestures",
                    actionLink = ActionLinks.GESTURES
                ),
                NavigationSettings(
                    icon = "keyboard",
                    name = "Keyboard & Binds",
                    actionLink = ActionLinks.KEYBOARD
                ),
                NavigationSettings(
                    icon = "bind",
                    name = "Global Shortcuts",
                    actionLink = ActionLinks.KEYBINDS
                ),
                NavigationSettings(
                    icon = "monitor",
                    name = "Display & Monitor",
                    actionLink = ActionLinks.DISPLAY_AND_MONITOR
                ),
                NavigationSettings(
                    icon = "touch",
                    name = "Touch Devices & Tables",
                    actionLink = ActionLinks.TOUCH
                )
            )
        ),
        SideBarComponent(
            componentTitle = "Layering",
            navigationSettings = listOf(
                NavigationSettings(
                    icon = "layoutGeneral",
                    name = "General Layout And Layers",
                    actionLink = ActionLinks.LAYOUT_GENERAL
                ),
                NavigationSettings(
                    icon = "dwindle",
                    name = "Dwindle Layout",
                    actionLink = ActionLinks.DWINDLE_LAYOUT
                ),
                NavigationSettings(
                    icon = "master",
                    name = "Master Layout",
                    actionLink = ActionLinks.MASTER_LAYOUT
                )
            )
        ),
        SideBarComponent(
            componentTitle = "Applications & Workspaces",
            navigationSettings = listOf(
                NavigationSettings(
                    icon = "workspaces",
                    name = "Workspace Rules",
                    actionLink = ActionLinks.WORKSPACE_RULES
                ),
                NavigationSettings(
                    icon = "window",
                    name = "Window Rules",
                    actionLink = ActionLinks.WINDOW_RULES
                ),
                NavigationSettings(
                    icon = "group",
                    name = "Application Groups",
                    actionLink = ActionLinks.GROUPS
                ),
            )
        ),
        SideBarComponent(
            componentTitle = "Look & Feel",
            navigationSettings = listOf(
                NavigationSettings(
                    icon = "gapAndBorders",
                    name = "Gaps & Borders",
                    actionLink = ActionLinks.GAPS_AND_BORDERS
                ),
                NavigationSettings(
                    icon = "windowDecoration",
                    name = "Window Decoration",
                    actionLink = ActionLinks.WINDOW_DECORATION
                ),
                NavigationSettings(
                    icon = "blur",
                    name = "Blur",
                    actionLink = ActionLinks.BLUR
                ),
                NavigationSettings(
                    icon = "shadow",
                    name = "Shadow",
                    actionLink = ActionLinks.SHADOW
                ),
                NavigationSettings(
                    icon = "animations",
                    name = "Animations",
                    actionLink = ActionLinks.ANIMATIONS
                )
            )
        ),
        SideBarComponent(
            componentTitle = "Other",
            navigationSettings = listOf(
                NavigationSettings(
                    icon = "var",
                    name = "Variables",
                    actionLink = ActionLinks.VARIABLES
                ),
                NavigationSettings(
                    icon = "autostart",
                    name = "Auto Start",
                    actionLink = ActionLinks.AUTOSTART
                ),
                NavigationSettings(
                    icon = "env",
                    name = "Environment Variables",
                    actionLink = ActionLinks.ENV
                ),
                NavigationSettings(
                    icon = "misc",
                    name = "Misc",
                    actionLink = ActionLinks.MISC
                ),
                NavigationSettings(
                    icon = "graphic",
                    name = "Graphics",
                    actionLink = ActionLinks.GRAPHICS
                ),
                NavigationSettings(
                    icon = "eco",
                    name = "Ecosystem",
                    actionLink = ActionLinks.ECOSYSTEM
                ),
                NavigationSettings(
                    icon = "experiment",
                    name = "Experimental Protocols",
                    actionLink = ActionLinks.EXPERIMENT
                ),
                NavigationSettings(
                    icon = "debug",
                    name = "Debug",
                    actionLink = ActionLinks.DEBUG
                )
            )
        )
    )
}