package write.submap

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.SubmapModel
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

class WriteSubmap : WriteIntoInterface<List<SubmapModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<SubmapModel>): Result<Boolean> {
        logger.info("Write Submap Into Hyprland")

        val submap = Path.of(loader.loadSingleHyprlandPath("submap")!!)

        if (!submap.exists()) {
            TODO("Result option throw")
        }

        val hyprlandSubmap = mutableListOf<String>()

        hypr.forEach {
            hyprlandSubmap.add("submap = ${it.submap}")
        }

        logger.info("Write into hyprland submap. There are ${hyprlandSubmap.size} submap")

        submap.writeText(hyprlandSubmap.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<SubmapModel>): Result<Boolean> {
        logger.info("Write submap Into Dot Config")

        val submap = Path.of(loader.loadSingleDotConfPath("submap")!!)

        if (!submap.exists()) {
            TODO("Result option throw")
        }

        val hyprlandSubmap = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into submap dot config. There are ${hyprlandSubmap.size().nrow} submap")

        hyprlandSubmap.writeCsv(submap)

        return Result.success(true)
    }
}