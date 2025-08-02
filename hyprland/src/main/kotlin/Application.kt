package org.dot.config

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.path
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.dot.config.controller.Initialize
import org.dot.config.view.ui.handleHelpUI
import org.dot.config.view.ui.handleUI
import org.dot.config.view.ui.mainUI
import org.dot.config.view.ui.updateCustomUI.handleAnimationTree
import org.dot.config.view.ui.updateCustomUI.handleEnv
import org.dot.config.view.ui.updateCustomUI.handleExecutes
import org.dot.config.view.ui.updateCustomUI.handleKeybinds
import org.dot.config.view.ui.updateCustomUI.handleMonitors
import org.dot.config.view.ui.updateCustomUI.handleVariables
import org.dot.config.view.ui.updateCustomUI.handleWorkspace
import org.slf4j.event.Level
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

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

    Initialize

    routing {
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
    }
}
