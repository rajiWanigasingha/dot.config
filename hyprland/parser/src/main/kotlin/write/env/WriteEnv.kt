package write.env

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.EnvModel
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

class WriteEnv : WriteIntoInterface<List<EnvModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<EnvModel>): Result<Boolean> {
        logger.info("Write Env Into Hyprland")

        val env = Path.of(loader.loadSingleHyprlandPath("env")!!)

        if (!env.exists()) {
            TODO("Result option throw")
        }

        val hyprlandEnv = mutableListOf<String>()

        hypr.forEach {
            hyprlandEnv.add("env = ${it.env}")
        }

        logger.info("Write into hyprland env. There are ${hyprlandEnv.size} env")

        env.writeText(hyprlandEnv.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<EnvModel>): Result<Boolean> {
        logger.info("Write Env Into Dot Config")

        val env = Path.of(loader.loadSingleDotConfPath("env")!!)

        if (!env.exists()) {
            TODO("Result option throw")
        }

        val hyprlandEnv = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into env dot config. There are ${hyprlandEnv.size().nrow} env")

        hyprlandEnv.writeCsv(env)

        return Result.success(true)
    }
}