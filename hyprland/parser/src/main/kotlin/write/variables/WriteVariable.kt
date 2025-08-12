package write.variables

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.VariableModel
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

class WriteVariable : WriteIntoInterface<List<VariableModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<VariableModel>): Result<Boolean> {

        logger.info("Write Variables Into Hyprland")

        val variable = Path.of(loader.loadSingleHyprlandPath("variable")!!)

        if (!variable.exists()) {
            TODO("Result option throw")
        }

        val hyprlandVariables = mutableListOf<String>()

        hypr.forEach { hyprlandVariables.add("${it.name} = ${it.value}") }

        logger.info("Write into hyprland variables. There are ${hyprlandVariables.size} variables")

        variable.writeText(hyprlandVariables.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<VariableModel>): Result<Boolean> {

        logger.info("Write Variables Into Dot.config")

        val variableDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        val path = loader.loadSingleDotConfPath("variable")

        if (path == null) {
            TODO("Result option throw")
        }

        logger.info("Write into dot.config variables. There are ${variableDF.size().nrow} variables")

        variableDF.writeCsv(path)

        return Result.success(true)
    }

}