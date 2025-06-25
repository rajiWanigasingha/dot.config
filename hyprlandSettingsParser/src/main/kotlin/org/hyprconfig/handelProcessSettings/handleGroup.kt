package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.GroupModel
import org.hyprconfig.model.defaultGroupSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel group Settings :")

private val groupPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/group.conf"

/**
 * Use to process and create groups settings for hyprland
 *
 * @param groups list of string
 * @return list of [GroupModel]
 */
internal fun handleGroup(groups: List<String>): List<GroupModel> {

    logger.info("Try to process and creating hyprland group")

    val groupStore = defaultGroupSettings

    groups.forEach {

        val processGroup = it.split(":=").map { group -> group.trim() }

        val name = processGroup.getOrNull(0) ?: return@forEach

        val value = processGroup.getOrNull(1)?.split("=")?.map { group -> group.trim() } ?: return@forEach

        val groupName = "$name:${value.getOrNull(0) ?: return@forEach}".trim()

        val groupValue = value.getOrNull(1)?.trim() ?: return@forEach

        groupStore.forEach { group ->

            if (group.name == groupName) {

                val validateGroup = groupValue.hyprlandTypeCheck(group.type)

                if (validateGroup == group.name) group.value = validateGroup
            }
        }
    }

    logger.info("Creating hyprland group settings file")

    handlePaths(groupPath)

    val groupSettings = generateGroupSettings(groupStore)

    Path.of(groupPath).writeText(groupSettings)

    return groupStore.toList()
}

private fun generateGroupSettings(groupSettings: List<GroupModel>): String {

    return """
        group {
            insert_after_current = ${groupSettings.find { it.name == "group:insert_after_current" }!!.value}
            focus_removed_window = ${groupSettings.find { it.name == "group:focus_removed_window" }!!.value}
            merge_groups_on_drag = ${groupSettings.find { it.name == "group:merge_groups_on_drag" }!!.value}
            merge_groups_on_groupbar = ${groupSettings.find { it.name == "group:merge_groups_on_groupbar" }!!.value}
            merge_floated_into_tiled_on_groupbar = ${groupSettings.find { it.name == "group:merge_floated_into_tiled_on_groupbar" }!!.value}
            auto_group = ${groupSettings.find { it.name == "group:auto_group" }!!.value}
            drag_into_group = ${groupSettings.find { it.name == "group:drag_into_group" }!!.value}
            group_on_movetoworkspace = ${groupSettings.find { it.name == "group:group_on_movetoworkspace" }!!.value}
            col.border_active = ${groupSettings.find { it.name == "group:col.border_active" }!!.value}
            col.border_inactive = ${groupSettings.find { it.name == "group:col.border_inactive" }!!.value}
            col.border_locked_active = ${groupSettings.find { it.name == "group:col.border_locked_active" }!!.value}
            col.border_locked_inactive = ${groupSettings.find { it.name == "group:col.border_locked_inactive" }!!.value}
            groupbar {
                enabled = ${groupSettings.find { it.name == "group:groupbar:enabled" }!!.value}
                font_family = ${groupSettings.find { it.name == "group:groupbar:font_family" }!!.value}
                font_weight_active = ${groupSettings.find { it.name == "group:groupbar:font_weight_active" }!!.value}
                font_weight_inactive = ${groupSettings.find { it.name == "group:groupbar:font_weight_inactive" }!!.value}
                font_size = ${groupSettings.find { it.name == "group:groupbar:font_size" }!!.value}
                gradients = ${groupSettings.find { it.name == "group:groupbar:gradients" }!!.value}
                height = ${groupSettings.find { it.name == "group:groupbar:height" }!!.value}
                indicator_gap = ${groupSettings.find { it.name == "group:groupbar:indicator_gap" }!!.value}
                indicator_height = ${groupSettings.find { it.name == "group:groupbar:indicator_height" }!!.value}
                priority = ${groupSettings.find { it.name == "group:groupbar:priority" }!!.value}
                render_titles = ${groupSettings.find { it.name == "group:groupbar:render_titles" }!!.value}
                scrolling = ${groupSettings.find { it.name == "group:groupbar:scrolling" }!!.value}
                text_color = ${groupSettings.find { it.name == "group:groupbar:text_color" }!!.value}
                stacked = ${groupSettings.find { it.name == "group:groupbar:stacked" }!!.value}
                rounding = ${groupSettings.find { it.name == "group:groupbar:rounding" }!!.value}
                gradient_rounding = ${groupSettings.find { it.name == "group:groupbar:gradient_rounding" }!!.value}
                round_only_edges = ${groupSettings.find { it.name == "group:groupbar:round_only_edges" }!!.value}
                gradient_round_only_edges = ${groupSettings.find { it.name == "group:groupbar:gradient_round_only_edges" }!!.value}
                gaps_out = ${groupSettings.find { it.name == "group:groupbar:gaps_out" }!!.value}
                gaps_in = ${groupSettings.find { it.name == "group:groupbar:gaps_in" }!!.value}
                keep_upper_gap = ${groupSettings.find { it.name == "group:groupbar:keep_upper_gap" }!!.value}
                col.active = ${groupSettings.find { it.name == "group:groupbar:col.active" }!!.value}
                col.inactive = ${groupSettings.find { it.name == "group:groupbar:col.inactive" }!!.value}
                col.locked_active = ${groupSettings.find { it.name == "group:groupbar:col.locked_active" }!!.value}
                col.locked_inactive = ${groupSettings.find { it.name == "group:groupbar:col.locked_inactive" }!!.value}
            }
        }
    """.insertVariables().trimIndent()

}