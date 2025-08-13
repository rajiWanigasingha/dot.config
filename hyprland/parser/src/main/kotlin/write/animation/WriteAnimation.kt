package write.animation

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.AnimationModel
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

class WriteAnimation : WriteIntoInterface<List<AnimationModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<AnimationModel>): Result<Boolean> {

        logger.info("Write Animation Into Hyprland")

        val animation = Path.of(loader.loadSingleHyprlandPath("animation")!!)

        if (!animation.exists()) {
            TODO("Result option throw")
        }

        val hyprlandAnimation = mutableListOf<String>()

        hypr.forEach { hyprlandAnimation.add("animation = ${it.name}, ${it.active}${if (it.speed != null) ", ${it.speed}" else ""}${if (it.bezier != null) ", ${it.bezier}" else ""}${if (it.animation != null) ", ${it.animation}" else ""}") }

        logger.info("Write into hyprland animation. There are ${hyprlandAnimation.size} animation")

        animation.writeText(hyprlandAnimation.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<AnimationModel>): Result<Boolean> {
        logger.info("Write animation Into Dot.config")

        val animationDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        val path = loader.loadSingleDotConfPath("animation")

        if (path == null) {
            TODO("Result option throw")
        }

        logger.info("Write into dot.config animation. There are ${animationDF.size().nrow} animation")

        animationDF.writeCsv(path)

        return Result.success(true)
    }
}