package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.dot.config.controller.ui.SidebarController
import org.dot.config.model.SendAndReceive
import org.dot.config.model.Tables
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class WindowController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val path = "${System.getProperty("user.home")}/.dot.config/data/windowRules.csv"

    fun getWindowRules(rule: SendAndReceive.WindowGetRules): List<SendAndReceive.WindowSendRules> {
        logger.info("Get $rule Rules")

        when (rule) {
            SendAndReceive.WindowGetRules.STATIC -> {

                val static =
                    DataFrame.readCsv(object {}.javaClass.getResourceAsStream("/dispatchers/window/staticRules.csv")!!)
                        .convert { all() }.with { rules -> rules.toString() }

                val staticRules = mutableListOf<SendAndReceive.WindowSendRules>()

                static.forEach { row ->
                    staticRules.add(
                        SendAndReceive.WindowSendRules(
                            name = row["name"].toString(),
                            actionName = row["actionName"].toString(),
                            description = row["description"].toString(),
                            actionSupport = row["actionSupport"].toString(),
                            help = row["help"].toString(),
                        )
                    )
                }

                return staticRules.toList()
            }

            SendAndReceive.WindowGetRules.DYNAMIC -> {
                val dynamic =
                    DataFrame.readCsv(object {}.javaClass.getResourceAsStream("/dispatchers/window/dynamicRules.csv")!!)
                        .convert { all() }.with { rules -> rules.toString() }

                val dynamicRules = mutableListOf<SendAndReceive.WindowSendRules>()

                dynamic.forEach { row ->
                    dynamicRules.add(
                        SendAndReceive.WindowSendRules(
                            name = row["name"].toString(),
                            actionName = row["actionName"].toString(),
                            description = row["description"].toString(),
                            actionSupport = row["actionSupport"].toString(),
                            help = row["help"].toString(),
                        )
                    )
                }

                return dynamicRules.toList()
            }

            SendAndReceive.WindowGetRules.PARAMS -> {
                val props =
                    DataFrame.readCsv(object {}.javaClass.getResourceAsStream("/dispatchers/window/props.csv")!!)
                        .convert { all() }.with { rules -> rules.toString() }

                val propsRules = mutableListOf<SendAndReceive.WindowSendRules>()

                props.forEach { row ->
                    propsRules.add(
                        SendAndReceive.WindowSendRules(
                            name = row["name"].toString(),
                            actionName = row["actionName"].toString(),
                            description = row["description"].toString(),
                            actionSupport = row["actionSupport"].toString(),
                            help = row["help"].toString(),
                        )
                    )
                }

                return propsRules.toList()
            }
        }
    }

    fun addNew(window: Tables.WindowRules): Boolean {
        logger.info("Add New Window Rule")

        val windowRules = SidebarController().getWindow().toMutableList()

        windowRules.add(window)

        val windowRuleDF = mutableListOf<Tables.WindowRulesDataFrame>()

        windowRules.forEach {

            val rules = mutableListOf<String>()

            it.rules.forEach { rule ->
                val ruleString = "${rule.name}${if (rule.value != null) " ${rule.value}" else ""}"
                rules.add(ruleString)
            }

            windowRuleDF.add(
                Tables.WindowRulesDataFrame(
                    rules = rules,
                    params = it.params
                )
            )
        }

        writeAllToCsv(window = windowRuleDF.toDataFrame().convert { all() }.with { it.toString() })
        writeIntoHyprland(window = windowRules.toList())

        return true
    }

    fun edit(newWindow: Tables.WindowRules, oldWindow: Tables.WindowRules): Boolean {
        logger.info("Edit Window Rules")

        val windowRules = SidebarController().getWindow().toMutableList()

        val editWindow = mutableListOf<Tables.WindowRules>()

        windowRules.forEach {
            if (it.params == oldWindow.params && it.rules == oldWindow.rules) {
                editWindow.add(newWindow)
            } else {
                editWindow.add(it)
            }
        }

        val windowRuleDF = mutableListOf<Tables.WindowRulesDataFrame>()

        editWindow.forEach {

            val rules = mutableListOf<String>()

            it.rules.forEach { rule ->
                val ruleString = "${rule.name}${if (rule.value != null) " ${rule.value}" else ""}"
                rules.add(ruleString)
            }

            windowRuleDF.add(
                Tables.WindowRulesDataFrame(
                    rules = rules,
                    params = it.params
                )
            )
        }

        writeAllToCsv(window = windowRuleDF.toDataFrame().convert { all() }.with { it.toString() })
        writeIntoHyprland(window = editWindow.toList())

        return true
    }

    fun delete(window: Tables.WindowRules): Boolean {
        logger.info("Delete Window Rules")

        val windowRules = SidebarController().getWindow().toMutableList()

        val delete = windowRules.filter { !(it.rules == window.rules && it.params == window.params) }

        val windowRuleDF = mutableListOf<Tables.WindowRulesDataFrame>()

        delete.forEach {

            val rules = mutableListOf<String>()

            it.rules.forEach { rule ->
                val ruleString = "${rule.name}${if (rule.value != null) " ${rule.value}" else ""}"
                rules.add(ruleString)
            }

            windowRuleDF.add(
                Tables.WindowRulesDataFrame(
                    rules = rules,
                    params = it.params
                )
            )
        }

        writeAllToCsv(window = windowRuleDF.toDataFrame().convert { all() }.with { it.toString() })
        writeIntoHyprland(window = delete.toList())

        return true
    }

    private fun writeAllToCsv(window: DataFrame<*>) {
        window.writeCsv(path)
    }

    private fun writeIntoHyprland(window: List<Tables.WindowRules>) {

        logger.info("Write Window Rules Into Hyprland")

        val value = WriteIntoHyprland().writeWindow(window = window)

        val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/windowRules.conf"

        Path.of(hyprlandPath).writeText(text = value)

        WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
    }
}