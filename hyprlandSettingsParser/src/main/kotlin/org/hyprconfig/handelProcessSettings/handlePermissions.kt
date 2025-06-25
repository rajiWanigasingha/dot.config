package org.hyprconfig.handelProcessSettings

import org.hyprconfig.helpers.handlePaths
import org.hyprconfig.helpers.insertVariables
import org.hyprconfig.model.PermissionModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

private val logger = LoggerFactory.getLogger("Handel permissions Settings :")

private val permissionPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/permission.conf"

/**
 * These uses to process and create permission for hyprland settings
 *
 * @param permission as list of strings
 * @return as list [PermissionModel]
 */
internal fun handlePermissions(permission: List<String>): List<PermissionModel> {

    logger.info("Try to process and create hyprland permissions")

    val permissionStore = mutableListOf<PermissionModel>()

    permission.forEach {

        val processPermission =
            it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { permission -> permission.trim() }
                ?: return@forEach

        val regex = processPermission.getOrNull(0) ?: return@forEach

        val permissions = processPermission.getOrNull(1)?.validatePermission() ?: return@forEach

        val mode = processPermission.getOrNull(2)?.validateMode() ?: return@forEach

        permissionStore.add(PermissionModel(regex, permissions, mode))
    }

    logger.info("Creating hyprland permission settings")

    handlePaths(permissionPath)

    val permissionSettings = mutableListOf<String>()

    permissionStore.forEach {
        permissionSettings.add("permission = ${it.regex} ,${it.permission} ,${it.mode}".insertVariables())
    }

    Path.of(permissionPath).writeText(permissionSettings.joinToString("\n"))

    return permissionStore.toList()
}

private fun String.validatePermission(): String? {
    return when (this.trim()) {
        "screencopy", "plugin", "keyboard", "keeb" -> this
        else -> null
    }
}

private fun String.validateMode(): String? {
    return when (this.trim()) {
        "ask", "allow", "deny" -> this
        else -> null
    }
}