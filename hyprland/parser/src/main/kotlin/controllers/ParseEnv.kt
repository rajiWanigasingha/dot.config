package controllers

import logger
import model.tables.EnvModel
import write.env.WriteEnv

/**
 * This uses to process and create environment settings
 *
 * @param env as list of string
 * @return list of [EnvModel]
 */
internal fun parseEnv(env: List<String>): Result<List<EnvModel>> {

    logger.info("Try to process and create hyprland env settings")

    val envStore = mutableListOf<EnvModel>()

    env.forEach {

        if (!it.startsWith("env")) return@forEach

        val processEnv = it.split("#")[0].split("=").getOrNull(1)?.trim() ?: return@forEach

        envStore.add(EnvModel(processEnv))
    }

    val write = WriteEnv()

    write.writeIntoHyprland(envStore).getOrThrow()
    write.writeIntoDotConfig(envStore).getOrThrow()

    return Result.success(envStore.toList())
}