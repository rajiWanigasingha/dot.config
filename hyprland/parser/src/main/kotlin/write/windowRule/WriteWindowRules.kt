package write.windowRule

import loadAndCreateDefaults.LoadAndCreateDefault
import model.tables.WindowRulesForDataFrame
import model.tables.WindowRulesModel
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

class WriteWindowRules : WriteIntoInterface<List<WindowRulesModel>> {

    private val loader = LoadAndCreateDefault()
    private val logger = LoggerFactory.getLogger(javaClass.name)

    override fun writeIntoHyprland(hypr: List<WindowRulesModel>): Result<Boolean> {
        logger.info("Write Window Rules Into Hyprland")

        val windowRules = Path.of(loader.loadSingleHyprlandPath("windowRules")!!)

        if (!windowRules.exists()) {
            TODO("Result option throw")
        }

        val windowRuleDF = mutableListOf<WindowRulesForDataFrame>()

        hypr.forEach {

            val rules = mutableListOf<String>()

            it.rules.forEach { rule ->
                val ruleString = "${rule.keyword}${if (rule.value != null) " ${rule.value}" else ""}"
                rules.add(ruleString)
            }

            windowRuleDF.add(
                WindowRulesForDataFrame(
                    rules = rules,
                    params = it.params
                )
            )
        }

        val hyprlandWindowRules = mutableListOf<String>()

        windowRuleDF.forEach { hyprlandWindowRules.add("windowrule = ${it.rules.joinToString(" ")} ,${it.params.joinToString(",")}") }

        logger.info("Write into hyprland window rules. There are ${hyprlandWindowRules.size} window rule")

        windowRules.writeText(hyprlandWindowRules.joinToString("\n"))

        return Result.success(true)
    }

    override fun writeIntoDotConfig(conf: List<WindowRulesModel>): Result<Boolean> {
        logger.info("Write Window Rules Into Dot Config")

        val windowRules = Path.of(loader.loadSingleDotConfPath("windowRules")!!)

        if (!windowRules.exists()) {
            TODO("Result option throw")
        }

        val windowRuleDF = mutableListOf<WindowRulesForDataFrame>()

        conf.forEach {

            val rules = mutableListOf<String>()

            it.rules.forEach { rule ->
                val ruleString = "${rule.keyword}${if (rule.value != null) " ${rule.value}" else ""}"
                rules.add(ruleString)
            }

            windowRuleDF.add(
                WindowRulesForDataFrame(
                    rules = rules,
                    params = it.params
                )
            )
        }

        val windowRuleForDF = windowRuleDF.toDataFrame().convert { all() }.with { it.toString() }

        logger.info("Write into dot config window rules. There are ${windowRuleForDF.size().nrow} window rule")

        windowRuleForDF.writeCsv(windowRules)

        return Result.success(true)
    }
}