package controllers

import RequestPaths
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import logger
import model.HyprlandKeywords
import model.HyprlandTypes
import model.ParsedModels
import model.ParsingStep
import model.ParsingSteps
import model.hyprlandTypeCheck
import model.tables.StandedKeywordModel
import model.tables.StandedKeywordParseModel
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.readCsv
import write.keyword.WriteKeyword
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Parses a list of settings and categorizes them into a store of keywords.
 * It organizes settings into various categories based on pre-defined keyword hierarchies
 * and processes them according to their respective structures and values. The categorized
 * settings are subsequently available for further processing.
 *
 * This is used for any settings with a keyword.
 * Ex: {general: {name: "foo"}}, {group: {name: "foo"}}, {debug: {name: "foo"}}
 *
 * @param allSettings a list of strings representing all settings to be parsed and categorized
 * @param session an instance of [DefaultWebSocketServerSession] used for server communication
 */
internal suspend fun parseKeywords(allSettings: List<String> ,session: DefaultWebSocketServerSession) {

    logger.info("Try to parse keywords")

    val hyprlandCategories = mutableListOf<String>()

    val hyprlandKeywordsStore = HyprlandKeywords()

    var hyprlandCategoriesCount = 0

    allSettings.forEachIndexed { index, settings ->

        if (settings.replace(" ", "").contains('{')) {
            hyprlandCategoriesCount++
            hyprlandCategories.add(settings)
            return@forEachIndexed
        }


        if (settings.replace(" ", "").contains("}")) {
            hyprlandCategoriesCount--
            hyprlandCategories.add(settings)
            return@forEachIndexed
        }

        if (hyprlandCategoriesCount > 0) {
            hyprlandCategories.add(settings)
        }
    }

    var keyword = ""

    hyprlandCategories.forEach {

        if (it.contains("{")) {
            keyword = if (keyword == "") {
                it.dropLast(1)
            } else {
                "$keyword:${it.dropLast(1)}"
            }
            return@forEach
        }

        if (it.contains("}")) {
            keyword = keyword.split(":").dropLast(1).joinToString(":")
            return@forEach
        }

        val (key, value) = it.split("=")

        val searchKeyword = keyword.split(":")

        when (searchKeyword[0]) {
            "general" -> hyprlandKeywordsStore.general.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "misc" -> hyprlandKeywordsStore.misc.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "group" -> hyprlandKeywordsStore.group.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = if (searchKeyword.size == 1) searchKeyword[0] else "groupBar"
                )
            )

            "debug" -> hyprlandKeywordsStore.debug.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "decoration" -> hyprlandKeywordsStore.decoration.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "dwindle" -> hyprlandKeywordsStore.dwindle.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "master" -> hyprlandKeywordsStore.master.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "animations" -> hyprlandKeywordsStore.animations.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "input" -> hyprlandKeywordsStore.inputs.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() }
                        .let { input ->
                            if (input != "input") {
                                input.replace("input", "inputs")
                            } else {
                                input
                            }
                        }
                        .let { input ->
                            if (input == "inputsTouchdevice") {
                                return@let "inputsTouchDevice"
                            } else {
                                return@let input
                            }
                        }
                )
            )

            "binds" -> hyprlandKeywordsStore.binds.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "gestures" -> hyprlandKeywordsStore.gestures.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "xwayland" -> hyprlandKeywordsStore.xwayland.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "opengl" -> hyprlandKeywordsStore.openGL.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = "openGl"
                )
            )

            "cursor" -> hyprlandKeywordsStore.cursor.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "render" -> hyprlandKeywordsStore.render.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "ecosystem" -> hyprlandKeywordsStore.ecosystem.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "experimental" -> hyprlandKeywordsStore.experimental.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            "device" -> hyprlandKeywordsStore.device.add(
                StandedKeywordParseModel(
                    name = key,
                    value = value,
                    fileName = searchKeyword.joinToString("") { first -> first.replaceFirstChar { ch -> ch.uppercase() } }
                        .replaceFirstChar { ch -> ch.lowercase() })
            )

            else -> {
                logger.warn("Unsupported Keyword \n $it")
            }
        }
    }

    val request = RequestPaths()

    hyprlandKeywordsStore::class.memberProperties.filterIsInstance<KProperty1<HyprlandKeywords, MutableList<StandedKeywordParseModel>>>()
        .forEach { k ->
            val paths = request.getPathsForKeyword(k.name)

            val list = k.get(hyprlandKeywordsStore)

            val validDataModel = mutableListOf<Pair<StandedKeywordModel, StandedKeywordParseModel>>()

            paths?.forEach {
                val defaults = Path.of("${request.getHyprlandKeywordPath()}${it}.csv")

                val readSettings = mutableListOf<StandedKeywordModel>()

                if (defaults.exists()) {
                    val df = DataFrame.readCsv(defaults).convert { all() }.with { d -> d.toString() }

                    df.forEach { row ->
                        readSettings.add(
                            StandedKeywordModel(
                                name = row["settingsName"].toString(),
                                value = row["value"].toString(),
                                type = HyprlandTypes.valueOf(row["type"].toString().uppercase())
                            )
                        )
                    }
                }

                val names = readSettings.associateBy { value -> value.name }.toMutableMap()

                list.forEachIndexed { index, model ->
                    names
                        .filter { nameof -> nameof.key == model.name.trim() }
                        .takeIf { list -> list.isNotEmpty() }
                        ?.let { list ->
                            val valueOfMap = list[model.name.trim()] ?: return@forEachIndexed

                            val validValue = model.value.hyprlandTypeCheck(valueOfMap.type) ?: return@forEachIndexed

                            validDataModel.add(
                                Pair(
                                    StandedKeywordModel(
                                        name = valueOfMap.name,
                                        value = validValue,
                                        type = valueOfMap.type
                                    ),
                                    model
                                )
                            )
                        }
                }
            }

            val write = WriteKeyword(listOf(k.name))

            runCatching {
                write.writeIntoHyprland(validDataModel).getOrThrow()
                write.writeIntoDotConfig(validDataModel).getOrThrow()

                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All ${k.name}",
                            success = true,
                            description = "Parse and write all ${k.name} to hyprland source file and dot.config.hyprland store.",
                            found = validDataModel.size
                        )
                    )
                )
            }.onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All ${k.name}",
                            success = false,
                            description = "Parse and write all ${k.name} to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return
            }

        }
}