package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.customSettingsControllers.KeybindController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("WebSocket")

fun Route.handleKeybinds() {

    webSocket("/keybinds") {

        Logger.info("Init /keybinds websocket")

        val keybindController = KeybindController()

        var loop = true

        while (loop) {
            runCatching {
                val getAction = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.KeybindAction?>>()

                when (getAction.actionType) {
                    SendAndReceive.ActionType.MAIN_KEYBINDS -> {

                        if (getAction.payload == null) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.ERROR,
                                    payload = SendAndReceive.KeybindError(
                                        status = SendAndReceive.KeybindErrStatus.EMPTY_PAYLOAD,
                                        message = "Payload Is Empty"
                                    )
                                )
                            )
                        }

                        when (getAction.payload!!.action) {
                            SendAndReceive.KeybindActionStatus.KEYBIND_HELP -> {

                                val help = keybindController.getHelpForKeys()

                                if (help != null) {
                                    Logger.info("Send Keybind Help Page")

                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.MAIN_KEYBINDS,
                                            payload = SendAndReceive.KeybindActionResult(
                                                action = SendAndReceive.KeybindActionStatus.KEYBIND_HELP,
                                                helpPage = help
                                            )
                                        )
                                    )
                                } else {
                                    Logger.error("Help Page For Keybind Is Empty. This Is A Bug Need Fixing")

                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.ERROR,
                                            payload = SendAndReceive.KeybindError(
                                                status = SendAndReceive.KeybindErrStatus.EMPTY_HELP_PAGE,
                                                message = "Help Page Is Empty"
                                            )
                                        )
                                    )

                                    loop = false
                                    return@runCatching
                                }
                            }

                            SendAndReceive.KeybindActionStatus.GET_DISPATCHERS -> {

                                val dispatchers = keybindController.getDispatchers()

                                val dispatcherHelp = keybindController.getHelpForDispatchers()

                                if (dispatchers != null) {
                                    Logger.info("Send Keybind Dispatchers")

                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.MAIN_KEYBINDS,
                                            payload = SendAndReceive.KeybindActionResult(
                                                action = SendAndReceive.KeybindActionStatus.GET_DISPATCHERS,
                                                dispatcher = dispatchers,
                                                helpPage = dispatcherHelp
                                            )
                                        )
                                    )
                                } else {
                                    Logger.error("Dispatchers Are Empty. This Is A Bug Need Fixing")

                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.ERROR,
                                            payload = SendAndReceive.KeybindError(
                                                status = SendAndReceive.KeybindErrStatus.EMPTY_DISPATCHERS,
                                                message = "Dispatchers Are Empty."
                                            )
                                        )
                                    )

                                    loop = false
                                    return@runCatching
                                }
                            }

                            SendAndReceive.KeybindActionStatus.DISPATCHER_HELP -> {

                                if (getAction.payload.dispatcherTitle === null) {
                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.ERROR,
                                            payload = SendAndReceive.KeybindError(
                                                status = SendAndReceive.KeybindErrStatus.EMPTY_DISPATCHER_COMMAND,
                                                message = "Dispatcher command is empty. Can't get help without it."
                                            )
                                        )
                                    )

                                    return@runCatching
                                }

                                val help = keybindController.getDispatcherHelp(command = getAction.payload.dispatcherTitle)

                                if (help != null) {
                                    Logger.info("Send Dispatcher Help Page")

                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.MAIN_KEYBINDS,
                                            payload = SendAndReceive.KeybindActionResult(
                                                action = SendAndReceive.KeybindActionStatus.DISPATCHER_HELP,
                                                helpPage = help
                                            )
                                        )
                                    )
                                } else {
                                    Logger.error("Help Page For This Dispatcher Is Empty. This Is A Bug Need Fixing")

                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.ERROR,
                                            payload = SendAndReceive.KeybindError(
                                                status = SendAndReceive.KeybindErrStatus.EMPTY_HELP_PAGE,
                                                message = "Help Page Is Empty For ${getAction.payload.dispatcherTitle}"
                                            )
                                        )
                                    )

                                    loop = false
                                    return@runCatching
                                }
                            }

                            SendAndReceive.KeybindActionStatus.CREATE_NEW -> {

                                if (getAction.payload.data === null) {
                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.ERROR,
                                            payload = SendAndReceive.KeybindError(
                                                status = SendAndReceive.KeybindErrStatus.EMPTY_DATA,
                                                message = "Data is empty for create keybind."
                                            )
                                        )
                                    )

                                    return@runCatching
                                }

                                val new = keybindController.createNewBinds(keybinds = getAction.payload.data)

                                if (new) {
                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.MAIN_KEYBINDS,
                                            payload = SendAndReceive.KeybindActionResult(
                                                action = SendAndReceive.KeybindActionStatus.CREATE_NEW,
                                                actionStatus = true,
                                                actionMessage = "New Keybind Created Successfully"
                                            )
                                        )
                                    )
                                } else {
                                    sendSerialized(
                                        data = SendAndReceive.Send(
                                            actionType = SendAndReceive.ActionType.ERROR,
                                            payload = SendAndReceive.KeybindError(
                                                status = SendAndReceive.KeybindErrStatus.NEW_BIND_CREATING_FAILED,
                                                message = "Could Not Create New Keybind"
                                            )
                                        )
                                    )
                                }
                            }
                        }

                    }

                    SendAndReceive.ActionType.DISCONNECT -> {
                        Logger.info("Disconnecting /keybinds websocket")
                        close(CloseReason(CloseReason.Codes.GOING_AWAY, "Closing /keybinds"))
                        loop = false
                    }

                    else -> throw IllegalArgumentException("Invalid Action Type")
                }

            }.onFailure { exception ->

                when (exception) {
                    is ClosedReceiveChannelException -> {
                        Logger.warn("Websocket is closed because -> ${closeReason.await()}")
                        loop = false
                        return@onFailure
                    }

                    is SerializationException -> {
                        Logger.error("Couldn't parse into data class", exception)

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.ERROR,
                                payload = SendAndReceive.KeybindError(
                                    status = SendAndReceive.KeybindErrStatus.SERIALIZABLE,
                                    message = exception.message.toString()
                                )
                            )
                        )

                        return@onFailure
                    }

                    is IllegalArgumentException -> {
                        Logger.error(exception.message)

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.ERROR,
                                payload = SendAndReceive.KeybindError(
                                    status = SendAndReceive.KeybindErrStatus.INVALID_ACTION_TYPE,
                                    message = exception.message!!
                                )
                            )
                        )

                        return@onFailure
                    }
                }
            }
        }
    }
}