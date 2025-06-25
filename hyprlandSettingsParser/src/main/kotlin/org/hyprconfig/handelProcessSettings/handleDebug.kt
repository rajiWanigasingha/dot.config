package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.DebugModel
import org.hyprconfig.model.defaultDebugSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel debug Settings :")

private val debugPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/debug.conf"

internal fun handleDebug(debug: List<String>): List<DebugModel> {

    logger.info("Try to process and create hyprland debug settings")

    val debugStore = defaultDebugSettings

    debug.forEach {

        val processDebug = it.split(":=").map { debug -> debug.trim() }

        val name = processDebug.getOrNull(0)?.trim() ?: return@forEach

        val value = processDebug.getOrNull(1)?.split("=")?.map { debug -> debug.trim() } ?: return@forEach

        val debugName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val debugValue = value.getOrNull(1) ?: return@forEach

        debugStore.forEach innerLoop@{ debug ->
            if (debug.name == debugName) {

                val validateDebugValue = debugValue.hyprlandTypeCheck(debug.type) ?: return@forEach

                if (validateDebugValue != debug.value) debug.value = validateDebugValue
            }
        }
    }

    logger.info("Creating hyprland debug settings file")

    handlePaths(debugPath)

    val debugSettings = generateDebugSettings(debugStore)

    Path.of(debugPath).writeText(debugSettings)

    return debugStore.toList()
}


private fun generateDebugSettings(debug: List<DebugModel>): String {
    return """
        debug {
            overlay = ${debug.find { it.name == "debug:overlay" }!!.value}
            damage_blink = ${debug.find { it.name == "debug:damage_blink" }!!.value}
            pass = ${debug.find { it.name == "debug:pass" }!!.value}
            disable_logs = ${debug.find { it.name == "debug:disable_logs" }!!.value}
            disable_time = ${debug.find { it.name == "debug:disable_time" }!!.value}
            enable_stdout_logs = ${debug.find { it.name == "debug:enable_stdout_logs" }!!.value}
            damage_tracking = ${debug.find { it.name == "debug:damage_tracking" }!!.value}
            manual_crash = ${debug.find { it.name == "debug:manual_crash" }!!.value}
            suppress_errors = ${debug.find { it.name == "debug:suppress_errors" }!!.value}
            error_limit = ${debug.find { it.name == "debug:error_limit" }!!.value}
            error_position = ${debug.find { it.name == "debug:error_position" }!!.value}
            disable_scale_checks = ${debug.find { it.name == "debug:disable_scale_checks" }!!.value}
            colored_stdout_logs = ${debug.find { it.name == "debug:colored_stdout_logs" }!!.value}
            full_cm_proto = ${debug.find { it.name == "debug:full_cm_proto" }!!.value}
        }
    """.insertVariables().trimIndent()

}