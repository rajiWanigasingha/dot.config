package org.hyprconfig.parseSingleSettings

import org.hyprconfig.handelProcessSettings.handleBindKeyword
import org.hyprconfig.model.BindModel
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.readText

private val logger = LoggerFactory.getLogger("Re parsing Bind:")

fun reParsingBind(): MutableList<BindModel> {
    logger.info("Parsing all the Bind")

    val path = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bind.conf"

    val allSettings = Path.of(path).readText().split("\n").map { it.trim() }

    val bindSettings = handleBindKeyword(allSettings)

    return bindSettings
}