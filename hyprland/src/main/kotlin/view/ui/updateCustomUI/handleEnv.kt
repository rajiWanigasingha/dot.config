package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import org.dot.config.controller.ui.customSettingsControllers.EnvController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Websocket")

fun Route.handleEnv() {

    webSocket("/env") {

        logger.info("Initialize Env WebSocket")

        val envController = EnvController()

        var loop = true

        while (loop) {

            val receiveEnv = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.Env?>>()

            logger.info("Receive Object $receiveEnv")

            if (receiveEnv.actionType == SendAndReceive.ActionType.DISCONNECT) {

                close(CloseReason(CloseReason.Codes.GOING_AWAY ,"Close After Actions"))

                logger.warn("Closing Env Websocket Connection")

                loop = false
                break;
            }


            if (receiveEnv.actionType != SendAndReceive.ActionType.MAIN_ENV) {
                sendSerialized(
                    data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.ERROR,
                        payload = "Invalid Action Type. Action Type Must Be MAIN_ENV"
                    )
                )

                loop = false
            }

            if (receiveEnv.payload == null) {
                sendSerialized(
                    data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.ERROR,
                        payload = "Invalid Payload. This Only Should Be Null When It Disconnecting"
                    )
                )

                loop = false
            }

            when (receiveEnv.payload?.actions) {
                SendAndReceive.EnvActions.ADD -> {

                    val addNew =
                        envController.createNewEnv(
                            keyword = receiveEnv.payload.keyword,
                            value = receiveEnv.payload.value
                        )

                    if (addNew) {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.SUCCESS,
                                    message = "New Env Variable Has Been Created"
                                )
                            )
                        )
                    } else {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.ADD_NEW_ERROR,
                                    message = "Couldn't Create Env Variable"
                                )
                            )
                        )
                    }
                }

                SendAndReceive.EnvActions.UPDATE -> {

                    if (receiveEnv.payload.oldValue == null) {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.EMPTY_COMMAND,
                                    message = "Couldn't Update Env Variable Because Old Env Value Is Empty"
                                )
                            )
                        )
                    }

                    val update =
                        envController.updateExistingEnv(
                            keyword = receiveEnv.payload.keyword,
                            value = receiveEnv.payload.value,
                            oldValue = receiveEnv.payload.oldValue!!
                        )

                    if (update) {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.SUCCESS,
                                    message = "Env Variable Has Been Updated"
                                )
                            )
                        )
                    } else {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.UPDATE_ERROR,
                                    message = "Couldn't Update Env Variable"
                                )
                            )
                        )
                    }
                }

                SendAndReceive.EnvActions.DELETE -> {

                    val delete =
                        envController.deleteEnv(keyword = receiveEnv.payload.keyword, value = receiveEnv.payload.value)

                    if (delete) {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.SUCCESS,
                                    message = "Env Variable Has Been Deleted"
                                )
                            )
                        )
                    } else {
                        sendSerialized(
                            data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ENV,
                                payload = SendAndReceive.EnvStatus(
                                    status = SendAndReceive.EnvStateCommand.DELETE_ERROR,
                                    message = "Couldn't Delete Env Variable"
                                )
                            )
                        )
                    }
                }

                else -> {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = "Invalid Payload. This Only Should Be Null When It Disconnecting"
                        )
                    )

                    loop = false
                }
            }
        }
    }
}