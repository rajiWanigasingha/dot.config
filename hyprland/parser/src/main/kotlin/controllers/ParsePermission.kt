package controllers

import logger
import model.tables.PermissionModel
import write.permission.WritePermission

/**
 * These uses to process and create permission for hyprland settings
 *
 * @param permission as list of strings
 * @return as list [PermissionModel]
 */
internal fun parsePermissions(permission: List<String>): Result<List<PermissionModel>> {

    logger.info("Try to process and create hyprland permissions")

    val permissionStore = mutableListOf<PermissionModel>()

    permission.forEach {

        if (!it.startsWith("permission")) return@forEach

        val processPermission =
            it.split("#")[0].split("=").getOrNull(1)?.split(",")?.map { permission -> permission.trim() }
                ?: return@forEach

        val regex = processPermission.getOrNull(0) ?: return@forEach

        val permissions = processPermission.getOrNull(1)?.validatePermission() ?: return@forEach

        val mode = processPermission.getOrNull(2)?.validateMode() ?: return@forEach

        permissionStore.add(PermissionModel(regex, permissions, mode))
    }

    val write = WritePermission()

    write.writeIntoHyprland(permissionStore).getOrThrow()
    write.writeIntoDotConfig(permissionStore).getOrThrow()

    return Result.success(permissionStore.toList())
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