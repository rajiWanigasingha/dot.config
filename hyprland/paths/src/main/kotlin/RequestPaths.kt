import ProjectPaths.toPath
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

class RequestPaths {

    fun getHyprlandPath(): Path? {

        val path = Path.of(ProjectPaths.hyprland_config_file)

        if (!path.exists()) {
            return null
        }

        return path
    }

    fun getDispatcherPath(): Path? {
        val pathToDispatch = ProjectPaths.keybindDispatchers.toPath()

        if (!pathToDispatch.exists()) {
            return null
        }

        return pathToDispatch
    }

    fun getFlags(): Path? {
        val pathToFlags = ProjectPaths.keybindFlags.toPath()

        if (!pathToFlags.exists()) {
            return null
        }

        return pathToFlags
    }

    fun getWindowRules(rules: String): Path {
        return when (rules) {
            "STATIC" -> {
                Path.of("${ProjectPaths.rules}/${ProjectPaths.ruleFolders[1]}.csv")
            }

            "DYNAMIC" -> {
                Path.of("${ProjectPaths.rules}/${ProjectPaths.ruleFolders[0]}.csv")
            }

            else -> {
                Path.of("${ProjectPaths.rules}/${ProjectPaths.ruleFolders[2]}.csv")
            }
        }
    }

    fun getPathsForKeyword(key: String): List<String>? {
        return when (key) {
            "general" -> listOf("general", "generalSnap")
            "misc" -> listOf("misc")
            "group" -> listOf("group", "groupBar")
            "debug" -> listOf("debug")
            "decoration" -> listOf("decoration", "decorationBlur", "decorationShadow")
            "dwindle" -> listOf("dwindle")
            "master" -> listOf("master")
            "animations" -> listOf("animations")
            "inputs" -> listOf("inputs", "inputsTouchpad", "inputsTouchDevice", "inputsTablet")
            "binds" -> listOf("binds")
            "gestures" -> listOf("gestures")
            "xwayland" -> listOf("xwayland")
            "openGL" -> listOf("openGl")
            "cursor" -> listOf("cursor")
            "render" -> listOf("render")
            "ecosystem" -> listOf("ecosystem")
            "experimental" -> listOf("experimental")
            "device" -> listOf("device")
            else -> null
        }
    }

    fun getHyprlandStruct(key: String): List<String>? {
        return when (key) {
            "general" -> listOf("general {", "snap {", "}", "}")
            "misc" -> listOf("misc {", "}")
            "group" -> listOf("group {", "groupbar {", "}", "}")
            "debug" -> listOf("debug {", "}")
            "decoration" -> listOf("decoration {", "blur {", "}", "shadow {", "}", "}")
            "dwindle" -> listOf("dwindle {", "}")
            "master" -> listOf("master {", "}")
            "animations" -> listOf("animations {", "}")
            "inputs" -> listOf("input {", "touchpad {", "}", "touchdevice {", "}", "tablet {", "}", "}")
            "binds" -> listOf("binds {", "}")
            "gestures" -> listOf("gestures {", "}")
            "xwayland" -> listOf("xwayland {", "}")
            "openGL" -> listOf("opengl {", "}")
            "cursor" -> listOf("cursor {", "}")
            "render" -> listOf("render {", "}")
            "ecosystem" -> listOf("ecosystem {", "}")
            "experimental" -> listOf("experimental {", "}")
            "device" -> listOf("device {", "}")
            else -> null
        }
    }


    fun getHyprlandKeywordPath(): String {
        return "${ProjectPaths.defaultHyprlandKeywordPaths}/0.49/"
    }

    fun getHyprlandKeywordStore(): String {
        return ProjectPaths.defaultHyprlandStore
    }

    fun allCreatedFilesOnHyprland(): List<Path>? {
        return ProjectPaths
            .defaultHyprlandGen
            .toPath()
            .takeIf { it.exists() && it.isDirectory() }
            ?.listDirectoryEntries()
    }

    fun getHyprlandDotConfigPath(): Path {
        return ProjectPaths.pathInDotConfig.toPath()
    }

    fun  getBackupPath(): Path {

        val backupPath = Path.of(ProjectPaths.backupPath)

        if (!backupPath.exists()) {
            backupPath.createDirectories()
            backupPath.resolve("backup.conf").createFile()
        }

        if (!backupPath.resolve("backup.conf").exists()) {
            backupPath.resolve("backup.conf").createFile()
        }

        return backupPath
    }
}