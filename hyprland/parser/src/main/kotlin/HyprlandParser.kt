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
import loadAndCreateDefaults.LoadAndCreateDefault
import model.BackupModel
import model.ParsedModels
import model.ParsingStep
import model.ParsingSteps
import model.tables.HyprlandCreateTable
import org.slf4j.LoggerFactory
import read.readHyprlandParseNeed.ReadHyprlandParseNeed
import write.hyprland.WriteHyprland
import java.nio.file.Path
import java.time.ZoneId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.writeText
import kotlin.io.path.copyTo
import kotlin.io.path.name

internal val logger = LoggerFactory.getLogger("Hyprland Parser :")

/**
 * HyprlandParser is responsible for gathering and parsing configuration data for Hyprland,
 * handling settings related to variables, keybinds, monitors, auto-start scripts, window rules, workspaces,
 * environments, layers, unbinding, and submaps.
 * The parsed data is processed and serialized for communication over a WebSocket session.
 *
 * @constructor Initializes the HyprlandParser with a WebSocket session.
 * @param session The WebSocket session used for communication with a client.
 */
class HyprlandParser(private val session: DefaultWebSocketServerSession) {

    init {
        LoadAndCreateDefault().checkForDefaultPath()
    }


    /**
     * Parses various configuration settings related to Hyprland by gathering, analyzing, and writing
     * specific configurations including variables, keybinds, monitors, auto-start scripts, window rules,
     * workspaces, environments, layers, unbinding, and submaps.
     *
     * The method performs the parsing in a step-by-step manner for different configuration categories.
     * If any step fails, it logs the failure and returns a failure result. If all steps are successful,
     * it returns a success result, and it will send serialized data to the client via the `session` parameter.
     *
     * @return A [Result] indicating the success or failure of the configuration parsing process.
     *         On success, it contains `true`. On failure, it includes the encountered exception.
     */
    suspend fun parseConfig(): Result<Boolean> {

        logger.info("Begin Parsing hyprland settings")

        logger.info("Gather All Settings From All Source File")

        val gatherAllHyprland = gatherHyprland().getOrElse {
            session.sendSerialized(
                data = ParsingSteps(
                    step = ParsingStep.PARSE,
                    parse = ParsedModels(
                        name = "Gather All Settings",
                        success = false,
                        description = "Gather all settings from hyprland.config and all other source files.",
                        found = 0
                    )
                )
            )

            return Result.failure(it)
        }

        session.sendSerialized(
            data = ParsingSteps(
                step = ParsingStep.PARSE,
                parse = ParsedModels(
                    name = "Gather All Settings",
                    success = true,
                    description = "Gather all settings from hyprland.config and all other source files.",
                    found = gatherAllHyprland.size
                )
            )
        )

        logger.info("Create Variable Settings")

        var allOtherSettings = listOf<String>()

        parseVariables(allSettings = gatherAllHyprland)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Variables",
                            success = true,
                            description = "Parse and write all variables to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )

                allOtherSettings = it
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Variables",
                            success = false,
                            description = "Parse and write all variables to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Variables")

        logger.info("Create Keybind Settings")

        parserKeybinds(keyBinds = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Keybinds",
                            success = true,
                            description = "Parse and write all keybinds to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Keybinds",
                            success = false,
                            description = "Parse and write all keybinds to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Keybinds")

        logger.info("Create monitor Settings")

        parseMonitor(monitors = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Monitor",
                            success = true,
                            description = "Parse and write all monitor to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Monitor",
                            success = false,
                            description = "Parse and write all monitor to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Monitor")

        logger.info("Create Auto Start Settings")

        parseExecute(execute = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Auto Start",
                            success = true,
                            description = "Parse and write all auto start to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Auto Start",
                            success = false,
                            description = "Parse and write all auto start to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Auto Start")

        logger.info("Create Window Rules")

        parseWindowRules(windows = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Window Rules",
                            success = true,
                            description = "Parse and write all window rules to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Window Rules",
                            success = false,
                            description = "Parse and write all window rules to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Window Rules")

        logger.info("Create Workspace Settings")

        parseWorkspace(workspace = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Workspace",
                            success = true,
                            description = "Parse and write all workspace to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Workspace",
                            success = false,
                            description = "Parse and write all workspace to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Workspace Rules")

        logger.info("Create Env Settings")

        parseEnv(env = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Environment",
                            success = true,
                            description = "Parse and write all environment to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Environment",
                            success = false,
                            description = "Parse and write all environment to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Env Rules")

        logger.info("Create Layer Settings")

        parseLayers(layer = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Layer",
                            success = true,
                            description = "Parse and write all layer to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Layer",
                            success = false,
                            description = "Parse and write all layer to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }


        logger.info("Got All Layer Rules")

        logger.info("Create Unbind Settings")

        parseUnbind(unbind = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Unbind",
                            success = true,
                            description = "Parse and write all unbind to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Unbind",
                            success = false,
                            description = "Parse and write all unbind to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All unbind")

        logger.info("Create Submap Settings")

        parseSubmap(submap = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Submap",
                            success = true,
                            description = "Parse and write all submap to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Submap",
                            success = false,
                            description = "Parse and write all submap to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All unbind")

        logger.info("Create Permission Settings")

        parsePermissions(permission = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Permission",
                            success = true,
                            description = "Parse and write all permission to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Permission",
                            success = false,
                            description = "Parse and write all permission to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Permission")

        logger.info("Create Bezier Curves Settings")

        parseBezier(bezier = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Bezier",
                            success = true,
                            description = "Parse and write all bezier to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Bezier",
                            success = false,
                            description = "Parse and write all bezier to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Bezier")

        logger.info("Create Animation Curves Settings")

        parseAnimation(animation = allOtherSettings)
            .onSuccess {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Animation",
                            success = true,
                            description = "Parse and write all animation to hyprland source file and dot.config.hyprland store.",
                            found = it.size
                        )
                    )
                )
            }
            .onFailure {
                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.PARSE,
                        parse = ParsedModels(
                            name = "Parse All Animation",
                            success = false,
                            description = "Parse and write all animation to hyprland source file and dot.config.hyprland store.",
                            found = 0
                        )
                    )
                )
                return Result.failure(it)
            }

        logger.info("Got All Animation")

        logger.info("Create keywords setting")

        parseKeywords(allSettings = allOtherSettings, session = session)

        return Result.success(true)
    }

    /**
     * Creates a log for all files found in the Hyprland directory.
     *
     * This method retrieves all file paths from the directory specified for Hyprland output
     * using the `allCreatedFilesOnHyprland` function. If files are found, it logs their paths.
     * This functionality is used to either validate or inspect the output files generated
     * in the Hyprland configuration pipeline.
     *
     * It is part of the HyprlandParser's operations and works in coordination with other
     * methods that manage Hyprland configurations.
     */
    suspend fun createHyprlandFile(): Result<Boolean> {

        val hyprlandPaths = RequestPaths().allCreatedFilesOnHyprland()

        var pathStore = mutableListOf<HyprlandCreateTable>()

        hyprlandPaths?.forEach {

            val modifyTime = it.getLastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

            pathStore.add(
                HyprlandCreateTable(
                    name = it.toString(),
                    date = modifyTime.toString()
                )
            )
        }

        val bezierList = pathStore.filter { it.name.contains("bezier") }
        val bezierNotList = pathStore.filterNot { it.name.contains("bezier") }

        pathStore = (bezierList + bezierNotList) as MutableList<HyprlandCreateTable>

        val write = WriteHyprland()

        write.writeIntoHyprland(pathStore).getOrElse {
            session.sendSerialized(
                data = ParsingSteps(
                    step = ParsingStep.CREATE_HYPRLAND,
                    createHyprland = false
                )
            )

            return Result.failure(it)
        }

        write.writeIntoDotConfig(pathStore).getOrElse {
            session.sendSerialized(
                data = ParsingSteps(
                    step = ParsingStep.CREATE_HYPRLAND,
                    createHyprland = false
                )
            )

            return Result.failure(it)
        }

        session.sendSerialized(
            data = ParsingSteps(
                step = ParsingStep.CREATE_HYPRLAND,
                createHyprland = true
            )
        )

        return Result.success(true)
    }

    /**
     * Creates a backup of Hyprland-related files and configurations.
     *
     * This method performs the following steps to ensure backups are properly created:
     * - Logs the initiation of the backup process.
     * - Determines the current timestamp to uniquely identify the backup directory.
     * - Creates a new backup directory using the timestamp.
     * - Writes the timestamp to a configuration file (`backup.conf`) in the backup directory.
     * - Retrieves all files generated in the Hyprland configuration directory via the `allCreatedFilesOnHyprland` method.
     * - Copies each retrieved file to the new backup directory.
     * - Logs the success or failure of each file copy operation.
     *
     * If files fail to copy, error messages are logged, but the process continues for the remaining files.
     *
     * Dependencies:
     * - `RequestPaths` for getting Hyprland file paths and backup paths.
     * - `logger` for logging backup operations.
     */
    suspend fun createBackups() {
        logger.info("Create Backups")

        val timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val backup = RequestPaths().getBackupPath()

        val newBackup = Path.of("${backup}/$timeNow")

        newBackup.createDirectory()

        Path.of("$backup/backup.conf").writeText(timeNow)

        val hyprlandPaths = RequestPaths().allCreatedFilesOnHyprland()

        var error = false

        hyprlandPaths?.forEach {
            runCatching {
                it.copyTo(Path.of("$newBackup/${it.fileName}"))
            }.onSuccess { _ ->
                logger.info("Backup created for: ${it.fileName}")
            }.onFailure { e ->
                logger.error("Failed to create backup for ${it.fileName}: ${e.message}")

                error = true

                session.sendSerialized(
                    data = ParsingSteps(
                        step = ParsingStep.CREATE_BACKUP,
                        createBackup = BackupModel(
                            file = it.toString(),
                            success = false
                        )
                    )
                )
            }
        }

        if (!error) {
            session.sendSerialized(
                data = ParsingSteps(
                    step = ParsingStep.CREATE_BACKUP,
                    createBackup = BackupModel(
                        file = newBackup.toString(),
                        success = true
                    )
                )
            )
        }
    }


    fun checkForParsing(): Boolean {
        logger.info("Check For Need To Parse Parsing")

        return ReadHyprlandParseNeed().readParseNeed()
    }

}