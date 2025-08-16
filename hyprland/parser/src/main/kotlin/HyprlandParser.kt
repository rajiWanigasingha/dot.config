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
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import io.ktor.websocket.send
import model.ParsedModels
import org.slf4j.LoggerFactory

internal val logger = LoggerFactory.getLogger("Hyprland Parser :")

class HyprlandParser(private val session: DefaultWebSocketServerSession) {

    suspend fun parseConfig(): Result<Boolean> {

        logger.info("Begin Parsing hyprland settings")

        logger.info("Gather All Settings From All Source File")

        val gatherAllHyprland = gatherHyprland().getOrElse {
            session.sendSerialized(
                data = ParsedModels(
                    name = "Gather All Settings",
                    success = false,
                    description = "Gather all settings from hyprland.config and all other source files.",
                    found = 0
                )
            )
            return Result.failure(it)
        }

        session.sendSerialized(
            data = ParsedModels(
                name = "Gather All Settings",
                success = true,
                description = "Gather all settings from hyprland.config and all other source files.",
                found = gatherAllHyprland.size
            )
        )

        logger.info("Create Variable Settings")

        var allOtherSettings = listOf<String>()

        parseVariables(allSettings = gatherAllHyprland)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Variables",
                        success = true,
                        description = "Parse and write all variables to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )

                allOtherSettings = it
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Variables",
                        success = false,
                        description = "Parse and write all variables to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Variables")

        logger.info("Create Keybind Settings")

        parserKeybinds(keyBinds = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Keybinds",
                        success = true,
                        description = "Parse and write all keybinds to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Keybinds",
                        success = false,
                        description = "Parse and write all keybinds to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Keybinds")

        logger.info("Create monitor Settings")

        parseMonitor(monitors = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Monitor",
                        success = true,
                        description = "Parse and write all monitor to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Monitor",
                        success = false,
                        description = "Parse and write all monitor to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Monitor")

        logger.info("Create Auto Start Settings")

        parseExecute(execute = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Auto Start",
                        success = true,
                        description = "Parse and write all auto start to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Auto Start",
                        success = false,
                        description = "Parse and write all auto start to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Auto Start")

        logger.info("Create Window Rules")

        parseWindowRules(windows = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Window Rules",
                        success = true,
                        description = "Parse and write all window rules to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Window Rules",
                        success = false,
                        description = "Parse and write all window rules to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Window Rules")

        logger.info("Create Workspace Settings")

        parseWorkspace(workspace = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Workspace",
                        success = true,
                        description = "Parse and write all workspace to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Workspace",
                        success = false,
                        description = "Parse and write all workspace to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Workspace Rules")

        logger.info("Create Env Settings")

        parseEnv(env = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Environment",
                        success = true,
                        description = "Parse and write all environment to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Environment",
                        success = false,
                        description = "Parse and write all environment to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Env Rules")

        logger.info("Create Layer Settings")

        parseLayers(layer = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Layer",
                        success = true,
                        description = "Parse and write all layer to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Layer",
                        success = false,
                        description = "Parse and write all layer to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Layer Rules")

        logger.info("Create Unbind Settings")

        parseUnbind(unbind = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Unbind",
                        success = true,
                        description = "Parse and write all unbind to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Unbind",
                        success = false,
                        description = "Parse and write all unbind to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All unbind")

        logger.info("Create Submap Settings")

        parseSubmap(submap = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Submap",
                        success = true,
                        description = "Parse and write all submap to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Submap",
                        success = false,
                        description = "Parse and write all submap to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All unbind")

        logger.info("Create Permission Settings")

        parsePermissions(permission = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Permission",
                        success = true,
                        description = "Parse and write all permission to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Permission",
                        success = false,
                        description = "Parse and write all permission to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Permission")

        logger.info("Create Bezier Curves Settings")

        parseBezier(bezier = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Bezier",
                        success = true,
                        description = "Parse and write all bezier to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Bezier",
                        success = false,
                        description = "Parse and write all bezier to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Bezier")

        logger.info("Create Animation Curves Settings")

        parseAnimation(animation = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Animation",
                        success = true,
                        description = "Parse and write all animation to hyprland source file and dot.config.hyprland store.",
                        found = it.size
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsedModels(
                        name = "Parse All Animation",
                        success = false,
                        description = "Parse and write all animation to hyprland source file and dot.config.hyprland store.",
                        found = 0
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Animation")

        logger.info("Create keywords setting")

        parseKeywords(allSettings = allOtherSettings , session = session)

        return Result.success(true)
    }

    fun createHyprlandFile() {

        val hyprlandPaths = RequestPaths().allCreatedFilesOnHyprland()

        hyprlandPaths?.forEach { logger.info(it.toString()) }

    }

    fun createBackups() {}

}