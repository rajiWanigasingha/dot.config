package org.hyprconfig.helpers

import org.hyprconfig.model.StoreInfoModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.getAttribute

private val logger = LoggerFactory.getLogger("Get Time Paths:")

internal fun getTimeForPaths(): MutableList<StoreInfoModel> {

    logger.info("Get time that paths created")

    val info = mutableListOf<StoreInfoModel>()

    val hyprlandPaths = mutableListOf(
        "${System.getProperty("user.home")}/.config/hypr/hyprland.conf"
    )

    allPaths.split("\n").forEach {

        val paths = it.split("=")[1].trim()

        hyprlandPaths.add(paths)
    }


    hyprlandPaths.forEach {
        val attribute = Path.of(it).getAttribute("lastModifiedTime") ?: return@forEach

        info.add(StoreInfoModel(it ,attribute.toString()))
    }

    return info
}