package controllers

import RequestPaths
import logger
import model.tables.RulesForWindow
import model.tables.WindowRulesModel
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.associateBy
import org.jetbrains.kotlinx.dataframe.io.readCsv

private val requestPaths = RequestPaths()

private val staticRules = DataFrame.readCsv(requestPaths.getWindowRules("STATIC")).associateBy { it["name"] }
private val dynamicRules = DataFrame.readCsv(requestPaths.getWindowRules("DYNAMIC")).associateBy { it["name"] }
private val params = DataFrame.readCsv(requestPaths.getWindowRules("")).associateBy { it["name"] }

/**
 * This tries to process and create window rule settings.
 *
 * @param windows as list of string
 * @return list of [WindowRulesModel]
 */
internal fun parseWindowRules(windows: List<String>): List<WindowRulesModel> {

    logger.info("Try to process and create window rule settings")

    val windowStore = mutableListOf<WindowRulesModel>()

    windows.forEach {

        if (!it.startsWith("windowrule")) return@forEach

        val processRules = it.split("#")[0].split("=").getOrNull(1)?.trim()?.split(",", limit = 2) ?: return@forEach

        val rules = processRules.getOrNull(0)?.validateRules() ?: return@forEach

        val params = processRules.getOrNull(1)?.split(",")?.map { param -> param.trim() } ?: return@forEach

        windowStore.add(WindowRulesModel(rules, params))
    }

    windowStore.forEach { logger.info(it.toString()) }

//    logger.info("Creating windowRules hyprland file")
//
//    val windowRule = mutableListOf<String>()
//
//    handlePaths(windowRulesPath)
//
//    windowStore.forEach {
//
////        var rule = ""
////
////        it.rules.forEach { rules ->
////            if (rule == "") {
////                rule = "${rules.keyword}${if (rules.value != null) " ${rules.value}" else ""}"
////            } else {
////                rule += " ${rules.keyword}${if (rules.value != null) " ${rules.value}" else ""}"
////            }
////        }
//
//        windowRule.add("windowrule = ${it.rules.trim()} ,${it.params.joinToString(",")}".insertVariables())
//    }
//
//    Path.of(windowRulesPath).writeText(windowRule.joinToString("\n"))

    return windowStore.toList()
}


private fun String.validateRules(): List<RulesForWindow> {

    val rule = this.split(" ").map { it.trim() }

    val rulesStore = mutableListOf<RulesForWindow>()
    var lastAdded = ""

    rule.forEach {
        if (staticRules[it] != null) {
            lastAdded = "STATIC"
            rulesStore.add(RulesForWindow(
                keyword = it,
                value = null
            ))
            return@forEach
        } else if (dynamicRules[it] != null) {
            lastAdded = "DYNAMIC"
            rulesStore.add(RulesForWindow(
                keyword = it,
                value = null
            ))
        } else if (params[it] != null) {
            lastAdded = "PARAMS"
            rulesStore.add(RulesForWindow(
                keyword = it,
                value = null
            ))
        } else {

            when (lastAdded) {
                "STATIC" -> {
                    val static = staticRules[rulesStore.last().keyword]

                    if (static?.get("actionSupport").toString() != "null") {
                        if (rulesStore.last().value == null) {
                            rulesStore.last().value = it
                        } else {
                            rulesStore.last().value = "${rulesStore.last().value} $it"
                        }
                    } else {
                        logger.warn("Unsupported static rules ${rulesStore.last().keyword} for $it")
                    }
                }

                "DYNAMIC" -> {
                    val dynamic = dynamicRules[rulesStore.last().keyword]

                    if (dynamic?.get("actionSupport").toString() != "null") {
                        if (rulesStore.last().value == null) {
                            rulesStore.last().value = it
                        } else {
                            rulesStore.last().value = "${rulesStore.last().value} $it"
                        }
                    } else {
                        logger.warn("Unsupported dynamic rules ${rulesStore.last().keyword} for $it")
                    }
                }

                else -> {
                    val params = params[rulesStore.last().keyword]

                    if (params?.get("actionSupport").toString() != "null") {
                        if (rulesStore.last().value == null) {
                            rulesStore.last().value = it
                        } else {
                            rulesStore.last().value = "${rulesStore.last().value} $it"
                        }
                    } else {
                        logger.warn("Unsupported params rules ${rulesStore.last().keyword} for $it")
                    }
                }
            }
        }
    }

    return rulesStore.toList()
}
