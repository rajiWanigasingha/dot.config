package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import org.dot.config.controller.ui.customSettingsControllers.ExecuteControllers
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Websocket")

fun Route.handleExecutes() {

    val executeController = ExecuteControllers()

    webSocket("/execute") {

        logger.info("Connected Into /execute websocket")

        val receiveExecuteAction = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.Executes>>()

        logger.info("Action Is $receiveExecuteAction")

        if (receiveExecuteAction.actionType != SendAndReceive.ActionType.MAIN_AUTOSTART) {
            sendSerialized(data = SendAndReceive.Send(
                actionType = SendAndReceive.ActionType.ERROR,
                payload = "Invalid Action Type. Action Type Must Be MAIN_AUTOSTART"
            ))
        }

        when (receiveExecuteAction.payload.actions) {
            SendAndReceive.ExecuteActions.ADD -> {

                val executeResult = executeController.createNewExecute(keyword = receiveExecuteAction.payload.keyword , command = receiveExecuteAction.payload.command)

                if (executeResult) {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.SUCCESS,
                            message = "New Autostart Command Has Been Added"
                        )
                    ))
                } else {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.ADD_NEW_ERROR,
                            message = "Couldn't add this auto start command"
                        )
                    ))
                }

            }

            SendAndReceive.ExecuteActions.UPDATE -> {

                if (receiveExecuteAction.payload.oldCommand == null) {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.EMPTY_COMMAND,
                            message = "Command That Need To Update Is Empty"
                        )
                    ))
                }

                val executeResult = executeController.updateExecute(keyword = receiveExecuteAction.payload.keyword , command = receiveExecuteAction.payload.command , oldCommand = receiveExecuteAction.payload.oldCommand!!)

                if (executeResult) {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.SUCCESS,
                            message = "New Autostart Command Has Been Updated"
                        )
                    ))
                } else {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.UPDATE_ERROR,
                            message = "Couldn't update this auto start command"
                        )
                    ))
                }
            }

            SendAndReceive.ExecuteActions.DELETE -> {

                val executeResult = executeController.deleteExecute(keyword = receiveExecuteAction.payload.keyword , command = receiveExecuteAction.payload.command)

                if (executeResult) {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.SUCCESS,
                            message = "Autostart command has been deleted"
                        )
                    ))
                } else {
                    sendSerialized(data = SendAndReceive.Send(
                        actionType = SendAndReceive.ActionType.MAIN_AUTOSTART,
                        payload = SendAndReceive.ExecuteStatus(
                            status = SendAndReceive.ExecuteStateCommand.DELETE_ERROR,
                            message = "Couldn't delete this auto start command"
                        )
                    ))
                }

            }
        }
    }

}