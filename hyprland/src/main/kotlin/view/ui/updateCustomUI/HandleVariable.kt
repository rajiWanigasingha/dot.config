package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import org.dot.config.controller.ui.customSettingsControllers.VariableController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket")

fun Route.handleVariables() {

    webSocket("/variable") {
        Logger.info("Connect To Variable Websocket")

        val variableController = VariableController()

        val sendVariable = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.VariableAction>>()

        Logger.info("Action Is $sendVariable")

        when (sendVariable.payload) {
            SendAndReceive.VariableAction.ADD -> {

                val receiveData = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.AddVariable>>()

                val addNew = variableController.createNewVariable(
                    name = receiveData.payload.name,
                    value = receiveData.payload.value
                )

                if (addNew) {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_VARIABLES,
                            payload = "New Variable Is Created"
                        )
                    )
                } else {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = "Couldn't Create New Variable"
                        )
                    )
                }
            }

            SendAndReceive.VariableAction.UPDATE -> {
                val receiveData = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.UpdateVariable>>()

                Logger.info("Receive Update Data $receiveData")

                val existUpdate = variableController.updateExisting(
                    name = receiveData.payload.name,
                    updateName = receiveData.payload.updateName,
                    updateValue = receiveData.payload.updateValue
                )

                if (existUpdate) {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.MAIN_VARIABLES,
                            payload = "Variable Is Updated"
                        )
                    )
                } else {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = "Couldn't Update Variable"
                        )
                    )
                }
            }
        }

        close(CloseReason(code = CloseReason.Codes.NORMAL, message = "Close After The Action"))
    }

}