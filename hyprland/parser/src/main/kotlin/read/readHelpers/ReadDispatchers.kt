package read.readHelpers

import RequestPaths
import model.helpers.DispatcherTable
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.forEach
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.readCsv
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Read Dispatchers")

fun readDispatchers(): List<DispatcherTable> {

    logger.info("Read All Dispatchers")

    val pathToDispatcher = RequestPaths().getDispatcherPath()

    if (pathToDispatcher == null) {
        TODO("Error Throw")
    }

    val data = DataFrame.readCsv(pathToDispatcher).convert { all() }.with { it.toString() }

    val allDispatchers = mutableListOf<DispatcherTable>()

    data.forEach { row ->
        allDispatchers.add(
            DispatcherTable(
                name = row["name"].toString(),
                command = row["command"].toString(),
                description = row["description"].toString(),
            )
        )
    }

    return allDispatchers.toList()
}