package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.RulesForWindow
import org.hyprconfig.model.WindowRulesModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Window Rules Settings :")

private val windowRulesPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/windowRules.conf"

/**
 * This tries to process and create window rule settings.
 *
 * @param windows as list of string
 * @return list of [WindowRulesModel]
 */
internal fun handleWindowRules(windows: List<String>): List<WindowRulesModel> {

    logger.info("Try to process and create window rule settings")

    val windowStore = mutableListOf<WindowRulesModel>()

    windows.forEach {

        val processRules = it.split("#")[0].split("=").getOrNull(1)?.trim()?.split(",", limit = 2) ?: return@forEach

        val rules = processRules.getOrNull(0) ?: return@forEach

        val params = processRules.getOrNull(1)?.split(",")?.map { param -> param.trim() } ?: return@forEach

        windowStore.add(WindowRulesModel(rules, params))
    }

    logger.info("Creating windowRules hyprland file")

    val windowRule = mutableListOf<String>()

    handlePaths(windowRulesPath)

    windowStore.forEach {

//        var rule = ""
//
//        it.rules.forEach { rules ->
//            if (rule == "") {
//                rule = "${rules.keyword}${if (rules.value != null) " ${rules.value}" else ""}"
//            } else {
//                rule += " ${rules.keyword}${if (rules.value != null) " ${rules.value}" else ""}"
//            }
//        }

        windowRule.add("windowrule = ${it.rules.trim()} ,${it.params.joinToString(",")}".insertVariables())
    }

    Path.of(windowRulesPath).writeText(windowRule.joinToString("\n"))

    return windowStore.toList()
}

/**
 * There is a bug in here
 *
 * If there are two rules like suppressevent to maximize because maximize has it oven values, it will first match it and make 'suppressevent' this to remove from values braking it all.
 */
private fun String.validateRules(): List<RulesForWindow> {

    val rule = this.split(" ").map { it.trim() }

    val rulesStore = mutableListOf<RulesForWindow>()

//    rule.forEach {
////        when {
////            it.contains("float") -> rulesStore.add(RulesForWindow(keyword = "float"))
////            it.contains("tile") -> rulesStore.add(RulesForWindow(keyword = "tile"))
////            it.contains("fullscreen") -> rulesStore.add(RulesForWindow(keyword = "fullscreen"))
////            it.contains("maximize") -> rulesStore.add(RulesForWindow(keyword = "maximize"))
////            it.contains("persistentsize") -> rulesStore.add(RulesForWindow(keyword = "persistentsize"))
////            it.contains("fullscreenstate") -> {
////
////                val getIndex = it.indexOf("fullscreenstate")
////
////                val internal = it.getOrNull(getIndex + 1)
////
////                val client = it.getOrNull(getIndex + 2)
////
////                if (internal != null || client != null) rulesStore.add(
////                    RulesForWindow(
////                        keyword = "fullscreenstate",
////                        value = "$internal $client"
////                    )
////                )
////            }
////
////            it.contains("move") -> {
////                val getIndex = rule.indexOf("move")
////
////                val first = rule.getOrNull(getIndex + 1)
////
////                val second = rule.getOrNull(getIndex + 2)
////
////                val third = rule.getOrNull(getIndex + 3)
////
////                val fourth = rule.getOrNull(getIndex + 4)
////
////                if (first == "onscreen") {
////                    if (second == "cursor") {
////
////                        if (third != null && fourth != null) {
////                            val x = if (third.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) third else null
////                            val y = if (fourth.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) fourth else null
////
////                            if (x != null && y != null) rulesStore.add(
////                                RulesForWindow(
////                                    keyword = "move",
////                                    value = "$first $second $x $y"
////                                )
////                            )
////                        }
////                    }
////
////                    if (second != null && third != null) {
////                        val x = if (second.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) second else null
////                        val y = if (third.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) third else null
////
////                        if (x != null && y != null) rulesStore.add(RulesForWindow(keyword = "move", value = "$first $x $y"))
////                    }
////                } else if (first == "cursor") {
////                    if (second == "onscreen") {
////
////                        if (third != null && fourth != null) {
////                            val x = if (third.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) third else null
////                            val y = if (fourth.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) fourth else null
////
////                            if (x != null && y != null) rulesStore.add(
////                                RulesForWindow(
////                                    keyword = "move",
////                                    value = "$first $second $x $y"
////                                )
////                            )
////                        }
////                    }
////
////                    if (second != null && third != null) {
////                        val x = if (second.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) second else null
////                        val y = if (third.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) third else null
////
////                        if (x != null && y != null) rulesStore.add(RulesForWindow(keyword = "move", value = "$first $x $y"))
////                    }
////                } else if (first != null && second != null) {
////
////                    val x = if (first.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) first else null
////                    val y = if (second.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) second else null
////
////                    if (x != null && y != null) rulesStore.add(RulesForWindow(keyword = "move", value = "$x $y"))
////                }
////            }
////
////            it.contains("size") -> {
////                val getIndex = rule.indexOf("size")
////
////                val x = rule.getOrNull(getIndex + 1)
////
////                val y = rule.getOrNull(getIndex + 2)
////
////                if (x != null && y != null) {
////                    if (x.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)""")) && y.matches(Regex("""^(\d+(\.\d+)?%?|100%-w-\d+|100%-\d+)"""))) {
////                        rulesStore.add(RulesForWindow(keyword = "size", value = "$x $y"))
////                    }
////                }
////            }
////
////            it.contains("center") -> {
////                val getIndex = rule.indexOf("center")
////
////                val opt = rule.getOrNull(getIndex + 1)
////
////                if (opt != null) {
////                    rulesStore.add(RulesForWindow(keyword = "center", value = "$opt"))
////                } else {
////                    rulesStore.add(RulesForWindow(keyword = "center"))
////                }
////            }
////
////            it.contains("pseudo") -> rulesStore.add(RulesForWindow(keyword = "pseudo"))
////            it.contains("monitor") -> {
////                val getIndex = rule.indexOf("monitor")
////
////                val id = rule.getOrNull(getIndex + 1)
////
////                if (id != null) {
////                    rulesStore.add(RulesForWindow(keyword = "center", value = "$id"))
////                }
////            }
////
////            it.contains("workspace") -> {
////                val getIndex = rule.indexOf("workspace")
////
////                val workspace = rule.getOrNull(getIndex + 1)
////
////                if (workspace != null) {
////                    rulesStore.add(RulesForWindow(keyword = "workspace", value = "$workspace"))
////                }
////            }
////
////            it.contains("noinitialfocus") -> rulesStore.add(RulesForWindow(keyword = "noinitialfocus"))
////            it.contains("pin") -> rulesStore.add(RulesForWindow(keyword = "pin"))
////            it.contains("unset") -> rulesStore.add(RulesForWindow(keyword = "unset"))
////            it.contains("nomaxsize") -> rulesStore.add(RulesForWindow(keyword = "nomaxsize"))
////            it.contains("stayfocused") -> rulesStore.add(RulesForWindow(keyword = "stayfocused"))
////            it.contains("group") -> {
////                val getIndex = rule.indexOf("group")
////
////                val option = rule.getOrNull(getIndex + 1)
////
////                if (option != null) {
////                    rulesStore.add(RulesForWindow(keyword = "workspace", value = "$option"))
////                }
////            }
////
////            it.contains("suppressevent") -> {
////                val getIndex = rule.indexOf("suppressevent")
////
////                val option = rule.getOrNull(getIndex + 1)
////
////                if (option == "fullscreen" || option == "maximize" || option == "activate" || option == "activatefocus" || option == "fullscreenoutput") {
////                    rulesStore.add(RulesForWindow(keyword = "suppressevent", value = option))
////                }
////            }
////
////            it.contains("content") -> {
////                val getIndex = rule.indexOf("content")
////
////                val option = rule.getOrNull(getIndex + 1)
////
////                if (option != null) {
////                    rulesStore.add(RulesForWindow(keyword = "content", value = option))
////                }
////            }
////
////            it.contains("noclosefor") -> {
////                val getIndex = rule.indexOf("noclosefor")
////
////                val option = rule.getOrNull(getIndex + 1)
////
////                if (option != null) {
////                    rulesStore.add(RulesForWindow(keyword = "noclosefor", value = option))
////                }
////            }
////
////            it.contains("animation") -> {
////                val getIndex = rule.indexOf("animation")
////
////                val style = rule.getOrNull(getIndex + 1)
////
////                val options = rule.getOrNull(getIndex + 2) ?: ""
////
////                if (style != null) {
////                    rulesStore.add(RulesForWindow(keyword = "animation", value = "$style $options"))
////                }
////            }
////
////            it.contains("bordercolor") -> {
////                val getIndex = rule.indexOf("bordercolor")
////
////                val color = rule.getOrNull(getIndex + 1)
////
////                if (color != null) {
////                    rulesStore.add(RulesForWindow(keyword = "bordercolor", value = "$color"))
////                }
////            }
////
////            it.contains("idleinhibit") -> {
////                val getIndex = rule.indexOf("idleinhibit")
////
////                val idle = rule.getOrNull(getIndex + 1)
////
////                if (idle != null && idle == "none" || idle == "always" || idle == "focus" || idle == "fullscreen") {
////                    rulesStore.add(RulesForWindow(keyword = "idleinhibit", value = "$idle"))
////                }
////            }
////
////            it.contains("opacity") -> {
////                val getIndex = rule.indexOf("opacity")
////
////                val option = rule.getOrNull(getIndex + 1)
////
////                if (option != null) {
////                    rulesStore.add(RulesForWindow(keyword = "opacity", value = "$option"))
////                }
////            }
////
////            it.contains("tag") -> {
////                val getIndex = rule.indexOf("tag")
////
////                val name = rule.getOrNull(getIndex + 1)
////
////                if (name != null) {
////                    rulesStore.add(RulesForWindow(keyword = "tag", value = "$name"))
////                }
////            }
////
////            it.contains("maxsize") -> {
////                val getIndex = rule.indexOf("maxsize")
////
////                val x = rule.getOrNull(getIndex + 1)
////                val y = rule.getOrNull(getIndex + 2)
////
////                if (x != null && y != null) {
////                    rulesStore.add(RulesForWindow(keyword = "maxsize", value = "$x $y"))
////                }
////            }
////
////            it.contains("minsize") -> {
////                val getIndex = rule.indexOf("minsize")
////
////                val x = rule.getOrNull(getIndex + 1)
////                val y = rule.getOrNull(getIndex + 2)
////
////                if (x != null && y != null) {
////                    rulesStore.add(RulesForWindow(keyword = "minsize", value = "$x $y"))
////                }
////            }
////
////            it.contains("bordersize") -> {
////                val getIndex = rule.indexOf("bordersize")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "bordersize", value = size))
////                }
////            }
////
////            it.contains("rounding") -> {
////                val getIndex = rule.indexOf("rounding")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "rounding", value = "$size"))
////                }
////            }
////
////            it.contains("allowsinput") -> {
////                val getIndex = rule.indexOf("allowsinput")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "allowsinput", value = "$size"))
////                }
////            }
////
////            it.contains("dimaround") -> {
////                val getIndex = rule.indexOf("dimaround")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "dimaround", value = "$size"))
////                }
////            }
////
////            it.contains("decorate") -> {
////                val getIndex = rule.indexOf("decorate")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "decorate", value = "$size"))
////                }
////            }
////
////            it.contains("focusonactivate") -> {
////                val getIndex = rule.indexOf("focusonactivate")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "focusonactivate", value = "$size"))
////                }
////            }
////
////            it.contains("keepaspectratio") -> {
////                val getIndex = rule.indexOf("keepaspectratio")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "keepaspectratio", value = "$size"))
////                }
////            }
////
////            it.contains("nearestneighbor") -> {
////                val getIndex = rule.indexOf("nearestneighbor")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "nearestneighbor", value = "$size"))
////                }
////            }
////
////            it.contains("noanim") -> {
////                val getIndex = rule.indexOf("noanim")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "noanim", value = "$size"))
////                }
////            }
////
////            it.contains("noblur") -> {
////                val getIndex = rule.indexOf("noblur")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "noblur", value = "$size"))
////                }
////            }
////
////            it.contains("noborder") -> {
////                val getIndex = rule.indexOf("noborder")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "noborder", value = "$size"))
////                }
////            }
////
////            it.contains("nodim") -> {
////                val getIndex = rule.indexOf("nodim")
////
////                val size = rule.getOrNull(getIndex + 1)
////
////                if (size != null) {
////                    rulesStore.add(RulesForWindow(keyword = "nodim", value = "$size"))
////                }
////            }
////
////            it.contains("nofocus") -> {
////                val index = rule.indexOf("nofocus")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "nofocus", value = value))
////                }
////            }
////
////            it.contains("nofollowmouse") -> {
////                val index = rule.indexOf("nofollowmouse")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "nofollowmouse", value = value))
////                }
////            }
////
////            it.contains("nomaxsize") -> {
////                val index = rule.indexOf("nomaxsize")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "nomaxsize", value = value))
////                }
////            }
////
////            it.contains("norounding") -> {
////                val index = rule.indexOf("norounding")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "norounding", value = value))
////                }
////            }
////
////            it.contains("noshadow") -> {
////                val index = rule.indexOf("noshadow")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "noshadow", value = value))
////                }
////            }
////
////            it.contains("noshortcutsinhibit") -> {
////                val index = rule.indexOf("noshortcutsinhibit")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "noshortcutsinhibit", value = value))
////                }
////            }
////
////            it.contains("opaque") -> {
////                val index = rule.indexOf("opaque")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "opaque", value = value))
////                }
////            }
////
////            it.contains("forcergbx") -> {
////                val index = rule.indexOf("forcergbx")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "forcergbx", value = value))
////                }
////            }
////
////            it.contains("syncfullscreen") -> {
////                val index = rule.indexOf("syncfullscreen")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "syncfullscreen", value = value))
////                }
////            }
////
////            it.contains("immediate") -> {
////                val index = rule.indexOf("immediate")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "immediate", value = value))
////                }
////            }
////
////            it.contains("xray") -> {
////                val index = rule.indexOf("xray")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "xray", value = value))
////                }
////            }
////
////            it.contains("renderunfocused") -> rulesStore.add(RulesForWindow(keyword = "renderunfocused"))
////
////            it.contains("scrollmouse") -> {
////                val index = rule.indexOf("scrollmouse")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "scrollmouse", value = value))
////                }
////            }
////
////            it.contains("scrolltouchpad") -> {
////                val index = rule.indexOf("scrolltouchpad")
////                rule.getOrNull(index + 1)?.let { value ->
////                    rulesStore.add(RulesForWindow(keyword = "scrolltouchpad", value = value))
////                }
////            }
////        }
//    }

    rulesStore.forEach { logger.info(it.toString()) }

    return rulesStore.toList()
}
