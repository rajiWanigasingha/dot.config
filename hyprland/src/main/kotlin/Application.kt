package org.dot.config

import HyprlandParser
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import model.ParsingStep
import model.ParsingSteps
import org.dot.config.model.SendAndReceive
import org.dot.config.view.ui.handleHelpUI
import org.dot.config.view.ui.handleUI
import org.dot.config.view.ui.mainUI
import org.dot.config.view.ui.updateCustomUI.handleAnimationTree
import org.dot.config.view.ui.updateCustomUI.handleEnv
import org.dot.config.view.ui.updateCustomUI.handleExecutes
import org.dot.config.view.ui.updateCustomUI.handleKeybinds
import org.dot.config.view.ui.updateCustomUI.handleMonitors
import org.dot.config.view.ui.updateCustomUI.handleVariables
import org.dot.config.view.ui.updateCustomUI.handleWindow
import org.dot.config.view.ui.updateCustomUI.handleWorkspace
import org.slf4j.event.Level
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

private val logger = org.slf4j.LoggerFactory.getLogger("Application")

@OptIn(ExperimentalSerializationApi::class)
fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {

        webSocket("/init") {

            val hyprlandParser = HyprlandParser(session = this)

            logger.info("Initiate Hyprland Parser")

            val needToParse = hyprlandParser.checkForParsing()

            if (!needToParse) {
                sendSerialized(data = ParsingSteps(
                    step = ParsingStep.NEED,
                    need = false
                ))
            } else {
                sendSerialized(data = ParsingSteps(
                    step = ParsingStep.NEED,
                    need = true
                ))
                close(CloseReason(CloseReason.Codes.NORMAL, "No need to parse"))
                return@webSocket
            }

            val result = hyprlandParser.parseConfig().getOrNull()

            if (result == null) {
                close(CloseReason(CloseReason.Codes.INTERNAL_ERROR ,"Couldn't parse config"))
            }

            val conformation = receiveDeserialized<SendAndReceive.Confirmation>()

            if (conformation.hypr) {
                hyprlandParser
                    .createHyprlandFile()
                    .onSuccess {
                        hyprlandParser.createBackups()
                    }
            } else {
                close(CloseReason(CloseReason.Codes.NORMAL ,"Don't create hyprland file"))
                return@webSocket
            }
        }


        handleUI()
        mainUI()
        handleHelpUI()

        handleVariables()
        handleExecutes()
        handleEnv()
        handleKeybinds()
        handleMonitors()
        handleAnimationTree()
        handleWorkspace()
        handleWindow()
    }
}
