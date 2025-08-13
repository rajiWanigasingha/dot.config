import controllers.gatherHyprland
import controllers.parseAnimation
import controllers.parseBezier
import controllers.parseEnv
import controllers.parseExecute
import controllers.parseLayers
import controllers.parseMonitor
import controllers.parsePermissions
import controllers.parseSubmap
import controllers.parseUnbind
import controllers.parseVariables
import controllers.parseWindowRules
import controllers.parseWorkspace
import controllers.parserKeybinds
import model.HyprlandSettingsModel
import model.SuccessRateOfParse
import org.slf4j.LoggerFactory

internal val logger = LoggerFactory.getLogger("Hyprland Parser :")

class HyprlandParser {

    val successRateOfParse = listOf(
        SuccessRateOfParse(name = "gather", success = null),
        SuccessRateOfParse(name = "variables", success = null),
        SuccessRateOfParse(name = "bind", success = null),
        SuccessRateOfParse(name = "monitor", success = null),
        SuccessRateOfParse(name = "execute", success = null),
        SuccessRateOfParse(name = "windowRules", success = null),
        SuccessRateOfParse(name = "workspace", success = null),
        SuccessRateOfParse(name = "env", success = null),
        SuccessRateOfParse(name = "layerRules", success = null),
        SuccessRateOfParse(name = "unbind", success = null),
        SuccessRateOfParse(name = "submap", success = null),
        SuccessRateOfParse(name = "permission", success = null),
        SuccessRateOfParse(name = "bezier", success = null),
        SuccessRateOfParse(name = "animation", success = null),
        SuccessRateOfParse(name = "general", success = null),
        SuccessRateOfParse(name = "misc", success = null),
        SuccessRateOfParse(name = "group", success = null),
        SuccessRateOfParse(name = "debug", success = null),
        SuccessRateOfParse(name = "decoration", success = null),
        SuccessRateOfParse(name = "dwindle", success = null),
        SuccessRateOfParse(name = "master", success = null),
        SuccessRateOfParse(name = "animations", success = null),
        SuccessRateOfParse(name = "inputs", success = null),
        SuccessRateOfParse(name = "binds", success = null),
        SuccessRateOfParse(name = "gestures", success = null),
        SuccessRateOfParse(name = "xwayland", success = null),
        SuccessRateOfParse(name = "openGL", success = null),
        SuccessRateOfParse(name = "cursor", success = null),
        SuccessRateOfParse(name = "render", success = null),
        SuccessRateOfParse(name = "ecosystem", success = null),
        SuccessRateOfParse(name = "experimental", success = null),
        SuccessRateOfParse(name = "device", success = null)
    )

    fun parseConfig() {

        logger.info("Begin Parsing hyprland settings")

        val processSettings = HyprlandSettingsModel()

        logger.info("Gather All Settings From All Source File")

        val gatherAllHyprland = gatherHyprland().getOrElse {
            changeSuccess(name = "gather" , success = false)
            return
        }

        logger.info("All Settings Are Collected")

        changeSuccess(name = "gather" , success = true)

        logger.info("Create Variable Settings")

        val variables = parseVariables(allSettings = gatherAllHyprland).getOrElse {
            changeSuccess(name = "variable" , success = false)
            return
        }

        logger.info("Got All Variables")

        changeSuccess(name = "variable" , success = true)

        processSettings.variables = variables.first

        val allOtherSettings = variables.second

        logger.info("Create Keybind Settings")

        val keybinds = parserKeybinds(keyBinds = allOtherSettings).getOrElse {
            changeSuccess(name = "bind" , success = false)
            return
        }

        logger.info("Got All Keybinds")

        changeSuccess(name = "bind" , success = true)

        processSettings.bind = keybinds

        logger.info("Create monitor Settings")

        val monitor = parseMonitor(monitors = allOtherSettings).getOrElse {
            changeSuccess(name = "monitor" , success = false)
            return
        }

        logger.info("Got All Monitor")

        changeSuccess(name = "monitor" , success = true)

        processSettings.monitor = monitor

        logger.info("Create Auto Start Settings")

        val autoStart = parseExecute(execute = allOtherSettings).getOrElse {
            changeSuccess(name = "autoStart" , success = false)
            return
        }

        logger.info("Got All Auto Start")

        changeSuccess(name = "autoStart" , success = true)

        processSettings.execute = autoStart

        logger.info("Create Window Rules")

        val windowRule = parseWindowRules(windows = allOtherSettings).getOrElse {
            changeSuccess(name = "windowRules" , success = false)
            return
        }

        logger.info("Got All Window Rules")

        changeSuccess(name = "windowRules" , success = true)

        processSettings.windowRules = windowRule

        logger.info("Create Workspace Settings")

        val workspaceRules = parseWorkspace(workspace = allOtherSettings).getOrElse {
            changeSuccess(name = "workspace" , success = false)
            return
        }

        logger.info("Got All Workspace Rules")

        changeSuccess(name = "workspace" , success = true)

        processSettings.workspace = workspaceRules

        logger.info("Create Env Settings")

        val envRules = parseEnv(env = allOtherSettings).getOrElse {
            changeSuccess(name = "env" , success = false)
            return
        }

        logger.info("Got All Env Rules")

        changeSuccess(name = "env" , success = true)

        processSettings.env = envRules

        logger.info("Create Layer Settings")

        val layers = parseLayers(layer = allOtherSettings).getOrElse {
            changeSuccess(name = "layerRules" , success = false)
            return
        }

        logger.info("Got All Layer Rules")

        changeSuccess(name = "layerRules" , success = true)

        processSettings.layerRules = layers

        logger.info("Create Unbind Settings")

        val unbind = parseUnbind(unbind = allOtherSettings).getOrElse {
            changeSuccess(name = "unbind" , success = false)
            return
        }

        logger.info("Got All unbind")

        changeSuccess(name = "unbind" , success = true)

        processSettings.unbind = unbind

        logger.info("Create Submap Settings")

        val submap = parseSubmap(submap = allOtherSettings).getOrElse {
            changeSuccess(name = "submap" , success = false)
            return
        }

        logger.info("Got All unbind")

        changeSuccess(name = "submap" , success = true)

        processSettings.submap = submap

        logger.info("Create Permission Settings")

        val permission = parsePermissions(permission = allOtherSettings).getOrElse {
            changeSuccess(name = "permission" , success = false)
            return
        }

        logger.info("Got All Permission")

        changeSuccess(name = "permission" , success = true)

        processSettings.permission = permission

        logger.info("Create Bezier Curves Settings")

        val bezier = parseBezier(bezier = allOtherSettings).getOrElse {
            changeSuccess(name = "bezier" , success = false)
            return
        }

        logger.info("Got All Bezier")

        changeSuccess(name = "bezier" , success = true)

        processSettings.bezier = bezier

        logger.info("Create Animation Curves Settings")

        val animation = parseAnimation(animation = allOtherSettings).getOrElse {
            changeSuccess(name = "animation" , success = false)
            return
        }

        logger.info("Got All Animation")

        changeSuccess(name = "animation" , success = true)

        processSettings.animation = animation
    }

    private fun changeSuccess(name: String ,success: Boolean) {
        successRateOfParse.forEach {
            if (it.name == name) {
                it.success = success
            }
        }
    }
}