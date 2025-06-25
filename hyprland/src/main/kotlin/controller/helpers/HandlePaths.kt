package org.dot.config.controller.helpers

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

}