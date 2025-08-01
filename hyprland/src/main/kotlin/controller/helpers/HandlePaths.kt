package org.dot.config.controller.helpers

import org.dot.config.view.builderComponents.Sidebar
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.io.read
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.writeText

class HandlePaths {

    private val logger = LoggerFactory.getLogger(javaClass.name)

    private val user = System.getProperty("user.home")

    private val dataStorePath = "${user}/.dot.config/data"

    init {
        if (!Path.of(dataStorePath).parent.exists()) {
            Path.of(dataStorePath).parent.createDirectory()
        }

        if (!Path.of(dataStorePath).exists()) {
            Path.of(dataStorePath).createDirectory()
        }
    }

    fun createFiles(): String {
        val paths = listOf(
            "$dataStorePath/general.csv",
            "$dataStorePath/generalSnap.csv",
            "$dataStorePath/decoration.csv",
            "$dataStorePath/decorationBlur.csv",
            "$dataStorePath/decorationShadow.csv",
            "$dataStorePath/animations.csv",
            "$dataStorePath/inputs.csv",
            "$dataStorePath/inputsTouchpad.csv",
            "$dataStorePath/inputsTouchDevice.csv",
            "$dataStorePath/inputsTablet.csv",
            "$dataStorePath/gestures.csv",
            "$dataStorePath/group.csv",
            "$dataStorePath/groupBar.csv",
            "$dataStorePath/misc.csv",
            "$dataStorePath/binds.csv",
            "$dataStorePath/xwayland.csv",
            "$dataStorePath/openGl.csv",
            "$dataStorePath/render.csv",
            "$dataStorePath/ecosystem.csv",
            "$dataStorePath/dwindle.csv",
            "$dataStorePath/master.csv",
            "$dataStorePath/debug.csv",
            "$dataStorePath/experimental.csv",
            "$dataStorePath/keybind.csv",
            "$dataStorePath/parsedTime.csv"
            )

        paths.forEach {
            Path.of(it).takeIf { path -> !path.exists() }?.createFile() ?: Path.of(it).writeText("")
        }

        return dataStorePath
    }

    fun checkNeedToParsedAnyPath(): Boolean {
        val path = Path.of("$dataStorePath/parsedTime.csv")

        if (!path.exists()) return false

        val pathDf = DataFrame.read(path = "$dataStorePath/parsedTime.csv")

        pathDf.forEach {
            val path = it["path"]?.toString() ?: return@forEach
            val time = it["time"]?.toString()?.split(".")?.getOrNull(0) ?: return@forEach

            val validPath = Path.of(path).exists()

            if (validPath) {
                val modifyTime = Path.of(path).getLastModifiedTime().toString().split(".").getOrNull(0)

                val size = Path.of(path).fileSize()

                if (size == 0L) return@forEach

                if (modifyTime != time) {
                    return false
                }

            } else {
                return false
            }
        }

        return true
    }

    fun getPathToUpdate(actionLink: Sidebar.ActionLinks): List<String> {

        return when (actionLink) {
            Sidebar.ActionLinks.MOUSE_AND_TOUCHPAD -> listOf("inputs.csv", "inputsTouchpad.csv" ,"misc.csv")
            Sidebar.ActionLinks.CURSOR -> listOf("cursor.csv")
            Sidebar.ActionLinks.GESTURES -> listOf("gestures.csv")
            Sidebar.ActionLinks.KEYBOARD -> TODO()
            Sidebar.ActionLinks.KEYBINDS -> TODO()
            Sidebar.ActionLinks.DISPLAY_AND_MONITOR -> TODO()
            Sidebar.ActionLinks.TOUCH -> listOf("inputsTouchDevice.csv","inputsTablet.csv")
            Sidebar.ActionLinks.LAYOUT_GENERAL -> listOf("general.csv" ,"misc.csv")
            Sidebar.ActionLinks.DWINDLE_LAYOUT -> listOf("dwindle.csv")
            Sidebar.ActionLinks.MASTER_LAYOUT -> listOf("master.csv")
            Sidebar.ActionLinks.WORKSPACE_RULES -> TODO()
            Sidebar.ActionLinks.WINDOW_RULES -> TODO()
            Sidebar.ActionLinks.GROUPS -> listOf("group.csv" ,"groupBar.csv")
            Sidebar.ActionLinks.GAPS_AND_BORDERS -> listOf("general.csv" ,"generalSnap.csv")
            Sidebar.ActionLinks.WINDOW_DECORATION -> listOf("decoration.csv" ,"misc.csv")
            Sidebar.ActionLinks.BLUR -> listOf("decorationBlur.csv")
            Sidebar.ActionLinks.SHADOW -> listOf("decorationShadow.csv")
            Sidebar.ActionLinks.ANIMATIONS -> listOf("animations.csv")
            Sidebar.ActionLinks.ENV -> TODO()
            Sidebar.ActionLinks.AUTOSTART -> TODO()
            Sidebar.ActionLinks.MISC -> listOf("misc.csv")
            Sidebar.ActionLinks.GRAPHICS -> listOf("xwayland.csv" ,"openGl.csv" ,"render.csv")
            Sidebar.ActionLinks.ECOSYSTEM -> listOf("ecosystem.csv")
            Sidebar.ActionLinks.EXPERIMENT -> listOf("experimental.csv")
            Sidebar.ActionLinks.DEBUG -> listOf("debug.csv")
            Sidebar.ActionLinks.VARIABLES -> TODO()
            Sidebar.ActionLinks.ANIMATION -> TODO()
        }

    }

}