package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.DevicesModel
import org.hyprconfig.model.defaultDeviceSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Device Settings :")

private val devicePath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/device.conf"

internal fun handleDevice(devices: List<String>): List<DevicesModel> {

    logger.info("Try to process and create hyprland device settings")

    val deviceStore = defaultDeviceSettings

    devices.forEach {

        val processDevice = it.split(":=").map { device -> device.trim() }

        val name = processDevice.getOrNull(0) ?: return@forEach

        val value = processDevice.getOrNull(1)?.split("=")?.map { device -> device.trim() } ?: return@forEach

        val deviceName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val deviceValue = value.getOrNull(1) ?: return@forEach

        deviceStore.forEach { device ->
            if (device.name == deviceName) {
                val validateDevice = deviceValue.hyprlandTypeCheck(device.type) ?: return@forEach

                if (validateDevice != device.value) device.value = validateDevice
            }
        }
    }

    logger.info("Creating device hyprland settings")

    handlePaths(devicePath)

    val deviceSettings = generateDeviceSettings(deviceStore)

    Path.of(devicePath).writeText(deviceSettings)

    return deviceStore.toList()
}

fun generateDeviceSettings(settings: List<DevicesModel>): String {

    if (settings.find { it.name == "device:name" }!!.value.isEmpty()) {
        return ""
    }

    return """
        device {
            name = ${settings.find { it.name == "device:name" }!!.value}
            sensitivity = ${settings.find { it.name == "device:sensitivity" }!!.value}
            accel_profile = ${settings.find { it.name == "device:accel_profile" }!!.value}
            kb_file = ${settings.find { it.name == "device:kb_file" }!!.value}
            kb_layout = ${settings.find { it.name == "device:kb_layout" }!!.value}
            kb_variant = ${settings.find { it.name == "device:kb_variant" }!!.value}
            kb_options = ${settings.find { it.name == "device:kb_options" }!!.value}
            kb_rules = ${settings.find { it.name == "device:kb_rules" }!!.value}
            kb_model = ${settings.find { it.name == "device:kb_model" }!!.value}
            repeat_rate = ${settings.find { it.name == "device:repeat_rate" }!!.value}
            repeat_delay = ${settings.find { it.name == "device:repeat_delay" }!!.value}
            natural_scroll = ${settings.find { it.name == "device:natural_scroll" }!!.value}
            numlock_by_default = ${settings.find { it.name == "device:numlock_by_default" }!!.value}
            resolve_binds_by_sym = ${settings.find { it.name == "device:resolve_binds_by_sym" }!!.value}
            left_handed = ${settings.find { it.name == "device:left_handed" }!!.value}
            scroll_method = ${settings.find { it.name == "device:scroll_method" }!!.value}
            scroll_button = ${settings.find { it.name == "device:scroll_button" }!!.value}
            scroll_button_lock = ${settings.find { it.name == "device:scroll_button_lock" }!!.value}
            scroll_points = ${settings.find { it.name == "device:scroll_points" }!!.value}
            transform = ${settings.find { it.name == "device:transform" }!!.value}
            output = ${settings.find { it.name == "device:output" }!!.value}
        }
    """.insertVariables().trimIndent()
}