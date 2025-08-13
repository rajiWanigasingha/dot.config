package write.permission

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.PermissionModel
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

class WritePermission : WriteIntoInterface<List<PermissionModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<PermissionModel>): Result<Boolean> {

        logger.info("Write Permission Into Hyprland")

        val permission = Path.of(loader.loadSingleHyprlandPath("permission")!!)

        if (!permission.exists()) {
            TODO("Result option throw")
        }

        val hyprlandPermission = mutableListOf<String>()

        hypr.forEach { hyprlandPermission.add("permission = ${it.regex} ,${it.permission} ,${it.mode}") }

        logger.info("Write into hyprland permission. There are ${hyprlandPermission.size} permission")

        permission.writeText(hyprlandPermission.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<PermissionModel>): Result<Boolean> {
        logger.info("Write Permission Into Dot.config")

        val permissionDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        val path = loader.loadSingleDotConfPath("permission")

        if (path == null) {
            TODO("Result option throw")
        }

        logger.info("Write into dot.config permission. There are ${permissionDF.size().nrow} permission")

        permissionDF.writeCsv(path)

        return Result.success(true)
    }
}