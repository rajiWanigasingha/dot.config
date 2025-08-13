package write.unbind

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.UnbindModel
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

class WriteUnbind : WriteIntoInterface<List<UnbindModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<UnbindModel>): Result<Boolean> {
        logger.info("Write Unbind Into Hyprland")

        val unbind = Path.of(loader.loadSingleHyprlandPath("unbind")!!)

        if (!unbind.exists()) {
            TODO("Result option throw")
        }

        val hyprlandUnbind = mutableListOf<String>()

        hypr.forEach {
            hyprlandUnbind.add("unbind = ${it.mods.joinToString(" ")} ,${it.key.joinToString(" ")}")
        }

        logger.info("Write into hyprland unbind. There are ${hyprlandUnbind.size} unbind")

        unbind.writeText(hyprlandUnbind.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<UnbindModel>): Result<Boolean> {
        logger.info("Write unbind Into Dot Config")

        val unbind = Path.of(loader.loadSingleDotConfPath("unbind")!!)

        if (!unbind.exists()) {
            TODO("Result option throw")
        }

        val hyprlandUnbinds = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into unbind dot config. There are ${hyprlandUnbinds.size().nrow} unbind")

        hyprlandUnbinds.writeCsv(unbind)

        return Result.success(true)
    }
}