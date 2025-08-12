package read.readHelpers

import RequestPaths
import model.helpers.FlagTable
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Read Flags")

fun readFlags(): List<FlagTable> {

    logger.info("Read All Dispatchers")

    val pathToFlags = RequestPaths().getFlags()

    if (pathToFlags == null) {
        TODO("Error Throw")
    }

    val data = DataFrame.readCsv(pathToFlags).convert { all() }.with { it.toString() }

    val allFlags = mutableListOf<FlagTable>()

    data.forEach { row ->
        allFlags.add(
            FlagTable(
                name = row["name"].toString(),
                settingsName = row["settingsName"].toString(),
                description = row["description"].toString(),
                have = false,
            )
        )
    }

    return allFlags.toList()
}