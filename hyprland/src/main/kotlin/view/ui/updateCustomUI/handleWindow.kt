package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.customSettingsControllers.WindowController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket /window ")

fun Route.handleWindow() {
    webSocket("/window") {
        Logger.info("Init websocket /window")

        val windowController = WindowController()

        var loop = true

        while (loop) {
            runCatching {

                val result = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.WindowReceiveData?>>()

                if (result.actionType == SendAndReceive.ActionType.DISCONNECT) {
                    Logger.info("Disconnect from /window")

                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.DISCONNECT,
                            payload = "Disconnect From /window"
                        )
                    )

                    loop = false

                    close(CloseReason(CloseReason.Codes.GOING_AWAY, "Close Websocket"))

                    return@runCatching
                }

                if (result.actionType != SendAndReceive.ActionType.MAIN_WINDOW || result.payload == null) {
                    throw IllegalArgumentException("${if (result.actionType != SendAndReceive.ActionType.MAIN_WINDOW) "Invalid Action Type. Action Type Must Be MAIN_WINDOW Or DISCONNECT" else ""}${if (result.payload == null) ". Payload Must Not Be Null" else ""}")
                }

                when (result.payload.action) {
                    SendAndReceive.WindowReceiveActions.EDIT -> {
                        if (result.payload.oldData == null || result.payload.data == null) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.ERROR,
                                    payload = SendAndReceive.WindowError(
                                        status = SendAndReceive.WindowErrorStatus.EMPTY_DATA_PAYLOAD,
                                        message = "data argument and old data argument is empty."
                                    )
                                )
                            )
                            return@runCatching
                        }

                        val editWindow =
                            windowController.edit(newWindow = result.payload.data, oldWindow = result.payload.oldData)

                        if (editWindow) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                    payload = SendAndReceive.WindowActionResults(
                                        action = SendAndReceive.WindowReceiveActions.EDIT,
                                        status = true,
                                        message = "Edit Window Successfully.",
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                    payload = SendAndReceive.WindowActionResults(
                                        action = SendAndReceive.WindowReceiveActions.EDIT,
                                        status = false,
                                        message = "Couldn't Edit Window Successfully.",
                                    )
                                )
                            )
                        }
                    }

                    SendAndReceive.WindowReceiveActions.DELETE -> {
                        if (result.payload.data == null) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.ERROR,
                                    payload = SendAndReceive.WindowError(
                                        status = SendAndReceive.WindowErrorStatus.EMPTY_DATA_PAYLOAD,
                                        message = "data argument is empty."
                                    )
                                )
                            )
                            return@runCatching
                        }

                        val deleteWindow = windowController.delete(result.payload.data)

                        if (deleteWindow) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                    payload = SendAndReceive.WindowActionResults(
                                        action = SendAndReceive.WindowReceiveActions.DELETE,
                                        status = true,
                                        message = "Window Deleted Successfully.",
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                    payload = SendAndReceive.WindowActionResults(
                                        action = SendAndReceive.WindowReceiveActions.DELETE,
                                        status = false,
                                        message = "Couldn't Delete Window Successfully.",
                                    )
                                )
                            )
                        }
                    }

                    SendAndReceive.WindowReceiveActions.ADD -> {

                        if (result.payload.data == null) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.ERROR,
                                    payload = SendAndReceive.WindowError(
                                        status = SendAndReceive.WindowErrorStatus.EMPTY_DATA_PAYLOAD,
                                        message = "data argument is empty."
                                    )
                                )
                            )
                            return@runCatching
                        }

                        val newWindow = windowController.addNew(result.payload.data)

                        if (newWindow) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                    payload = SendAndReceive.WindowActionResults(
                                        action = SendAndReceive.WindowReceiveActions.ADD,
                                        status = true,
                                        message = "New Window Create Successfully.",
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                    payload = SendAndReceive.WindowActionResults(
                                        action = SendAndReceive.WindowReceiveActions.ADD,
                                        status = false,
                                        message = "Couldn't Create New Window Successfully.",
                                    )
                                )
                            )
                        }
                    }

                    SendAndReceive.WindowReceiveActions.GET -> {

                        if (result.payload.get == null) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.ERROR,
                                    payload = SendAndReceive.WindowError(
                                        status = SendAndReceive.WindowErrorStatus.EMPTY_GET_PAYLOAD,
                                        message = "get argument is empty. need to be one of STATIC, DYNAMIC, PROPS"
                                    )
                                )
                            )
                            return@runCatching
                        }

                        val rules = windowController.getWindowRules(result.payload.get)

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_WINDOW,
                                payload = SendAndReceive.WindowActionResults(
                                    action = SendAndReceive.WindowReceiveActions.GET,
                                    status = true,
                                    message = "Got All Rules Requested",
                                    getWindow = result.payload.get,
                                    getData = rules
                                )
                            )
                        )
                    }
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
                                payload = SendAndReceive.WindowError(
                                    status = SendAndReceive.WindowErrorStatus.SERIALIZABLE,
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
                                payload = SendAndReceive.WindowError(
                                    status = SendAndReceive.WindowErrorStatus.INVALID_ACTION_TYPE,
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