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
import org.dot.config.view.errors.ErrorsBasicInputComponent
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

                val receiveAction =
                    receiveDeserialized<SendAndReceive.Receive<SendAndReceive.ReceiveMainActionForStandedInputs>>()

                Logger.info("Receive Websocket At /main -> $receiveAction")

                if (receiveAction.actionType != SendAndReceive.ActionType.MAIN) throw ErrorWebsocket.InvalidActionTypeForMain()

                Logger.info("Receive action object of $receiveAction")

                val update = mainPageController.updateChanges(data = receiveAction.payload)

                if (update) {

                    Logger.info("Update is successful, and send success message for $receiveAction")

                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN,
                            payload = SendAndReceive.SendStandedCategoryValueUpdate(
                                status = SendAndReceive.SendUpdateStatus.SUCCESS,
                                message = "Update is successful updating ${receiveAction.payload.name}"
                            )
                        )
                    )
                } else {

                    Logger.error("Update is failed, and send error message for $receiveAction")

                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN,
                            payload = SendAndReceive.SendStandedCategoryValueUpdate(
                                status = SendAndReceive.SendUpdateStatus.ERROR,
                                message = "Couldn't Update ${receiveAction.payload.name}, Something Went Wrong"
                            )
                        )
                    )
                }

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
                                payload = SendAndReceive.SendMainBarError(
                                    code = SendAndReceive.MainErrorCodes.SERIALIZABLE,
                                    errorMessage = exception.message.toString()
                                )
                            )
                        )
                    }

                    is ErrorWebsocket.InvalidActionTypeForMain -> {
                        Logger.warn("Invalid Main Websocket Action")

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.ERROR,
                                payload = SendAndReceive.SendMainBarError(
                                    code = SendAndReceive.MainErrorCodes.INVALID_ACTION_TYPE,
                                    errorMessage = "Invalid action on main socket"
                                )
                            )
                        )
                    }

                    is ErrorsBasicInputComponent.UpdateMainPageStandedCategoryCouldNotFound -> {

                        Logger.error("Update is failed ,${exception.message}")

                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN,
                                payload = SendAndReceive.SendStandedCategoryValueUpdate(
                                    status = SendAndReceive.SendUpdateStatus.ERROR,
                                    message = exception.message ?: ""
                                )
                            )
                        )
                    }

                    else -> {
                        Logger.error(exception.message, exception)
                        connect = false
                    }
                }

            }
        }
    }
}