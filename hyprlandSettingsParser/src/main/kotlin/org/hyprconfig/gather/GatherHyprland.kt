package org.hyprconfig.gather

import org.hyprconfig.errors.NoHyprlandPathFound
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

private val logger = LoggerFactory.getLogger("Gathering Hyprland :")

private val mainPath = "${System.getProperty("user.home")}/.config/hypr/hyprland.conf"

/**
 * Get All Hyprland settings
 *
 * @return List of strings as hyprland settings.
 */
internal fun gatherHyprland() :List<String> {

    Path.of(mainPath)
        .takeUnless { it.exists() }
        ?.let {
            throw NoHyprlandPathFound()
        }

    val allSettings = getSourceFiles(mainPath)

    return allSettings
}


/**
 * This uses to gather all hyprland settings without any empty lines, comment lines or any source lines
 *
 * @param path as a [String]
 *
 * @return List of hyprland settings as strings
 */
private fun getSourceFiles(path: String) : List<String> {

    val storeAllSettings = mutableListOf<String>()

    val readAll = Path.of(path).readText()

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