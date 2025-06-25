package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.hyprlandTypeCheck
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.EcosystemModel
import org.hyprconfig.model.defaultEcosystemSettings
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Ecosystem Settings :")

private val ecosystemPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/ecosystem.conf"

internal fun handleEcosystem(ecosystem: List<String>): List<EcosystemModel> {

    logger.info("Try to process and create hyprland ecosystem settings")

    val ecosystemStore = defaultEcosystemSettings

    ecosystem.forEach {

        val processEcosystem = it.split(":=").map { eco -> eco.trim() }

        val name = processEcosystem.getOrNull(0) ?: return@forEach

        val value = processEcosystem.getOrNull(1)?.split("=")?.map { eco -> eco.trim() } ?: return@forEach

        val ecosystemName = "$name:${value.getOrNull(0) ?: return@forEach}"

        val ecosystemValue = value.getOrNull(1) ?: return@forEach

        ecosystemStore.forEach innerLoop@{ eco ->
            if (eco.name == ecosystemName) {
                val validateRenderValue = ecosystemValue.hyprlandTypeCheck(eco.type) ?: return@forEach

                if (validateRenderValue != eco.value) eco.value = validateRenderValue
            }
        }
    }

    logger.info("Creating hyprland ecosystem settings")

    handlePaths(ecosystemPath)

    val ecosystemSettings = generateEcosystemSettings(ecosystemStore)

    Path.of(ecosystemPath).writeText(ecosystemSettings)

    return ecosystemStore.toList()
}

fun generateEcosystemSettings(settings: List<EcosystemModel>): String {
    return """
        ecosystem {
            no_update_news = ${settings.find { it.name == "ecosystem:no_update_news" }!!.value}
            no_donation_nag = ${settings.find { it.name == "ecosystem:no_donation_nag" }!!.value}
            enforce_permissions = ${settings.find { it.name == "ecosystem:enforce_permissions" }!!.value}
        }
    """.insertVariables().trimIndent()
}