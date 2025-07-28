package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.customSettingsControllers.MonitorController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("WebSocket")

fun Route.handleMonitors() {
    webSocket("/monitor") {

        Logger.info("Init /monitor websocket")

        val monitorController = MonitorController()

        var loop = true

        while (loop) {
            runCatching {

                val receiveMonitor = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.MonitorAction?>>()

                if (receiveMonitor.actionType === SendAndReceive.ActionType.DISCONNECT) {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.DISCONNECT,
                            payload = "Disconnect From /monitor"
                        )
                    )
                    loop = false

                    close(CloseReason(CloseReason.Codes.GOING_AWAY, "Close Websocket"))

                    return@runCatching
                }

                if (receiveMonitor.actionType !== SendAndReceive.ActionType.MAIN_MONITOR) {
                    throw IllegalArgumentException("Invalid Action Type. Action type can be MAIN_MONITOR or DISCONNECT")
                }

                if (receiveMonitor.payload === null) {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                            payload = SendAndReceive.MonitorError(
                                status = SendAndReceive.MonitorErrorStatus.EMPTY_PAYLOAD,
                                message = "Payload is empty."
                            )
                        )
                    )
                    return@runCatching
                }

                when (receiveMonitor.payload.actions) {
                    SendAndReceive.MonitorActionType.CHANGE_GENERAL -> {

                        val monitorGeneral = monitorController.changeGeneral(general = receiveMonitor.payload.monitor)

                        if (monitorGeneral) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.CHANGE_GENERAL,
                                        status = true,
                                        message = "General Settings Changed Successfully"
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.ERROR,
                                        status = false,
                                        message = "Couldn't change general settings"
                                    )
                                )
                            )
                        }

                    }

                    SendAndReceive.MonitorActionType.DISABLE -> {
                        val monitorGeneral = monitorController.disableMonitor(
                            general = receiveMonitor.payload.monitor,
                            disable = receiveMonitor.payload.disable ?: false
                        )

                        if (monitorGeneral) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.DISABLE,
                                        status = true,
                                        message = "Disable ${receiveMonitor.payload.monitor.name} successfully"
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.ERROR,
                                        status = false,
                                        message = "Couldn't Disable ${receiveMonitor.payload.monitor.name}"
                                    )
                                )
                            )
                        }
                    }

                    SendAndReceive.MonitorActionType.ADD_RESERVED -> {

                        if (receiveMonitor.payload.reset == null) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.ERROR,
                                        status = false,
                                        message = "Reset argument is empty"
                                    )
                                )
                            )
                            return@runCatching
                        }

                        val monitorAddReserved = monitorController.addReserved(reserved = receiveMonitor.payload.monitor , reset = receiveMonitor.payload.reset)

                        if (monitorAddReserved) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.ADD_RESERVED,
                                        status = true,
                                        message = "${if(receiveMonitor.payload.reset) "Add Or Update Reserved Area" else "Remove Reserved Area"} in ${receiveMonitor.payload.monitor.name} successfully"
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.ERROR,
                                        status = false,
                                        message = "Couldn't ${if(receiveMonitor.payload.reset) "Add Or Update Reserved Area" else "Remove Reserved Area"} in ${receiveMonitor.payload.monitor.name}"
                                    )
                                )
                            )
                        }

                    }

                    SendAndReceive.MonitorActionType.MIRROR -> {
                        val monitorAddReserved = monitorController.mirror(mirror = receiveMonitor.payload.monitor)

                        if (monitorAddReserved) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.MIRROR,
                                        status = true,
                                        message = "Mirrored monitors successfully"
                                    )
                                )
                            )
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_MONITOR,
                                    payload = SendAndReceive.MonitorActionResults(
                                        action = SendAndReceive.MonitorActionType.ERROR,
                                        status = false,
                                        message = "Couldn't mirror the monitors"
                                    )
                                )
                            )
                        }
                    }

                    else -> sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = SendAndReceive.MonitorError(
                                status = SendAndReceive.MonitorErrorStatus.INVALID_MONITOR_ACTION,
                                message = "Invalid Monitor Action"
                            )
                        )
                    )
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
                                payload = SendAndReceive.MonitorError(
                                    status = SendAndReceive.MonitorErrorStatus.SERIALIZABLE,
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
                                payload = SendAndReceive.MonitorError(
                                    status = SendAndReceive.MonitorErrorStatus.INVALID_ACTION_TYPE,
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