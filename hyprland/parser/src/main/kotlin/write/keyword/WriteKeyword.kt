package write.keyword

import RequestPaths
import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.StandedKeywordModel
import model.tables.StandedKeywordParseModel
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.api.add
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.insert
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.update
import org.jetbrains.kotlinx.dataframe.api.where
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.jetbrains.kotlinx.dataframe.size
import org.slf4j.LoggerFactory
import write.WriteIntoInterface
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

class WriteKeyword(val hyprPath: List<String>) :
    WriteIntoInterface<List<Pair<StandedKeywordModel, StandedKeywordParseModel>>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<Pair<StandedKeywordModel, StandedKeywordParseModel>>): Result<Boolean> {
        logger.info("Write ${hyprPath[0]} Into Hyprland")

        val keywordPath = Path.of(loader.loadSingleHyprlandPath(hyprPath[0])!!)

        val struct = RequestPaths().getHyprlandStruct(hyprPath[0])?.toMutableList()

        if (!keywordPath.exists() && struct == null) {
            logger.error("Path doesn't exist ,$keywordPath")
            TODO("Result option throw")
        }

        hypr.forEach { (stood, parse) ->

            val hyprlandName = parse.fileName.split(Regex("(?=[A-Z])")).last().lowercase()

            val index = struct!!.indexOfFirst { it == "$hyprlandName {" }

            if (index == -1) return@forEach

            struct.add(element = "${stood.name} = ${stood.value}", index = index + 1)
        }

        logger.info("Writing ${hypr.size} settings into ${hyprPath[0]} keyword settings")

        keywordPath.writeText(formatWithIndent(struct!!))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<Pair<StandedKeywordModel, StandedKeywordParseModel>>): Result<Boolean> {
        logger.info("Write ${hyprPath[0]} Into dot config")

        val request = RequestPaths()

        val paths = request.getPathsForKeyword(hyprPath[0])

        paths?.forEach {
            val defaults = Path.of("${request.getHyprlandKeywordPath()}${it}.csv")

            logger.info(defaults.toString())

            if (!defaults.exists()) {
                logger.error("Path doesn't exist in dot config store $defaults")
                TODO("Result option throw")
            }

            val validToThisFile = mutableListOf<StandedKeywordModel>()

            conf.forEach { (standed, parse) ->
                if (parse.fileName == it) {
                    validToThisFile.add(standed)
                }
            }

            val df = DataFrame.readCsv(defaults).convert { all() }.with { d -> d.toString() }

            val buildValidOnce = mutableListOf<DataRow<Any?>>()

            validToThisFile.forEach { valid ->
                val filteredDF = df.update { "value"<String>() }
                    .where { "settingsName"<String>() == valid.name }
                    .with { valid.value }
                    .filter { "settingsName"<String>() == valid.name }[0]

                buildValidOnce.add(filteredDF)
            }

            buildValidOnce.toDataFrame().convert { all() }.with { d -> d.toString() }
                .writeCsv(Path.of("${request.getHyprlandKeywordStore()}${it}.csv"))
        }

        return Result.success(true)
    }

    private fun formatWithIndent(lines: List<String>): String {
        var depth = 0
        return lines.joinToString("\n") { line ->
            when {
                line.trim() == "}" -> {
                    depth--
                    "  ".repeat(depth) + line
                }

                line.trim().endsWith("{") -> {
                    val indented = "  ".repeat(depth) + line
                    depth++
                    indented
                }

                else -> "  ".repeat(depth) + line
            }
        }
    }
}