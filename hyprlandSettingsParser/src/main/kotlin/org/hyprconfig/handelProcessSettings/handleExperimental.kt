package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.ExperimentalModel
import org.hyprconfig.model.defaultExperimentalSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Experimental Settings :")

private val experimentalPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/experimental.conf"

internal fun handleExperimental(exp: List<String>): List<ExperimentalModel> {

    logger.info("Try to process and create experimental hyprland settings")

    val expStore = defaultExperimentalSettings

    exp.forEach {

        val processExperimental = it.split(":=").map { exp -> exp.trim() }

        val name = processExperimental.getOrNull(0) ?: return@forEach

        val value = processExperimental.getOrNull(1)?.split("=")?.map { exp -> exp.trim() } ?: return@forEach

        val experimentalName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val experimentalValue = value.getOrNull(1) ?: return@forEach

        expStore.forEach innerLoop@{ exp ->
            if (exp.name == experimentalName) {
                val validateExp = experimentalValue.hyprlandTypeCheck(exp.type) ?: return@forEach

                if (validateExp != exp.value) exp.value = validateExp
            }
        }
    }

    logger.info("Creating experimental hyprland settings")

    handlePaths(experimentalPath)

    val experimentalSettings = generateExperimentalSettings(expStore)

    Path.of(experimentalPath).writeText(experimentalSettings)

    return expStore.toList()
}

fun generateExperimentalSettings(settings: List<ExperimentalModel>): String {
    return """
        experimental {
            xx_color_management_v4 = ${settings.find { it.name == "experimental:xx_color_management_v4" }!!.value}
        }
    """.insertVariables().trimIndent()
}
