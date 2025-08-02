package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.customSettingsControllers.WorkspaceController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket /workspace ")

fun Route.handleWorkspace() {

    webSocket("/workspace") {

        Logger.info("Init Websocket For Workspace")

        val workspaceController = WorkspaceController()

        var loop = true

        while (loop) {
            runCatching {

                val receiveData = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.WorkspaceReceiveData?>>()

                if (receiveData.actionType == SendAndReceive.ActionType.DISCONNECT) {
                    Logger.info("Disconnect from /workspace")

                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.DISCONNECT,
                            payload = "Disconnect From /workspace"
                        )
                    )

                    loop = false

                    close(CloseReason(CloseReason.Codes.GOING_AWAY, "Close Websocket"))

                    return@runCatching
                }

                if (receiveData.actionType != SendAndReceive.ActionType.MAIN_WORKSPACE || receiveData.payload == null) {
                    throw IllegalArgumentException("${if (receiveData.actionType != SendAndReceive.ActionType.MAIN_WORKSPACE) "Invalid Action Type. Action Type Must Be MAIN_WORKSPACE Or DISCONNECT" else ""}${if (receiveData.payload == null) ". Payload Must Not Be Null" else ""}")
                }

                when (receiveData.payload.action) {
                    SendAndReceive.WorkspaceReceiveActions.EDIT -> {

                        val edit = workspaceController.edit(workspace = receiveData.payload.data)

                        if (edit) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WORKSPACE,
                                    payload = SendAndReceive.WorkspaceActionResults(
                                        action = SendAndReceive.WorkspaceReceiveActions.EDIT,
                                        status = true,
                                        message = "Workspace Edit Is Successful"
                                    )
                                )
                            )
                            return@runCatching
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WORKSPACE,
                                    payload = SendAndReceive.WorkspaceActionResults(
                                        action = SendAndReceive.WorkspaceReceiveActions.EDIT,
                                        status = false,
                                        message = "Couldn't Edit Workspace."
                                    )
                                )
                            )
                            return@runCatching
                        }
                    }

                    SendAndReceive.WorkspaceReceiveActions.DELETE -> {

                        if (receiveData.payload.delete == null) {

                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.ERROR,
                                    payload = SendAndReceive.WorkspaceError(
                                        status = SendAndReceive.WorkspaceErrorStatus.EMPTY_DELETE_PAYLOAD,
                                        message = "Empty delete payload. Must be All Or Single"
                                    )
                                )
                            )

                            return@runCatching
                        }

                        val delete = workspaceController.delete(workspace = receiveData.payload.data , delete = receiveData.payload.delete)

                        if (delete) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WORKSPACE,
                                    payload = SendAndReceive.WorkspaceActionResults(
                                        action = SendAndReceive.WorkspaceReceiveActions.DELETE,
                                        status = true,
                                        message = "Workspace Deleted Successfully"
                                    )
                                )
                            )
                            return@runCatching
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WORKSPACE,
                                    payload = SendAndReceive.WorkspaceActionResults(
                                        action = SendAndReceive.WorkspaceReceiveActions.DELETE,
                                        status = false,
                                        message = "Couldn't Delete Workspace."
                                    )
                                )
                            )
                            return@runCatching
                        }
                    }

                    SendAndReceive.WorkspaceReceiveActions.ADD -> {

                        val addNew = workspaceController.addNew(workspace = receiveData.payload.data)

                        if (addNew) {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WORKSPACE,
                                    payload = SendAndReceive.WorkspaceActionResults(
                                        action = SendAndReceive.WorkspaceReceiveActions.ADD,
                                        status = true,
                                        message = "Workspace Added Successfully"
                                    )
                                )
                            )
                            return@runCatching
                        } else {
                            sendSerialized(
                                data = SendAndReceive.Send(
                                    actionType = SendAndReceive.ActionType.MAIN_WORKSPACE,
                                    payload = SendAndReceive.WorkspaceActionResults(
                                        action = SendAndReceive.WorkspaceReceiveActions.ADD,
                                        status = false,
                                        message = "Couldn't Add New Workspace."
                                    )
                                )
                            )
                            return@runCatching
                        }
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
                                payload = SendAndReceive.WorkspaceError(
                                    status = SendAndReceive.WorkspaceErrorStatus.SERIALIZABLE,
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
                                payload = SendAndReceive.WorkspaceError(
                                    status = SendAndReceive.WorkspaceErrorStatus.INVALID_ACTION_TYPE,
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