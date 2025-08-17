package read.readHyprlandParseNeed

import RequestPaths
import model.tables.HyprlandCreateTable
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.isEmpty
import org.jetbrains.kotlinx.dataframe.api.toListOf
import org.jetbrains.kotlinx.dataframe.io.readCsv
import java.time.ZoneId
import kotlin.io.path.getLastModifiedTime

class ReadHyprlandParseNeed {

    private val request = RequestPaths()

    fun readParseNeed(): Boolean {
        val hyprlandPath = request.getHyprlandDotConfigPath()

        val getDataFrame = DataFrame.readCsv(hyprlandPath)

        if (getDataFrame.isEmpty()) {
            return false
        }

        val df = getDataFrame.toListOf<HyprlandCreateTable>()

        val currentParsedSettingsFolder = request.allCreatedFilesOnHyprland()

        val pathStore = mutableListOf<HyprlandCreateTable>()

        currentParsedSettingsFolder?.forEach {
            val modifyTime = it.getLastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

            pathStore.add(
                HyprlandCreateTable(
                    name = it.toString(),
                    date = modifyTime.toString()
                )
            )
        }

        return df == pathStore
    }

}