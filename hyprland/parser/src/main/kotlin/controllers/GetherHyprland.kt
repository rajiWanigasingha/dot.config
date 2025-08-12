package controllers

import RequestPaths
import logger
import java.nio.file.Path
import kotlin.io.path.readText

private val mainPath = RequestPaths().getHyprlandPath()

/**
 * Get All Hyprland settings
 *
 * @return List of strings as hyprland settings.
 */
internal fun gatherHyprland() : Result<List<String>> {

    val allSettings = getSourceFiles()

    return Result.success(allSettings)
}


/**
 * This uses to gather all hyprland settings without any empty lines, comment lines or any source lines
 *
 * @param path as a [String]
 *
 * @return List of hyprland settings as strings
 */
private fun getSourceFiles(path: String? = null) : List<String> {

    if (mainPath == null) {
        TODO("Throw an exception")
    }

    val storeAllSettings = mutableListOf<String>()

    val readAll = if (path == null) {
        mainPath.readText()
    } else {
        Path.of(path).readText()
    }

    readAll
        .split("\n")
        .forEach {

            val line = it.trim()

            if (line == "" || line.startsWith("#")) return@forEach

            if (line.contains('=')) {
                line
                    .split("=")
                    .getOrNull(0)
                    ?.let { command ->

                        if (command.trim() == "source") {

                            logger.info("Found source file -> $line")

                            val source = line.split("=").getOrNull(1)?.trim() ?: return@let

                            val fullPath = source.getValidPath() ?: return@let

                            val sourceSettings = getSourceFiles(fullPath)

                            storeAllSettings.addAll(sourceSettings)

                        } else {

                            storeAllSettings.add(line.trim())
                        }

                    }
            } else {
                storeAllSettings.add(line.replace(" " ,""))
            }
        }

    return storeAllSettings
}


/**
 * Helper fun that uses to get the full path for source files.
 */
private fun String.getValidPath() : String? {

    val user = System.getProperty("user.home")

    return when {
        this.startsWith(user) -> return this
        this.startsWith("~") -> return this.replaceFirst("~" ,user)
        this.startsWith(".") -> return this.replaceFirst("." ,"$user/.config/hypr")
        this.startsWith("..") -> return this.replaceFirst("~" ,"$user/.config")

        else -> null
    }

}