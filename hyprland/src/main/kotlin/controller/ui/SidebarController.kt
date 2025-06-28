package org.dot.config.controller.ui

import org.dot.config.controller.helpers.BuildPageHelpers
import org.dot.config.controller.helpers.HandlePaths
import org.dot.config.model.Helpers
import org.dot.config.model.InputAndOutput
import org.dot.config.model.Tables
import org.dot.config.view.basicComponents.InputComponents
import org.dot.config.view.builderComponents.Sidebar
import org.dot.config.view.errors.ErrorsBasicInputComponent
import org.hyprconfig.helpers.HyprlandTypes
import org.slf4j.LoggerFactory

class SidebarController {

    private val standedCategoryUI = mutableListOf<InputAndOutput.StandedCategoryBuildUI>()

    private val logger = LoggerFactory.getLogger(javaClass.name)

    private val handlePaths = HandlePaths()

    fun getSidebarUI(): List<Sidebar.SideBarComponent> {
        return Sidebar.navigationSidebar
    }

    fun getPageUI(actionLinks: Sidebar.ActionLinks): List<InputAndOutput.StandedCategoryBuildUI> {
        when (actionLinks) {
            Sidebar.ActionLinks.MOUSE_AND_TOUCHPAD -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.CURSOR -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.GESTURES -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.TOUCH -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.KEYBOARD -> TODO()

            Sidebar.ActionLinks.KEYBINDS -> TODO()

            Sidebar.ActionLinks.LAYOUT_GENERAL -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.DWINDLE_LAYOUT -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.MASTER_LAYOUT -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.GROUPS -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.GAPS_AND_BORDERS -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            else -> TODO()
        }
    }

    fun getHelpUI(actionLinks: Sidebar.ActionLinks, validate: String) {}

    private fun createPageUI(
        actionLinks: Sidebar.ActionLinks,
        pageSettings: List<String>
    ): List<InputAndOutput.StandedCategoryBuildUI> {
        standedCategoryUI.clear()

        logger.info("Create page settings $actionLinks")

        InputAndOutput
            .buildUITableCsv(actionLink = actionLinks, pathToSettings = pageSettings)
            .forEach {
                val type = runCatching {
                    return@runCatching HyprlandTypes.valueOf(value = it.type.uppercase())
                }.getOrNull() ?: return@forEach

                val value =
                    Helpers.validateHyprlandTypesForValidHyprValues(value = it.value, type = type)

                val inputTypes = InputComponents.TypesOfInputs.valueOf(it.method)

                if (value == null) {
                    when (inputTypes) {
                        InputComponents.TypesOfInputs.INPUT_INT -> throw ErrorsBasicInputComponent.InputIntInvalidType()
                        InputComponents.TypesOfInputs.INPUT_TOGGLE -> throw ErrorsBasicInputComponent.InputToggleInvalidType()
                        InputComponents.TypesOfInputs.INPUT_RANGE -> throw ErrorsBasicInputComponent.InputFloatInvalidType()
                        InputComponents.TypesOfInputs.INPUT_STR_SELECT -> throw ErrorsBasicInputComponent.InputStrInvalidType()
                        InputComponents.TypesOfInputs.INPUT_STR -> throw ErrorsBasicInputComponent.InputStrInvalidType()
                        InputComponents.TypesOfInputs.INPUT_FLOAT -> throw ErrorsBasicInputComponent.InputFloatInvalidType()
                        InputComponents.TypesOfInputs.INPUT_INT_SELECT -> throw ErrorsBasicInputComponent.InputIntInvalidType()
                        InputComponents.TypesOfInputs.INPUT_VEC -> throw ErrorsBasicInputComponent.InputVecInvalidType()
                        InputComponents.TypesOfInputs.INPUT_COLOR -> throw ErrorsBasicInputComponent.InputColorInvalidType()
                    }
                }

                val standed = Tables.StandedHyprlandLangTable(
                    category = it.category,
                    name = it.displayName,
                    settingsName = it.settingsName,
                    description = it.description,
                    typeOfHyprland = type,
                    value = value
                )

                val ui = BuildPageHelpers.createUI(
                    inputTypes = inputTypes,
                    content = standed,
                    validate = it.validate,
                    special = it.special
                )

                insertNewUI(ui = ui, data = standed, tab = it.tab)
            }

        return standedCategoryUI.toList()
    }

    private fun insertNewUI(ui: InputComponents.InputShouldHave, data: Tables.StandedHyprlandLangTable, tab: String) {
        standedCategoryUI
            .find { it.tab == tab }
            ?.settings
            ?.add(
                InputAndOutput.SettingsUI(
                    inputUI = ui,
                    data = data
                )
            )
            ?: run {
                standedCategoryUI.add(
                    InputAndOutput.StandedCategoryBuildUI(
                        tab = tab,
                        settings = mutableListOf(
                            InputAndOutput.SettingsUI(
                                inputUI = ui,
                                data = data
                            )
                        )
                    )
                )
            }
    }
}