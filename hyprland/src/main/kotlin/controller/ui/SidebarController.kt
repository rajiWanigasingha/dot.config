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
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readCsv
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

            Sidebar.ActionLinks.WINDOW_DECORATION -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.BLUR -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.SHADOW -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.GRAPHICS -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.MISC -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.ECOSYSTEM -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.EXPERIMENT -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.DEBUG -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            Sidebar.ActionLinks.ANIMATIONS -> return createPageUI(
                actionLinks = actionLinks,
                pageSettings = handlePaths.getPathToUpdate(actionLink = actionLinks)
            )

            else -> {
                throw Exception("This Is A Bug Should Not Reach This Part")
            }
        }
    }

    fun getVariableUI(): List<InputAndOutput.VariableUI> {

        val variableUI = mutableListOf<InputAndOutput.VariableUI>()

        val variables = DataFrame.readCsv("${System.getProperty("user.home")}/.dot.config/data/variable.csv" ,colTypes = mapOf("name" to ColType.String ,"value" to ColType.String))

        variables.forEach { row ->
            variableUI.add(
                InputAndOutput.VariableUI(
                    name = row["name"].toString(),
                    value = row["value"].toString()
                )
            )
        }

        return variableUI.toList()
    }

    fun getAutoStart(): List<Tables.AutoStart> {

        val autoStartUI = mutableListOf<Tables.AutoStart>()

        val autoStart = DataFrame.readCsv(fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/autoStart.csv" ,colTypes = mapOf("keyword" to ColType.String ,"command" to ColType.String))

        autoStart.forEach { row ->
            autoStartUI.add(
                Tables.AutoStart(
                    keyword = row["keyword"].toString(),
                    command = row["command"].toString()
                )
            )
        }

        return autoStartUI.toList()
    }

    fun getEnv(): List<Tables.Env> {

        val envUI = mutableListOf<Tables.Env>()

        val env = DataFrame.readCsv(fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/env.csv" , colTypes = mapOf("keyword" to ColType.String ,"value" to ColType.String))

        env.forEach { row ->
            envUI.add(
                Tables.Env(
                    keyword = row["keyword"].toString(),
                    value = row["value"].toString()
                )
            )
        }

        return envUI.toList()
    }

    fun getKeybinds(): List<Tables.KeybindTable> {

        val keybinds = mutableListOf<Tables.KeybindTable>()

        val keybind = DataFrame.readCsv(fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/keybind.csv" , colTypes = mapOf("flags" to ColType.String ,"mod" to ColType.String ,"keys" to ColType.String ,"description" to ColType.String ,"dispatcher" to ColType.String ,"args" to ColType.String))

        keybind.forEach { row ->
            keybinds.add(
                Tables.KeybindTable(
                    flags = row["flags"].toString().removePrefix("[").removeSuffix("]").split(",").mapNotNull { it.trim().toCharArray().getOrNull(0) },
                    mod = row["mod"].toString().removePrefix("[").removeSuffix("]").split(",").map { it.trim() },
                    keys = row["keys"].toString().removePrefix("[").removeSuffix("]").split(",").map { it.trim() },
                    description = row["description"].toString(),
                    dispatcher = row["dispatcher"].toString(),
                    args = row["args"].toString()
                )
            )
        }

        return keybinds.toList()
    }

    fun getMonitors(): List<Tables.MonitorTable> {

        val monitor = mutableListOf<Tables.MonitorTable>()

        val monitorSettings = DataFrame.readCsv(fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/monitor.csv" , colTypes = mapOf("name" to ColType.String ,"disable" to ColType.String ,"addreserved" to ColType.String ,"resolution" to ColType.String ,"position" to ColType.String ,"scale" to ColType.String ,"mirror" to ColType.String ,"bitDepth" to ColType.String ,"transform" to ColType.String ,"cm" to ColType.String ,"sdrsaturation" to ColType.String ,"sdrbrightness" to ColType.String ,"vrr" to ColType.String))

        monitorSettings.forEach { row ->
            monitor.add(
                Tables.MonitorTable(
                    name = row["name"].toString().takeIf { it != "null" }?.let { return@let it } ?: "",
                    disable = row["disable"].toString().toBoolean(),
                    addreserved = row["addreserved"].toString().takeIf { it != "null" }?.removePrefix("[")
                        ?.removeSuffix("]")?.split(",")?.map { it.trim().toInt() },
                    resolution = row["resolution"].toString(),
                    position = row["position"].toString(),
                    scale = row["scale"].toString(),
                    mirror = row["mirror"].toString().takeIf { it !== "null" }?.let { return@let it },
                    bitDepth = row["bitDepth"].toString().takeIf { it !== "null" }?.let { return@let it.toInt() },
                    transform = row["transform"].toString().takeIf { it !== "null" }?.let { return@let it.toInt() },
                    cm = row["cm"].toString().takeIf { it !== "null" }?.let { return@let it },
                    sdrsaturation = row["sdrsaturation"].toString().takeIf { it !== "null" }?.let { return@let it.toFloat() },
                    sdrbrightness = row["sdrbrightness"].toString().takeIf { it !== "null" }?.let { return@let it.toFloat() },
                    vrr = row["vrr"].toString().takeIf { it !== "null" }?.let { return@let it.toInt() }
                )
            )
        }

        return monitor.toList()
    }

    fun getAnimation(): Pair<List<Tables.AnimationTable>, List<Tables.BezierTable>> {

        val animation = mutableListOf<Tables.AnimationTable>()

        val animationSettings = DataFrame.readCsv(fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/animation.csv" , colTypes = mapOf("name" to ColType.String ,"onOff" to ColType.Int ,"speed" to ColType.String ,"curve" to ColType.String ,"style" to ColType.String))

        animationSettings.forEach { row ->
            animation.add(
                Tables.AnimationTable(
                    name = row["name"].toString(),
                    onOff = row["onOff"].toString().toInt(),
                    speed = row["speed"].toString(),
                    curve = row["curve"].toString(),
                    style = row["style"].toString()
                )
            )
        }

        val bezier = mutableListOf<Tables.BezierTable>()

        val bezierSettings = DataFrame.readCsv(fileOrUrl = "${System.getProperty("user.home")}/.dot.config/data/bezier.csv" , colTypes = mapOf("name" to ColType.String ,"x0" to ColType.String ,"y0" to ColType.String ,"x1" to ColType.String ,"y1" to ColType.String))

        bezierSettings.forEach { row ->
            bezier.add(
                Tables.BezierTable(
                    name = row["name"].toString(),
                    x0 = row["x0"].toString(),
                    y0 = row["y0"].toString(),
                    x1 = row["x1"].toString(),
                    y1 = row["y1"].toString(),
                )
            )
        }

        return Pair(animation.toList() ,bezier.toList())
    }

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
                        InputComponents.TypesOfInputs.INPUT_GRADIANT -> throw ErrorsBasicInputComponent.InputColorInvalidType()
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