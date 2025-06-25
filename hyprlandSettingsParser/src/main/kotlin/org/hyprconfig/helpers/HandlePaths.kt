package org.hyprconfig.helpers

import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists

val allPaths = """
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/variable.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bind.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/monitor.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/execute.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/windowRules.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/workspace.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/env.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/layer.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/unbind.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/submap.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/permission.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bezier.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/animation.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/general.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/misc.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/group.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/debug.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/decoration.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/dwindle.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/master.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/animations.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/inputs.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/binds.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/gestures.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/xwayland.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/openGL.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/cursor.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/render.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/ecosystem.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/experimental.conf
    source = ${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/device.conf
""".trimIndent()

/**
 * Check if settings files exist if not create it
 *
 * @param path as a string
 */
internal fun handlePaths(path: String) {
    val hyprPath = Path.of(path)

    if (!hyprPath.parent.exists()) {
        hyprPath.parent.createDirectory()
        hyprPath.createFile()
    }

    if (!hyprPath.exists()) {
        hyprPath.createFile()
    }
}