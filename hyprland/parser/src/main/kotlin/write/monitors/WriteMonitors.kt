package write.monitors

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.MonitorModel
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

class WriteMonitors : WriteIntoInterface<List<MonitorModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<MonitorModel>): Result<Boolean> {
        logger.info("Write monitor Into Hyprland")

        val monitor = Path.of(loader.loadSingleHyprlandPath("monitor")!!)

        if (!monitor.exists()) {
            TODO("Result option throw")
        }

        val monitorStore = mutableListOf<String>()

        hypr.forEach {
            if (it.disable) {
                monitorStore.add("monitor = ${it.name} ,disable")
                return@forEach
            } else if (it.addreserved && it.addreservedValue != null) {
                monitorStore.add("monitor = ${it.name} ,addreserved ,${it.addreservedValue?.top} ,${it.addreservedValue?.bottom} ,${it.addreservedValue?.left} ,${it.addreservedValue?.right}")
                return@forEach
            } else {

                val mirror = if (it.mirror != null) " ,mirror ,${it.mirror}" else ""
                val bitdepth = if (it.bitDepth != null) " ,bitdepth ,${it.bitDepth}" else ""
                val transform = if (it.transform != null) " ,transform ,${it.transform}" else ""
                val cm = if (it.cm != null) " ,cm ,${it.cm}" else ""
                val sdrbrightness = if (it.sdrbrightness != null) " ,sdrbrightness ,${it.sdrbrightness}" else ""
                val sdrsaturation = if (it.sdrsaturation != null) " ,sdrsaturation ,${it.sdrsaturation}" else ""
                val vvr = if (it.vvr != null) " ,vrr ,${it.vvr}" else ""
                val workspace = if (it.workspace != null) " ,workspace ,${it.workspace}" else ""

                monitorStore.add("monitor = ${it.name} ,${it.resolution} ,${it.position} ,${it.scale}${mirror}${bitdepth}${transform}${cm}${sdrbrightness}${sdrsaturation}${vvr}${workspace}")
            }
        }

        logger.info("Write into hyprland monitor. There are ${monitorStore.size} monitor settings")

        monitor.writeText(monitorStore.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<MonitorModel>): Result<Boolean> {
        logger.info("Write monitor Into dot.config")

        val monitor = Path.of(loader.loadSingleDotConfPath("monitor")!!)

        if (!monitor.exists()) {
            TODO("Result option throw")
        }

        val monitorDF = conf.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into dot.config monitor. There are ${monitorDF.size().nrow} monitor")

        monitorDF.writeCsv(monitor)

        return Result.success(true)
    }
}