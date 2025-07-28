package org.dot.config.controller.ui

import org.dot.config.controller.helpers.HandlePaths
import org.dot.config.controller.services.WriteIntoHyprland
import org.dot.config.model.SendAndReceive
import org.dot.config.view.builderComponents.Sidebar
import org.dot.config.view.errors.ErrorsBasicInputComponent
import org.hyprconfig.helpers.HyprlandTypes
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.isEmpty
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class MainPageController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val user = System.getProperty("user.home")
    private val dataStorePath = "${user}/.dot.config/data"

    fun updateChanges(data: SendAndReceive.ReceiveMainActionForStandedInputs): Boolean {

        when (data.actionLink) {
            Sidebar.ActionLinks.MOUSE_AND_TOUCHPAD -> {
                val update = updateStandedInDatabase(data)

                if (update) {
                    createHyprlandText(
                        listOfPaths = listOf(
                            "inputs.csv",
                            "inputsTouchpad.csv",
                            "inputsTouchDevice.csv",
                            "inputsTablet.csv"
                        ),
                        settingsStructure = listOf(
                            "input {",
                            "touchpad {",
                            "}",
                            "touchdevice {",
                            "}",
                            "tablet {",
                            "}",
                            "}"
                        ),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/inputs.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.CURSOR -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("cursor.csv"),
                        settingsStructure = listOf("cursor {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/cursor.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.GESTURES -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("gestures.csv"),
                        settingsStructure = listOf("gestures {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/gestures.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.KEYBOARD -> TODO()
            Sidebar.ActionLinks.KEYBINDS -> TODO()
            Sidebar.ActionLinks.DISPLAY_AND_MONITOR -> TODO()
            Sidebar.ActionLinks.TOUCH -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    if (data.category != "misc") {
                        createHyprlandText(
                            listOfPaths = listOf(
                                "inputs.csv",
                                "inputsTouchpad.csv",
                                "inputsTouchDevice.csv",
                                "inputsTablet.csv"
                            ),
                            settingsStructure = listOf(
                                "input {",
                                "touchpad {",
                                "}",
                                "touchdevice {",
                                "}",
                                "tablet {",
                                "}",
                                "}"
                            ),
                            pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/inputs.conf",
                        )
                    } else {
                        createHyprlandText(
                            listOfPaths = listOf(
                                "misc.csv",
                            ),
                            settingsStructure = listOf(
                                "misc {",
                                "}"
                            ),
                            pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/misc.conf",
                        )
                    }

                }

                return update
            }

            Sidebar.ActionLinks.LAYOUT_GENERAL -> {
                val update = updateStandedInDatabase(data)

                if (update) {
                    if (data.category != "misc") {
                        createHyprlandText(
                            listOfPaths = listOf("general.csv", "generalSnap.csv"),
                            settingsStructure = listOf("general {", "snap {", "}", "}"),
                            pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/general.conf",
                        )
                    } else {
                        createHyprlandText(
                            listOfPaths = listOf("misc.csv"),
                            settingsStructure = listOf("misc {", "}"),
                            pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/misc.conf",
                        )
                    }
                }

                return update
            }

            Sidebar.ActionLinks.DWINDLE_LAYOUT -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("dwindle.csv"),
                        settingsStructure = listOf("dwindle {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/dwindle.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.MASTER_LAYOUT -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("master.csv"),
                        settingsStructure = listOf("master {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/master.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.WORKSPACE_RULES -> TODO()
            Sidebar.ActionLinks.WINDOW_RULES -> TODO()
            Sidebar.ActionLinks.GROUPS -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("group.csv", "groupBar.csv"),
                        settingsStructure = listOf("group {", "groupbar {", "}", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/group.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.GAPS_AND_BORDERS -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("general.csv", "generalSnap.csv"),
                        settingsStructure = listOf("general {", "snap {", "}", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/general.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.WINDOW_DECORATION, Sidebar.ActionLinks.BLUR, Sidebar.ActionLinks.SHADOW -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    if (data.category != "misc") {
                        createHyprlandText(
                            listOfPaths = listOf("decoration.csv", "decorationBlur.csv", "decorationShadow.csv"),
                            settingsStructure = listOf("decoration {", "blur {", "}", "shadow {", "}", "}"),
                            pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/decoration.conf",
                        )
                    } else {
                        createHyprlandText(
                            listOfPaths = listOf("misc.csv"),
                            settingsStructure = listOf("misc {", "}"),
                            pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/misc.conf",
                        )
                    }
                }

                return update
            }

            Sidebar.ActionLinks.ANIMATIONS -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    createHyprlandText(
                        listOfPaths = listOf("animations.csv"),
                        settingsStructure = listOf("animations {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/animations.conf",
                    )
                }

                return update
            }
            Sidebar.ActionLinks.ANIMATION -> TODO()
            Sidebar.ActionLinks.ENV -> TODO()
            Sidebar.ActionLinks.AUTOSTART -> TODO()
            Sidebar.ActionLinks.MISC -> {
                val update = updateStandedInDatabase(data)

                if (update) {
                    createHyprlandText(
                        listOfPaths = listOf("misc.csv"),
                        settingsStructure = listOf("misc {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/misc.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.GRAPHICS -> {
                val update = updateStandedInDatabase(data)

                if (update) {

                    when (data.category) {
                        "xwayland" -> {
                            createHyprlandText(
                                listOfPaths = listOf("xwayland.csv"),
                                settingsStructure = listOf("xwayland {", "}"),
                                pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/xwayland.conf",
                            )
                        }

                        "render" -> {
                            createHyprlandText(
                                listOfPaths = listOf("render.csv"),
                                settingsStructure = listOf("render {", "}"),
                                pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/render.conf",
                            )
                        }

                        "opengl" -> {
                            createHyprlandText(
                                listOfPaths = listOf("openGl.csv"),
                                settingsStructure = listOf("opengl {", "}"),
                                pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/openGL.conf",
                            )
                        }
                    }
                }

                return update
            }

            Sidebar.ActionLinks.ECOSYSTEM -> {
                val update = updateStandedInDatabase(data)

                if (update) {
                    createHyprlandText(
                        listOfPaths = listOf("ecosystem.csv"),
                        settingsStructure = listOf("ecosystem {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/ecosystem.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.EXPERIMENT -> {
                val update = updateStandedInDatabase(data)

                if (update) {
                    createHyprlandText(
                        listOfPaths = listOf("experimental.csv"),
                        settingsStructure = listOf("experimental {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/experimental.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.DEBUG -> {
                val update = updateStandedInDatabase(data)

                if (update) {
                    createHyprlandText(
                        listOfPaths = listOf("debug.csv"),
                        settingsStructure = listOf("debug {", "}"),
                        pathToHyprland = "$user/.config/hypr/hyprConfigAutoGen/debug.conf",
                    )
                }

                return update
            }

            Sidebar.ActionLinks.VARIABLES -> TODO()
        }

    }

    private fun updateStandedInDatabase(data: SendAndReceive.ReceiveMainActionForStandedInputs): Boolean {
        logger.info("Begin Updating ${data.actionLink}")

        var update = false

        val updatePaths = HandlePaths().getPathToUpdate(actionLink = data.actionLink)

        updatePaths.forEachIndexed { index, it ->
            if (update) return@forEachIndexed

            val path = "$dataStorePath/$it"

            val inputs = DataFrame.readCsv(
                fileOrUrl = path,
                colTypes = mapOf("value" to ColType.String, "validate" to ColType.String)
            )

            val findRow =
                inputs.filter {
                    "settingsName"<String>() == data.name && "type"<String>() == data.type.toString()
                        .lowercase() && "category"<String>() == data.category
                }

            findRow.print()

            if (!findRow.isEmpty()) {
                update = true
            } else if (index == updatePaths.size - 1) {
                throw ErrorsBasicInputComponent.UpdateMainPageStandedCategoryCouldNotFound(
                    name = data.name,
                    category = data.category,
                    type = data.type
                )
            }

            val update = inputs.update { "value"<String?>() }
                .where {
                    "settingsName"<String>() == data.name && "type"<String>() == data.type.toString()
                        .lowercase() && "category"<String>() == data.category
                }
                .with { _: String? -> data.value }

            update.writeCsv(path)
        }

        return update
    }

    private fun createHyprlandText(listOfPaths: List<String>, settingsStructure: List<String>, pathToHyprland: String) {
        val hyprlandWrite = WriteIntoHyprland().write(paths = listOfPaths)

        val hyprlandSettings = mutableListOf<String>()

        settingsStructure.forEachIndexed { index, string ->
            hyprlandSettings.add(string)

            if ("""\w+\s*\{""".toRegex().matches(input = string)) {
                if (hyprlandWrite.isNotEmpty()) hyprlandSettings.add(hyprlandWrite.removeFirst())
            }
        }

        Path.of(pathToHyprland).writeText(text = hyprlandSettings.joinToString("\n"))

        WriteIntoHyprland().updateTime(hyprlandPath = pathToHyprland)
    }

}