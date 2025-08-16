import controllers.gatherHyprland
import controllers.parseAnimation
import controllers.parseBezier
import controllers.parseEnv
import controllers.parseExecute
import controllers.parseKeywords
import controllers.parseLayers
import controllers.parseMonitor
import controllers.parsePermissions
import controllers.parseSubmap
import controllers.parseUnbind
import controllers.parseVariables
import controllers.parseWindowRules
import controllers.parseWorkspace
import controllers.parserKeybinds
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

        val allOtherSettings = variables

        logger.info("Create Keybind Settings")

        parserKeybinds(keyBinds = allOtherSettings).getOrElse {
            changeSuccess(name = "bind" , success = false)
            return
        }

        logger.info("Got All Keybinds")

        changeSuccess(name = "bind" , success = true)

        logger.info("Create monitor Settings")

        parseMonitor(monitors = allOtherSettings).getOrElse {
            changeSuccess(name = "monitor" , success = false)
            return
        }

        logger.info("Got All Monitor")

        changeSuccess(name = "monitor" , success = true)

        logger.info("Create Auto Start Settings")

        parseExecute(execute = allOtherSettings).getOrElse {
            changeSuccess(name = "autoStart" , success = false)
            return
        }

        logger.info("Got All Auto Start")

        changeSuccess(name = "autoStart" , success = true)

        logger.info("Create Window Rules")

        parseWindowRules(windows = allOtherSettings).getOrElse {
            changeSuccess(name = "windowRules" , success = false)
            return
        }

        logger.info("Got All Window Rules")

        changeSuccess(name = "windowRules" , success = true)

        logger.info("Create Workspace Settings")

        parseWorkspace(workspace = allOtherSettings).getOrElse {
            changeSuccess(name = "workspace" , success = false)
            return
        }

        logger.info("Got All Workspace Rules")

        changeSuccess(name = "workspace" , success = true)

        logger.info("Create Env Settings")

        parseEnv(env = allOtherSettings).getOrElse {
            changeSuccess(name = "env" , success = false)
            return
        }

        logger.info("Got All Env Rules")

        changeSuccess(name = "env" , success = true)

        logger.info("Create Layer Settings")

        parseLayers(layer = allOtherSettings).getOrElse {
            changeSuccess(name = "layerRules" , success = false)
            return
        }

        logger.info("Got All Layer Rules")

        changeSuccess(name = "layerRules" , success = true)

        logger.info("Create Unbind Settings")

        parseUnbind(unbind = allOtherSettings).getOrElse {
            changeSuccess(name = "unbind" , success = false)
            return
        }

        logger.info("Got All unbind")

        changeSuccess(name = "unbind" , success = true)

        logger.info("Create Submap Settings")

        parseSubmap(submap = allOtherSettings).getOrElse {
            changeSuccess(name = "submap" , success = false)
            return
        }

        logger.info("Got All unbind")

        changeSuccess(name = "submap" , success = true)

        logger.info("Create Permission Settings")

        parsePermissions(permission = allOtherSettings).getOrElse {
            changeSuccess(name = "permission" , success = false)
            return
        }

        logger.info("Got All Permission")

        changeSuccess(name = "permission" , success = true)

        logger.info("Create Bezier Curves Settings")

        parseBezier(bezier = allOtherSettings).getOrElse {
            changeSuccess(name = "bezier" , success = false)
            return
        }

        logger.info("Got All Bezier")

        changeSuccess(name = "bezier" , success = true)

        logger.info("Create Animation Curves Settings")

        parseAnimation(animation = allOtherSettings).getOrElse {
            changeSuccess(name = "animation" , success = false)
            return
        }

        logger.info("Got All Animation")

        changeSuccess(name = "animation" , success = true)

        logger.info("Create keywords setting")

        parseKeywords(allSettings = allOtherSettings)
    }

    private fun changeSuccess(name: String ,success: Boolean) {
        successRateOfParse.forEach {
            if (it.name == name) {
                it.success = success
            }
        }
    }
}