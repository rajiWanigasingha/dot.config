import ProjectPaths.toPath
import java.nio.file.Path
import kotlin.io.path.exists

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
}