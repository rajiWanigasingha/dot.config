package write.workspace

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.WorkspaceModel
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.jetbrains.kotlinx.dataframe.size
import org.slf4j.LoggerFactory
import write.WriteIntoInterface
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

class WriteWorkspace : WriteIntoInterface<List<WorkspaceModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<WorkspaceModel>): Result<Boolean> {
        logger.info("Write Workspace Into Hyprland")

        val workspace = Path.of(loader.loadSingleHyprlandPath("workspace")!!)

        if (!workspace.exists()) {
            TODO("Result option throw")
        }

        val hyprlandWorkspace = mutableListOf<String>()

        hypr.forEach {
            hyprlandWorkspace.add("workspace = ${it.name} ,${it.rules.joinToString(" ,")}")
        }

        logger.info("Write into hyprland workspace. There are ${hyprlandWorkspace.size} workspace")

        workspace.writeText(hyprlandWorkspace.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<WorkspaceModel>): Result<Boolean> {
        logger.info("Write Workspace Into Dot Config")

        val workspace = Path.of(loader.loadSingleDotConfPath("workspace")!!)

        if (!workspace.exists()) {
            TODO("Result option throw")
        }

        val hyprlandWorkspace = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into workspace dot config. There are ${hyprlandWorkspace.size().nrow} workspace")

        hyprlandWorkspace.writeCsv(workspace)

        return Result.success(true)
    }
}