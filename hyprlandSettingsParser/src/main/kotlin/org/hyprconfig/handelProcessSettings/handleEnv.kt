package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.EnvModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel Environment Variables Settings :")

private val envPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/env.conf"


/**
 * This uses to process and create environment settings
 *
 * @param env as list of string
 * @return list of [EnvModel]
 */
internal fun handleEnv(env: List<String>): List<EnvModel> {

    logger.info("Try to process and create hyprland env settings")

    val envStore = mutableListOf<EnvModel>()

    env.forEach {

        val processEnv = it.split("#")[0].split("=").getOrNull(1)?.trim() ?: return@forEach

        envStore.add(EnvModel(processEnv))
    }

    logger.info("Create env hyprland settings file")

    handlePaths(envPath)

    Path.of(envPath).writeText(env.joinToString("\n") { it.insertVariables() })

    return envStore.toList()
}