package org.hyprconfig.categories

import org.hyprconfig.model.CategoriesModel
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Categories settings :")

/**
 * This will separate hyprland settings into different categories
 *
 * @param allSettings as list of strings
 * @return [CategoriesModel] as categories' settings
 */
internal fun categoriesSettings(allSettings : List<String>) : CategoriesModel {

    logger.info("Try to categories settings")

    val categories = CategoriesModel()

    val newSettings = allSettings.toMutableList()

    val hyprlandCategories = mutableListOf<String>()

    var hyprlandCategoriesCount = 0

    newSettings.forEachIndexed { index ,settings ->

        if (settings.replace(" " ,"").contains('{')) {
            hyprlandCategoriesCount ++
            hyprlandCategories.add(settings)
            newSettings[index] = ""
            return@forEachIndexed
        }


        if (settings.replace(" " ,"").contains("}")) {
            hyprlandCategoriesCount --
            hyprlandCategories.add(settings)
            newSettings[index] = ""
            return@forEachIndexed
        }

        if (hyprlandCategoriesCount > 0) {
            hyprlandCategories.add(settings)
            newSettings[index] = ""
        }
    }

    logger.info("Category hyprland settings and keyword hyprland settings were separated.")

    logger.info("Categories keyword hyprland settings")

    newSettings.forEach {

        if (it == "") return@forEach

        when {
            it.startsWith("bind") -> categories.bind.add(it)
            it.startsWith("monitor") -> categories.monitor.add(it)
            it.startsWith("unbind") -> categories.unbind.add(it)
            it.startsWith("workspace") -> categories.workspace.add(it)
            it.startsWith("windowrule") -> categories.windowrule.add(it)
            it.startsWith("layerrule") -> categories.layerrule.add(it)
            it.startsWith("bezier") -> categories.bezier.add(it)
            it.startsWith("animation") -> categories.animation.add(it)
            it.startsWith("source") -> categories.source.add(it)
            it.startsWith("submap") -> categories.submap.add(it)
            it.startsWith("plugin") -> categories.plugin.add(it)
            it.startsWith("permission") -> categories.permission.add(it)
            it.startsWith("env") -> categories.env.add(it)
            it.startsWith("exec") -> categories.executes.add(it)
        }
    }

    val hyprlandCategory = mutableListOf<String>()

    logger.info("Categories hyprland category settings")

    hyprlandCategories.forEach {

        if (it.replace(" " ,"").contains('{')) {
            hyprlandCategory.add(it.replace(" ", "").trim().dropLast(1))
            return@forEach
        }

        if (it.replace(" " ,"").trim().contains("}")) {
            if (hyprlandCategory.isNotEmpty()) {
                hyprlandCategory.removeLast()
            }
            return@forEach
        }

        val processCategorySettings = it.trim().split("#")[0].split("=").map { value -> value.trim() }

        val key = processCategorySettings.getOrNull(0) ?: return@forEach

        val value = processCategorySettings.getOrNull(1) ?: return@forEach

        when {
            key.startsWith("bind") -> categories.bind.add(it)
            key.startsWith("monitor") -> categories.monitor.add(it)
            key.startsWith("unbind") -> categories.unbind.add(it)
            key.startsWith("workspace") -> categories.workspace.add(it)
            key.startsWith("windowrule") -> categories.windowrule.add(it)
            key.startsWith("layerrule") -> categories.layerrule.add(it)
            key.startsWith("bezier") -> categories.bezier.add(it)
            key.startsWith("animation") -> categories.animation.add(it)
            key.startsWith("source") -> categories.source.add(it)
            key.startsWith("submap") -> categories.submap.add(it)
            key.startsWith("plugin") -> categories.plugin.add(it)
            key.startsWith("permission") -> categories.permission.add(it)
            key.startsWith("env") -> categories.env.add(it)
            key.startsWith("exec") -> categories.executes.add(it)
        }

        when (hyprlandCategory[0]) {

            "general" -> categories.general.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "misc" -> categories.misc.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "group" -> categories.group.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "debug" -> categories.debug.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "decoration" -> categories.decoration.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "dwindle" -> categories.dwindle.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "master" -> categories.master.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "animations" -> categories.animations.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "input" -> categories.input.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "binds" -> categories.binds.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "gestures" -> categories.gestures.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "xwayland" -> categories.xwayland.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "opengl" -> categories.opengl.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "cursor" -> categories.cursor.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "autogenerated" -> categories.autogenerated.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "render" -> categories.render.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "ecosystem" -> categories.ecosystem.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "experimental" -> categories.experimental.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")

            "device" -> categories.device.add(hyprlandCategory.joinToString(":") + ":=${key}=${value}")
        }
    }

    return categories
}