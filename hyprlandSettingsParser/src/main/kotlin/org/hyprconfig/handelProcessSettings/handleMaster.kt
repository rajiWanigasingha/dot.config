package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.MasterModel
import org.hyprconfig.model.defaultMasterSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Master Settings :")

private val masterPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/master.conf"

internal fun handleMaster(master: List<String>): List<MasterModel> {

    logger.info("Try to process and create master layout hyprland settings.")

    val masterStore = defaultMasterSettings

    master.forEach {
        val processMaster = it.split(":=").map { master -> master.trim() }

        val name = processMaster.getOrNull(0) ?: return@forEach

        val value = processMaster.getOrNull(1)?.split("=")?.map { master -> master.trim() } ?: return@forEach

        val masterName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val masterValue = value.getOrNull(1) ?: return@forEach

        masterStore.forEach innerLoop@{ master ->
            if (master.name == masterName) {

                val validateMaster = masterValue.hyprlandTypeCheck(master.type) ?: return@forEach

                if (validateMaster != master.value) master.value = validateMaster
            }
        }
    }

    logger.info("Creating master layout settings.")

    handlePaths(masterPath)

    val masterLayoutSettings = generateMasterLayoutSettings(masterStore)

    Path.of(masterPath).writeText(masterLayoutSettings)

    return masterStore.toList()
}

private fun generateMasterLayoutSettings(master: List<MasterModel>): String {
    return """
        master {
            special_scale_factor = ${master.find { it.name == "master:special_scale_factor" }!!.value}
            mfact = ${master.find { it.name == "master:mfact" }!!.value}
            new_status = ${master.find { it.name == "master:new_status" }!!.value}
            slave_count_for_center_master = ${master.find { it.name == "master:slave_count_for_center_master" }!!.value}
            center_master_fallback = ${master.find { it.name == "master:center_master_fallback" }!!.value}
            center_ignores_reserved = ${master.find { it.name == "master:center_ignores_reserved" }!!.value}
            new_on_active = ${master.find { it.name == "master:new_on_active" }!!.value}
            new_on_top = ${master.find { it.name == "master:new_on_top" }!!.value}
            orientation = ${master.find { it.name == "master:orientation" }!!.value}
            inherit_fullscreen = ${master.find { it.name == "master:inherit_fullscreen" }!!.value}
            allow_small_split = ${master.find { it.name == "master:allow_small_split" }!!.value}
            smart_resizing = ${master.find { it.name == "master:smart_resizing" }!!.value}
            drop_at_cursor = ${master.find { it.name == "master:drop_at_cursor" }!!.value}
            always_keep_position = ${master.find { it.name == "master:always_keep_position" }!!.value}
        }
    """.insertVariables().trimIndent()
}