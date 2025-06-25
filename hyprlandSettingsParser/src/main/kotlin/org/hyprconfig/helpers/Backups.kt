package org.hyprconfig.helpers

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists

private val backupFolder = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGenBackup"

private val configPaths = listOf(
    "/conf/variable.conf",
    "/conf/bind.conf",
    "/conf/monitor.conf",
    "/conf/execute.conf",
    "/conf/windowRules.conf",
    "/conf/workspace.conf",
    "/conf/env.conf",
    "/conf/layer.conf",
    "/conf/unbind.conf",
    "/conf/submap.conf",
    "/conf/permission.conf",
    "/conf/bezier.conf",
    "/conf/animation.conf",
    "/conf/general.conf",
    "/conf/misc.conf",
    "/conf/group.conf",
    "/conf/debug.conf",
    "/conf/decoration.conf",
    "/conf/dwindle.conf",
    "/conf/master.conf",
    "/conf/animations.conf",
    "/conf/inputs.conf",
    "/conf/binds.conf",
    "/conf/gestures.conf",
    "/conf/xwayland.conf",
    "/conf/openGL.conf",
    "/conf/cursor.conf",
    "/conf/render.conf",
    "/conf/ecosystem.conf",
    "/conf/experimental.conf",
    "/conf/device.conf"
)

/**
 * Creating backup files
 */
internal fun createBackups() {

    // Create Backup Folder
    if (!Path.of(backupFolder).exists()) {
        Path.of(backupFolder).createDirectory()
    }

    // Get all paths
    val allPath = mutableListOf(
        "${System.getProperty("user.home")}/.config/hypr/hyprland.conf"
    )

    allPaths.split("\n").forEach { allPath.add(it.split("=")[1].trim()) }

    // Create backup folder for these settings
    val time = LocalDateTime.now()

    val backupFolderPath = "$backupFolder/backup-at-$time"

    Path.of(backupFolderPath).createDirectory()
    Path.of("$backupFolderPath/hyprland.conf").createFile()

    // Create Backup of hyprland file

    val source = Path.of("${System.getProperty("user.home")}/.config/hypr/hyprland.conf")

    val target = Path.of("$backupFolderPath/hyprland.conf")

    Files.copy(source ,target , StandardCopyOption.REPLACE_EXISTING)

    // create other config files

    Path.of("$backupFolderPath/conf").createDirectory()

    configPaths.forEach {
        Path.of("$backupFolderPath$it").createFile()
    }

    allPath.forEach {
        val source = Path.of(it)

        if (!source.exists()) return@forEach

        val files = it.split("/")

        val fileName = files.getOrNull(files.lastIndex)?.checkFileName() ?: return@forEach

        val target = Path.of("$backupFolderPath/conf/$fileName")

        Files.copy(source ,target , StandardCopyOption.REPLACE_EXISTING)
    }
}

private fun String.checkFileName(): String? {
    return if (this.matches("[\\w\\-]+\\.conf".toRegex())) this else null
}