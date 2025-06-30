package org.dot.config.view.ui

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.HelpPageController
import org.dot.config.controller.ui.SidebarController
import org.dot.config.model.SendAndReceive
import org.dot.config.view.errors.ErrorWebsocket
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket")

fun Route.handleHelpUI() {
    webSocket("/help") {
        Logger.info("Initialize web socket /help")

        val helpPageController = HelpPageController()

        sendSerialized(
            data = SendAndReceive.Send(
                actionType = SendAndReceive.ActionType.CONNECT,
                payload = "Connection Is Successful"
            )
        )

        Logger.info("send successful connection message /help")

        var connect = true

        while (connect) {
            runCatching {

                val helpRequest = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.ReceiveHelp>>()

                Logger.info("Receive Help Object $helpRequest")

                if (helpRequest.actionType != SendAndReceive.ActionType.HELP) throw ErrorWebsocket.InvalidActionTypeForHelp()

                val mdFile = helpPageController.getHelpUI(actionLinks = helpRequest.payload.actionLink , category = helpRequest.payload.category , name = helpRequest.payload.name)

                sendSerialized(
                    data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.HELP,
                        payload = mdFile
                    )
                )

                Logger.info("Send markdown file")

            }.onFailure { exception ->
                when (exception) {

                    is ClosedReceiveChannelException -> {
                        Logger.warn("Websocket is closed because -> ${closeReason.await()}")
                        connect = false
                    }

                    is SerializationException -> {
                        Logger.error("Couldn't parse into data class", exception)

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.ERROR,
                                payload = SendAndReceive.SendHelpError(
                                    code = SendAndReceive.HelpErrorCodes.SERIALIZABLE,
                                    errorMessage = exception.message.toString()
                                )
                            )
                        )
                    }

                    is ErrorWebsocket.InvalidActionTypeForHelp -> {
                        Logger.error("Couldn't parse into data class", exception)

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.ERROR,
                                payload = SendAndReceive.SendHelpError(
                                    code = SendAndReceive.HelpErrorCodes.NO_HELP_FOUND,
                                    errorMessage = exception.message.toString()
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}