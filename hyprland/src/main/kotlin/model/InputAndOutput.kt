package org.dot.config.model

import kotlinx.serialization.Serializable
import org.dot.config.view.basicComponents.InputComponents
import org.dot.config.view.builderComponents.Sidebar
import org.hyprconfig.helpers.HyprlandTypes
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.io.ColType
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.slf4j.LoggerFactory

object InputAndOutput {

    private val path = "${System.getProperty("user.home")}/.dot.config/data"

    private val logger = LoggerFactory.getLogger(javaClass.name)

    fun buildUITableCsv(actionLink: Sidebar.ActionLinks, pathToSettings: List<String>): List<Helpers.TableCsv> {

        logger.info("Get All Settings From $actionLink And Path From ${pathToSettings.joinToString(" ,")}")

        val allSettings = mutableListOf<Helpers.TableCsv>()

        pathToSettings.forEach {

            val input = DataFrame.readCsv("$path/$it", colTypes = mapOf("validate" to ColType.String))

            val categoryInputs = input.filter { row -> row["actionLink"] == "$actionLink" }

            categoryInputs.forEach { row ->
                allSettings.add(
                    Helpers.TableCsv(
                        category = row["category"].toString(),
                        displayName = row["displayName"].toString(),
                        settingsName = row["settingsName"].toString(),
                        description = row["description"].toString(),
                        type = row["type"].toString(),
                        value = row["value"]?.toString() ?: "",
                        actionLink = row["actionLink"].toString(),
                        tab = row["tab"].toString(),
                        method = row["method"].toString(),
                        validate = row["validate"].toString(),
                        special = row["special"].toString()
                    )
                )
            }
        }

        return allSettings.toList()
    }

    @Serializable
    data class SettingsUI(
        val inputUI: InputComponents.InputShouldHave,
        val data: Tables.StandedHyprlandLangTable
    )

    @Serializable
    data class StandedCategoryBuildUI(
        val tab: String,
        val settings: MutableList<SettingsUI>
    )

    @Serializable
    data class UpdateSettingsStandedCategories(
        val name: String,
        val value: String,
        val type: HyprlandTypes,
        val category: String
    )
}