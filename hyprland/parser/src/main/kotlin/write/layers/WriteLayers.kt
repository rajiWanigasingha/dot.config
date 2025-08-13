package write.layers

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.LayerRulesModel
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

class WriteLayers : WriteIntoInterface<List<LayerRulesModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<LayerRulesModel>): Result<Boolean> {
        logger.info("Write Layer Into Hyprland")

        val layer = Path.of(loader.loadSingleHyprlandPath("layer")!!)

        if (!layer.exists()) {
            TODO("Result option throw")
        }

        val hyprlandLayer = mutableListOf<String>()

        hypr.forEach {
            hyprlandLayer.add("layerrule = ${it.rule} ,${it.value}")
        }

        logger.info("Write into hyprland layer. There are ${hyprlandLayer.size} layer")

        layer.writeText(hyprlandLayer.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<LayerRulesModel>): Result<Boolean> {
        logger.info("Write Env Into Dot Config")

        val env = Path.of(loader.loadSingleDotConfPath("layer")!!)

        if (!env.exists()) {
            TODO("Result option throw")
        }

        val hyprlandLayer = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into layer dot config. There are ${hyprlandLayer.size().nrow} layer")

        hyprlandLayer.writeCsv(env)

        return Result.success(true)
    }
}