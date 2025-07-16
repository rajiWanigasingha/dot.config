package org.dot.config.view.ui

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.SidebarController
import org.dot.config.model.SendAndReceive
import org.dot.config.view.builderComponents.Sidebar
import org.dot.config.view.errors.ErrorWebsocket
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket")

fun Route.handleUI() {
    webSocket("/pages") {
        Logger.info("Initialize web socket /pages")

        val sidebarService = SidebarController()

        sendSerialized(
            data = SendAndReceive.Send(
                actionType = SendAndReceive.ActionType.CONNECT,
                payload = sidebarService.getSidebarUI()
            )
        )

        Logger.info("Send Websocket At /Page -> Init Values")

        var connect = true

        while (connect) {
            runCatching {
                Logger.info("Receive Action From Sidebar")

                val message = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.ReceiveSidebarAction>>()

                Logger.info("Receive Websocket At /pages -> $message")

                if (message.actionType != SendAndReceive.ActionType.SIDE_BAR) throw ErrorWebsocket.InvalidActionTypeForPages()

                when (message.payload.actionLink) {

                    Sidebar.ActionLinks.VARIABLES -> {
                        val ui = sidebarService.getVariableUI()

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_VARIABLES,
                            payload = ui
                        ))
                    }

                    Sidebar.ActionLinks.AUTOSTART -> {
                        val ui = sidebarService.getAutoStart()

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                            payload = ui
                        ))
                    }

                    Sidebar.ActionLinks.ENV -> {
                        val ui = sidebarService.getEnv()

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_ENV,
                            payload = ui
                        ))
                    }

                    Sidebar.ActionLinks.KEYBINDS -> {
                        val ui = sidebarService.getKeybinds()

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_KEYBINDS,
                            payload = ui
                        ))
                    }

                     else -> {
                         val ui = sidebarService.getPageUI(actionLinks = message.payload.actionLink)

                         sendSerialized(data = SendAndReceive.Send(
                             actionType = SendAndReceive.ActionType.MAIN,
                             payload = ui
                         ))

                     }
                }

            }.onFailure { exception ->
                when (exception) {

                    is ClosedReceiveChannelException -> {
                        Logger.warn("Websocket is closed because -> ${closeReason.await()}")
                        connect = false
                    }

                    is SerializationException -> {
                        Logger.error("Couldn't parse into data class" ,exception)

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = SendAndReceive.SendSideBarError(
                                code = SendAndReceive.PagesErrorCodes.SERIALIZABLE,
                                errorMessage = exception.message.toString()
                            )
                        ))
                    }

                    is ErrorWebsocket.InvalidActionTypeForPages -> {
                        Logger.error(exception.message)

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = SendAndReceive.SendSideBarError(
                                code = SendAndReceive.PagesErrorCodes.INVALID_ACTION_TYPE,
                                errorMessage = exception.message.toString()
                            )
                        ))
                    }

                    else -> {
                        Logger.error(exception.message ,exception)
                        connect = false
                    }
                }
            }
        }
    }
}

/**
 * There is three parts of ui that need to be handles
 *
 * 1. Sidebar
 * 2. Settings config
 * 3. Help
 *
 * Needs to have a common structure of serialized way to receive data.
 *
 * {
 *  action: SIDEBAR, SETTINGS, HELP
 *  payload: T -> any data class of these three
 * }
 *
 * Every message sends and receives should be logged.
 *
 */