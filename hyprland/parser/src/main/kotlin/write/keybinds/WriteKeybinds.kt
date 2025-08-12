package write.keybinds

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.BindModel
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

class WriteKeybinds : WriteIntoInterface<List<BindModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<BindModel>): Result<Boolean> {
        logger.info("Write Keybinds Into Hyprland")

        val bind = Path.of(loader.loadSingleHyprlandPath("bind")!!)

        if (!bind.exists()) {
            TODO("Result option throw")
        }

        val binds = mutableListOf<String>()

        hypr.forEach {
            val joinString = if (it.flags.contains('s')) "&" else " "

            binds.add("bind${it.flags} = ${it.mod.joinToString(joinString)} ,${it.key.joinToString(joinString)}${if (it.description != null) " ,${it.description}" else ""} ,${it.dispatcher}${if (it.args != null) " ,${it.args}" else ""}")
        }

        logger.info("Write into hyprland keybind. There are ${binds.size} keybinds")

        bind.writeText(binds.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<BindModel>): Result<Boolean> {
        logger.info("Write Keybind Into Dot Config File")

        val bindPath = Path.of(loader.loadSingleDotConfPath("keybind")!!)

        if (!bindPath.exists()) {
            TODO("Result option throw")
        }

        val bindDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into dot.config keybind. There are ${bindDF.size().nrow} keybind")

        bindDF.writeCsv(bindPath)

        return Result.success(true)
    }

}