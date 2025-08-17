package write.hyprland

import RequestPaths
import model.tables.HyprlandCreateTable
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import write.WriteIntoInterface
import kotlin.io.path.writeText


/**
 * Class responsible for writing Hyprland configuration data to different output formats.
 * Implements WriteIntoInterface for handling List<HyprlandCreate> data.
 */
class WriteHyprland : WriteIntoInterface<List<HyprlandCreateTable>> {

    private val request = RequestPaths()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    /**
     * Writes the provided Hyprland configuration sources into the Hyprland configuration file.
     * Creates a source entry for each HyprlandCreate item.
     *
     * @param hypr List of HyprlandCreate objects containing configuration data
     * @return Result<Boolean> indicating success (true) or failure with exception
     */
    override fun writeIntoHyprland(hypr: List<HyprlandCreateTable>): Result<Boolean> {
        logger.info("Write All Source File Into Hyprland")

        val listOfSources = mutableListOf<String>()

        hypr.forEach {
            listOfSources.add("source = ${it.name}")
        }


        val hyprlandPath = request.getHyprlandPath() ?: return Result.failure(Exception("Hyprland Path is null"))

        hyprlandPath.writeText(listOfSources.joinToString("\n"))

        return Result.success(true)
    }

    /**
     * Writes the provided Hyprland configuration into the dot config directory as CSV.
     * Converts the configuration list to a DataFrame before writing.
     *
     * @param conf List of HyprlandCreate objects to be written
     * @return Result<Boolean> indicating success (true) or failure
     */
    override fun writeIntoDotConfig(conf: List<HyprlandCreateTable>): Result<Boolean> {
        logger.info("Write All Source File Into Dot Config Paths")

        val df = conf.toDataFrame().convert { all() }.with { it.toString() }

        df.writeCsv(request.getHyprlandDotConfigPath())

        return Result.success(true)
    }
}