import controllers.gatherHyprland
import controllers.parseExecute
import controllers.parseMonitor
import controllers.parseVariables
import controllers.parseWindowRules
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

        parseWindowRules(windows = allOtherSettings)
    }

    private fun changeSuccess(name: String ,success: Boolean) {
        successRateOfParse.forEach {
            if (it.name == name) {
                it.success = success
            }
        }
    }
}