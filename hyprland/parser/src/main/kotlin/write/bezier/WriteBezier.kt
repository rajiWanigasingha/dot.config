package write.bezier

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.BezierModel
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

class WriteBezier : WriteIntoInterface<List<BezierModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<BezierModel>): Result<Boolean> {

        logger.info("Write Bezier Into Hyprland")

        val bezier = Path.of(loader.loadSingleHyprlandPath("bezier")!!)

        if (!bezier.exists()) {
            TODO("Result option throw")
        }

        val hyprlandBezier = mutableListOf<String>()

        hypr.forEach { hyprlandBezier.add("bezier = ${it.name}, ${it.x0} ,${it.y0} ,${it.x1} ,${it.y1}") }

        logger.info("Write into hyprland bezier. There are ${hyprlandBezier.size} bezier")

        bezier.writeText(hyprlandBezier.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<BezierModel>): Result<Boolean> {
        logger.info("Write bezier Into Dot.config")

        val bezierDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        val path = loader.loadSingleDotConfPath("bezier")

        if (path == null) {
            TODO("Result option throw")
        }

        logger.info("Write into dot.config bezier. There are ${bezierDF.size().nrow} bezier")

        bezierDF.writeCsv(path)

        return Result.success(true)
    }
}