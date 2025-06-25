package org.dot.config.controller

import org.dot.config.controller.helpers.HandlePaths
import org.dot.config.model.Tables
import org.hyprconfig.model.AnimationsModel
import org.hyprconfig.model.BindsModel
import org.hyprconfig.model.CursorModel
import org.hyprconfig.model.DebugModel
import org.hyprconfig.model.DecorationModel
import org.hyprconfig.model.DwindleModel
import org.hyprconfig.model.EcosystemModel
import org.hyprconfig.model.ExperimentalModel
import org.hyprconfig.model.GeneralModel
import org.hyprconfig.model.GesturesModel
import org.hyprconfig.model.GroupModel
import org.hyprconfig.model.InputsModel
import org.hyprconfig.model.MasterModel
import org.hyprconfig.model.MiscModel
import org.hyprconfig.model.OpenGLModel
import org.hyprconfig.model.RenderModel
import org.hyprconfig.model.SettingsModel
import org.hyprconfig.model.XwaylandModel
import org.hyprconfig.parseHyprland
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

object Initialize {

    private val logger = LoggerFactory.getLogger(javaClass.name)

    private val nameOfFiles = listOf(
        Pair("general", "general"),
        Pair("generalSnap", "general"),
        Pair("decoration", "decoration"),
        Pair("decorationBlur", "decoration"),
        Pair("decorationShadow", "decoration"),
        Pair("animations", "animations"),
        Pair("inputs" ,"inputs"),
        Pair("inputsTouchpad" ,"inputs"),
        Pair("inputsTouchDevice","inputs"),
        Pair("inputsTablet","inputs"),
        Pair("cursor","cursor"),
        Pair("gestures","gestures"),
        Pair("group","group"),
        Pair("groupBar","group"),
        Pair("misc","misc"),
        Pair("binds","binds"),
        Pair("xwayland","xwayland"),
        Pair("openGl","openGL"),
        Pair("render","render"),
        Pair("ecosystem","ecosystem"),
        Pair("dwindle","dwindle"),
        Pair("master","master"),
        Pair("debug","debug"),
        Pair("experimental","experimental")
    )

    init {

        logger.info("Creating All Files")

        val beginParser = HandlePaths().checkNeedToParsedAnyPath()

        if (!beginParser) {

            logger.info("Begin to parse hyprland settings")

            val dataStorePath = HandlePaths().createFiles()

            val parsedSettings = parseHyprland()

            beginStoringParsedSettings(path = dataStorePath, settingsModel = parsedSettings)

            logger.info("All parsed settings has been successfully stored")

        } else {

            logger.info("Skipping the parsing. It's already up to date")

        }

    }

    private fun beginStoringParsedSettings(path: String, settingsModel: SettingsModel) {
        // Create Common Structure Of Hyprland
        parserTheStandedSettings(path = path, nameOfFile = nameOfFiles, settingsModel = settingsModel)

        // Create Keyboard Structure Of Hyprland
        val keybindSettings = settingsModel.bind

        val allKeybindSettings = mutableListOf<Tables.KeybindTable>()

        keybindSettings?.forEach {
            allKeybindSettings.add(
                Tables.KeybindTable(
                    flags = it.flags.toList(),
                    mod = it.mod,
                    keys = it.key,
                    description = it.description,
                    dispatcher = it.dispatcher,
                    args = it.args
                )
            )
        }

        val keybindDf = allKeybindSettings.toDataFrame()

        keybindDf.writeCsv("$path/keybind.csv")

        // Create Animation Structure Of Hyprland
        val animationSettings = settingsModel.animation

        val allAnimationSettings = mutableListOf<Tables.AnimationTable>()

        animationSettings?.forEach {
            allAnimationSettings.add(
                Tables.AnimationTable(
                    name = it.name,
                    onOff = it.active.toInt(),
                    speed = it.speed,
                    curve = it.bezier,
                    style = it.animation
                )
            )
        }

        val animationDf = allAnimationSettings.toDataFrame()

        animationDf.writeCsv("$path/animation.csv")

        // Create Bezier Structure Of Hyprland
        val bezierSettings = settingsModel.bezier

        val allBezierSettings = mutableListOf<Tables.BezierTable>()

        bezierSettings?.forEach {
            allBezierSettings.add(
                Tables.BezierTable(
                    name = it.name,
                    x0 = it.x0,
                    x1 = it.x1,
                    y0 = it.y0,
                    y1 = it.y1
                )
            )
        }

        val bezierDf = allBezierSettings.toDataFrame()

        bezierDf.writeCsv("$path/bezier.csv")

        // Create Monitor Structure Of Hyprland
        val monitorSettings = settingsModel.monitor

        val allMonitorSettings = mutableListOf<Tables.MonitorTable>()

        monitorSettings?.forEach {
            allMonitorSettings.add(
                Tables.MonitorTable(
                    name = it.name,
                    disable = it.disable,
                    addreserved = if (it.addreserved) listOf(it.addreservedValue?.top ?: 0,it.addreservedValue?.bottom ?: 0,it.addreservedValue?.left ?: 0,it.addreservedValue?.right ?: 0,) else null,
                    resolution = it.resolution,
                    position = it.position,
                    scale = it.scale,
                    mirror = it.mirror,
                    bitDepth = it.bitDepth,
                    transform = it.transform,
                    cm = it.cm,
                    sdrbrightness = it.sdrbrightness,
                    sdrsaturation = it.sdrsaturation,
                    vrr = it.vvr
                )
            )
        }

        val monitorDf = allMonitorSettings.toDataFrame()

        monitorDf.writeCsv("$path/monitor.csv")

        // Create Window Rule Structure Of Hyprland
        val windowRulesSettings = settingsModel.windowRules

        val allWindowRulesSettings = mutableListOf<Tables.WindowRules>()

        windowRulesSettings?.forEach {

            val rules = mutableListOf<Pair<String , String>>()

            it.rules.forEach { rule -> rules.add(Pair(rule.keyword ,rule.value ?: "")) }

            allWindowRulesSettings.add(
                Tables.WindowRules(
                    rules = rules.toList(),
                    params = it.params
                )
            )
        }

        val windowRulesDf = allWindowRulesSettings.toDataFrame()

        windowRulesDf.writeCsv("$path/windowRules.csv")

        // Create Workspace Rule Structure Of Hyprland
        val workspaceRulesSettings = settingsModel.workspace

        val allWorkspaceRulesSettings = mutableListOf<Tables.WorkspaceRules>()

        workspaceRulesSettings?.forEach {
            allWorkspaceRulesSettings.add(
                Tables.WorkspaceRules(
                    name = it.name,
                    rules = it.rules
                )
            )
        }

        val workspaceRulesDf = allWorkspaceRulesSettings.toDataFrame()

        workspaceRulesDf.writeCsv("$path/workspaceRules.csv")

        // Create Autostart Structure Of Hyprland
        val autoStartSettings = settingsModel.execute

        val allAutoStartSettings = mutableListOf<Tables.AutoStart>()

        autoStartSettings?.forEach {
            allAutoStartSettings.add(
                Tables.AutoStart(
                    keyword = it.keyword,
                    command = it.command
                )
            )
        }

        val autoStartDf = allAutoStartSettings.toDataFrame()

        autoStartDf.writeCsv("$path/autoStart.csv")

        // Create Env Structure Of Hyprland
        val envSettings = settingsModel.env

        val allEnvSettings = mutableListOf<Tables.Env>()

        envSettings?.forEach {
            allEnvSettings.add(
                Tables.Env(
                    keyword = "env",
                    value = it.env
                )
            )
        }

        val envDf = allEnvSettings.toDataFrame()

        envDf.writeCsv("$path/env.csv")

        // Create layer Rules Structure Of Hyprland
        val layerRulesSettings = settingsModel.layerRules

        val allLayerRulesSettings = mutableListOf<Tables.LayerRules>()

        layerRulesSettings?.forEach {
            allLayerRulesSettings.add(
                Tables.LayerRules(
                    rule = it.rule,
                    value = it.value
                )
            )
        }

        val layerDf = allLayerRulesSettings.toDataFrame()

        layerDf.writeCsv("$path/layerRules.csv")

        // Create Time Of Updates
        val parsedTime = settingsModel.storeInfo

        val parsedSettings = mutableListOf<Tables.ParsedTime>()

        parsedTime?.forEach { parsedSettings.add(Tables.ParsedTime(it.path ,it.time)) }

        val parsedDf = parsedSettings.toDataFrame()

        parsedDf.writeCsv("$path/parsedTime.csv")
    }

    private fun parserTheStandedSettings(
        path: String,
        nameOfFile: List<Pair<String, String>>,
        settingsModel: SettingsModel
    ) {

        nameOfFile.forEach {
            var standedDefaultDF =
                DataFrame.readCsv(File(object {}.javaClass.getResource("/defaults/${it.first}.csv")!!.toURI()) , colTypes = mapOf("validate" to ColType.String))

            val property =
                settingsModel::class.memberProperties.find { data -> data.name == it.second } as? KProperty1<SettingsModel, *>

            val parsedValues = property?.get(settingsModel) as? List<*> ?: return@forEach

            parsedValues.forEach { item ->
                val standedSettings = when (item) {
                    is GeneralModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is DecorationModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is AnimationsModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is InputsModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is CursorModel ->  Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is GesturesModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is GroupModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is MiscModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is BindsModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is XwaylandModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is OpenGLModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is RenderModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is EcosystemModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is DwindleModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is MasterModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is DebugModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    is ExperimentalModel -> Tables.StandedHyprlandParsedSettings(
                        name = item.name,
                        type = item.type,
                        value = item.value
                    )

                    else -> {
                        TODO()
                    }
                }

                val name = standedSettings.name.split(":")

                if (name.size == 2) {
                    standedDefaultDF = standedDefaultDF.update { update -> update["value"] }
                        .where { "settingsName"<String>() == name.last().trim() }.with { standedSettings.value }
                }
            }

            standedDefaultDF.writeCsv("$path/${it.first}.csv")
        }

    }

}