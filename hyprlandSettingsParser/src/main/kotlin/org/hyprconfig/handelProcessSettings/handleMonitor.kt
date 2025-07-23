package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.AddReserved
import org.hyprconfig.model.MonitorModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Monitor Settings :")

private val monitorPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/monitor.conf"

/**
 * This uses to process and store monitor hyprland settings.
 *
 * @param monitors as list of hyprland settings strings
 * @return list of [MonitorModel]
 */
internal fun handleMonitors(monitors: List<String>): List<MonitorModel> {

    val monitorStore = mutableListOf<MonitorModel>()

    logger.info("Try to process and create hyprland monitor settings")

    monitors.forEach {

        val monitor = MonitorModel()

        val processMonitor = it.split("#")[0].split("=")

        val monitorValues =
            processMonitor.getOrNull(1)?.trim()?.split(",")?.map { value -> value.trim() } ?: return@forEach

        monitor.name = monitorValues.getOrNull(0)?.trim() ?: return@forEach

        val secondArg = monitorValues.getOrNull(1)?.trim() ?: return@forEach

        if (secondArg == "disable" || secondArg == "disabled" || secondArg == "addreserved") {
            when (secondArg) {
                "disable", "disabled" -> monitor.disable = true

                "addreserved" -> {
                    monitor.addreserved = true

                    val monitorTop = monitorValues.getOrNull(2)?.toInt() ?: return@forEach
                    val monitorBottom = monitorValues.getOrNull(3)?.toInt() ?: return@forEach
                    val monitorLeft = monitorValues.getOrNull(4)?.toInt() ?: return@forEach
                    val monitorRight = monitorValues.getOrNull(5)?.toInt() ?: return@forEach

                    monitor.addreservedValue = AddReserved(monitorTop, monitorBottom, monitorLeft, monitorRight)
                }
            }

            monitorStore.add(monitor)

            return@forEach
        }

        monitor.resolution = secondArg.validateRes() ?: return@forEach

        monitor.position = monitorValues.getOrNull(2)?.trim()?.validatePosition() ?: return@forEach

        monitor.scale = monitorValues.getOrNull(3)?.trim()?.validateScale() ?: return@forEach

        val restOfSettings = if (monitorValues.size > 4) monitorValues.subList(4, monitorValues.size) else null

        if (restOfSettings.isNullOrEmpty()) {
            monitorStore.add(monitor)
            return@forEach
        }

        restOfSettings.forEachIndexed { index: Int, extra: String ->

            val monitorExtraValue = restOfSettings.getOrNull(index + 1)

            when (extra) {
                "mirror" -> {
                    monitor.mirror = monitorExtraValue ?: return@forEach
                }

                "bitdepth" -> {
                    monitor.bitDepth = if (monitorExtraValue == "10") 10 else return@forEach
                }

                "transform" -> {
                    val transformValue = monitorExtraValue?.validateTransform() ?: return@forEach
                    monitor.transform = transformValue
                }

                "cm" -> {
                    val cm = monitorExtraValue?.validateCM() ?: return@forEach
                    monitor.cm = cm
                }

                "sdrsaturation" -> {
                    monitor.sdrsaturation = monitorExtraValue?.toFloatOrNull() ?: return@forEach
                }

                "sdrbrightness" -> {
                    monitor.sdrbrightness = monitorExtraValue?.toFloatOrNull() ?: return@forEach
                }

                "vrr" -> {
                    monitor.vvr = monitorExtraValue?.toIntOrNull() ?: return@forEach
                }

                "workspace" -> {
                    monitor.workspace = monitorExtraValue ?: return@forEach
                }

                else -> return@forEachIndexed
            }

        }

        monitorStore.add(monitor)
    }

    val monitorHyprlandSettings = mutableListOf<String>()

    handlePaths(monitorPath)

    monitorStore.forEach {
        if (it.disable) {
            monitorHyprlandSettings.add("monitor = ${it.name} ,disable".insertVariables())
            return@forEach
        } else if (it.addreserved && it.addreservedValue != null) {
            monitorHyprlandSettings.add("monitor = ${it.name} ,addreserved ,${it.addreservedValue?.top} ,${it.addreservedValue?.bottom} ,${it.addreservedValue?.left} ,${it.addreservedValue?.right}".insertVariables())
            return@forEach
        } else {

            val mirror = if (it.mirror != null) " ,mirror ,${it.mirror}" else ""
            val bitdepth = if (it.bitDepth != null) " ,bitdepth ,${it.bitDepth}" else ""
            val transform = if (it.transform != null) " ,transform ,${it.transform}" else ""
            val cm = if (it.cm != null) " ,cm ,${it.cm}" else ""
            val sdrbrightness = if (it.sdrbrightness != null) " ,sdrbrightness ,${it.sdrbrightness}" else ""
            val sdrsaturation = if (it.sdrsaturation != null) " ,sdrsaturation ,${it.sdrsaturation}" else ""
            val vvr = if (it.vvr != null) " ,vrr ,${it.vvr}" else ""
            val workspace = if (it.workspace != null) " ,workspace ,${it.workspace}" else ""

            monitorHyprlandSettings.add("monitor = ${it.name} ,${it.resolution} ,${it.position} ,${it.scale}${mirror}${bitdepth}${transform}${cm}${sdrbrightness}${sdrsaturation}${vvr}${workspace}".insertVariables())
        }
    }

    Path.of(monitorPath).writeText(monitorHyprlandSettings.joinToString("\n"))

    return monitorStore.toList()
}

private fun String.validateRes(): String? {
    return when {
        this.startsWith("pref") || this.startsWith("highres") || this.startsWith("highrr") -> this
        this.matches("^\\d{3,5}x\\d{3,5}(@\\d{1,3})?$".toRegex()) -> this
        else -> null
    }
}

private fun String.validatePosition(): String? {
    return when {
        this == "auto" || this.matches("auto-(right|left|up|down)".toRegex()) -> this
        this.matches("-?\\d+x\\d+".toRegex()) -> this
        else -> "auto"
    }
}

private fun String.validateScale(): String? {
    if (this == "auto") {
        return this
    }

    return runCatching {
        return this.toFloat().toString()
    }.getOrNull()
}

private fun String.validateTransform(): Int? {
    return runCatching {
        val transform = this.toInt()
        return if (transform in 0..7) transform else null
    }.getOrNull()
}

private fun String.validateCM(): String? {
    return when (this) {
        "auto", "srgb", "wide", "edid", "hdr", "hdredid" -> this
        else -> null
    }
}