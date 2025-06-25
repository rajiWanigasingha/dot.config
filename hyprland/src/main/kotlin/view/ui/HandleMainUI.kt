package org.dot.config.view.ui

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.MainPageController
import org.dot.config.model.InputAndOutput
import org.dot.config.model.SendAndReceive
import org.dot.config.view.builderComponents.Sidebar
import org.dot.config.view.errors.ErrorWebsocket
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket")

fun Route.mainUI() {
    webSocket("/main") {
        Logger.info("Initialize web socket /main")

        val mainPageController = MainPageController()

        sendSerialized(
            data = SendAndReceive.Send(
                actionType = SendAndReceive.ActionType.CONNECT,
                payload = "Connection Is Successful"
            )
        )

        Logger.info("send successful connection message /main")

        var connect = true

        while (connect) {
            runCatching {
                Logger.info("Receive Action From Main Page")

                val receiveAction = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.ReceiveMainAction>>()

                Logger.info("Receive Websocket At /pages -> $receiveAction")

                if (receiveAction.actionType != SendAndReceive.ActionType.MAIN) throw ErrorWebsocket.InvalidActionTypeForMain()

                sendSerialized(
                    data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN,
                        payload = SendAndReceive.SendStandedCategoryValueUpdate(
                            status = SendAndReceive.SendUpdateStatus.MESSAGE,
                            message = "Action Will Take At ${receiveAction.payload.actionLink.name}"
                        )
                    )
                )

                when (receiveAction.payload.actionLink) {
                    Sidebar.ActionLinks.MOUSE_AND_TOUCHPAD -> {

                        Logger.info("Try to get mouse and touchpad action")

                        val receiveMouse = receiveDeserialized<SendAndReceive.Receive<InputAndOutput.UpdateSettingsStandedCategories>>()

                        Logger.info("Action Mouse And Touchpad ${receiveMouse.payload}")

                        if (receiveMouse.actionType != SendAndReceive.ActionType.MAIN) throw ErrorWebsocket.InvalidActionTypeForMain()

                        val mouseAndTouchpadUpdate = mainPageController.mouseAndTouchpad(name = receiveMouse.payload.name , value = receiveMouse.payload.value , type = receiveMouse.payload.type , category = receiveMouse.payload.category)

                        if (mouseAndTouchpadUpdate) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN,
                                payload = SendAndReceive.SendStandedCategoryValueUpdate(
                                    status = SendAndReceive.SendUpdateStatus.SUCCESS,
                                    message = "Update is successful updating ${receiveMouse.payload.name}"
                                )
                            ))
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN,
                                payload = SendAndReceive.SendStandedCategoryValueUpdate(
                                    status = SendAndReceive.SendUpdateStatus.ERROR,
                                    message = "Update is unsuccessful updating ${receiveMouse.payload.name}"
                                )
                            ))
                        }
                    }
                    Sidebar.ActionLinks.KEYBOARD -> TODO()
                    Sidebar.ActionLinks.KEYBINDS -> TODO()
                    else -> TODO()
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
                            payload = SendAndReceive.SendMainBarError(
                                code = SendAndReceive.MainErrorCodes.SERIALIZABLE,
                                errorMessage = exception.message.toString()
                            )
                        ))
                    }

                    is ErrorWebsocket.InvalidActionTypeForMain -> {
                        Logger.warn("Invalid Main Websocket Action")

                        sendSerialized(data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = SendAndReceive.SendMainBarError(
                                code = SendAndReceive.MainErrorCodes.INVALID_ACTION_TYPE,
                                errorMessage = "Invalid action on main socket"
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