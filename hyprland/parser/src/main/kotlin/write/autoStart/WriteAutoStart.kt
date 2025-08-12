package write.autoStart

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.ExecuteModel
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

class WriteAutoStart : WriteIntoInterface<List<ExecuteModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<ExecuteModel>): Result<Boolean> {
        logger.info("Write auto start Into Hyprland")

        val autoStart = Path.of(loader.loadSingleHyprlandPath("autoStart")!!)

        if (!autoStart.exists()) {
            TODO("Result option throw")
        }

        val autoStartStore = mutableListOf<String>()

        hypr.forEach {
            autoStartStore.add("${it.keyword} = ${it.command}")
        }

        logger.info("Write into hyprland auto start. There are ${autoStartStore.size} auto start")

        autoStart.writeText(autoStartStore.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<ExecuteModel>): Result<Boolean> {
        logger.info("Write auto start Into Dot.Config")

        val autoStart = Path.of(loader.loadSingleDotConfPath("autoStart")!!)

        if (!autoStart.exists()) {
            TODO("Result option throw")
        }

        val autoStartDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into dot config auto start. There are ${autoStartDF.size().nrow} auto start")

        autoStartDF.writeCsv(autoStart)

        return Result.success(true)
    }
}