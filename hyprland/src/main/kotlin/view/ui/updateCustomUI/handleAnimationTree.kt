package org.dot.config.view.ui.updateCustomUI

import io.ktor.server.routing.Route
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.SerializationException
import org.dot.config.controller.ui.customSettingsControllers.AnimationController
import org.dot.config.model.SendAndReceive
import org.slf4j.LoggerFactory

private val Logger = LoggerFactory.getLogger("Websocket /animationTree ")

fun Route.handleAnimationTree() {

    webSocket("/animationTree") {

        Logger.info("Init Websocket For AnimationTree")

        val animationController = AnimationController()

        var loop = true

        while (loop) {
            runCatching {

                val receiveData = receiveDeserialized<SendAndReceive.Receive<SendAndReceive.AnimationReceiveData?>>()

                if (receiveData.actionType == SendAndReceive.ActionType.DISCONNECT) {
                    Logger.info("Disconnect from /animationTree")

                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.DISCONNECT,
                            payload = "Disconnect From /animationTree"
                        )
                    )

                    loop = false

                    close(CloseReason(CloseReason.Codes.GOING_AWAY, "Close Websocket"))

                    return@runCatching
                }

                if (receiveData.actionType != SendAndReceive.ActionType.MAIN_ANIMATION) {
                    throw IllegalArgumentException("Invalid Action Type. Is Must Be MAIN_ANIMATION Or DISCONNECT")
                }

                if (receiveData.payload == null) {
                    sendSerialized(
                        data = SendAndReceive.Send(
                            actionType = SendAndReceive.ActionType.ERROR,
                            payload = SendAndReceive.AnimationError(
                                status = SendAndReceive.AnimationErrorStatus.EMPTY_PAYLOAD,
                                message = "Payload only can be empty on disconnect"
                            )
                        )
                    )
                    return@runCatching
                }

                when (receiveData.payload.action) {
                    SendAndReceive.AnimationReceiveActions.ADD_NEW -> {

                        val addResult = animationController.addNewAnimation(animation = receiveData.payload.animation , curve = receiveData.payload.bezier)

                        if (addResult) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.ADD_NEW,
                                    status = true,
                                    message = "Add New Animation Successfully"
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.ADD_NEW,
                                    status = false,
                                    message = "Couldn't Add New Animation"
                                )
                            ))
                            return@runCatching
                        }
                    }

                    SendAndReceive.AnimationReceiveActions.ENABLE_OR_DISABLE -> {

                        val enable = animationController.disableOrEnable(animation = receiveData.payload.animation)

                        if (enable) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.ENABLE_OR_DISABLE,
                                    status = true,
                                    message = "${receiveData.payload.animation.name} Successfully ${if (receiveData.payload.animation.onOff == 1) "Enabled" else "Disabled"}"
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.ENABLE_OR_DISABLE,
                                    status = false,
                                    message = "Couldn't ${if (receiveData.payload.animation.onOff == 1) "Enabled" else "Disabled"} This ${receiveData.payload.animation.name} Animation"
                                )
                            ))
                            return@runCatching
                        }
                    }

                    SendAndReceive.AnimationReceiveActions.EDIT -> {

                        val enable = animationController.edit(animation = receiveData.payload.animation)

                        if (enable) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.EDIT,
                                    status = true,
                                    message = "${receiveData.payload.animation.name} Successfully Edited"
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.EDIT,
                                    status = false,
                                    message = "Couldn't Edit ${receiveData.payload.animation.name}"
                                )
                            ))
                            return@runCatching
                        }
                    }

                    SendAndReceive.AnimationReceiveActions.DELETE -> {
                        val delete = animationController.delete(animation = receiveData.payload.animation)

                        if (delete) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.DELETE,
                                    status = true,
                                    message = "${receiveData.payload.animation.name} Successfully Deleted."
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.DELETE,
                                    status = false,
                                    message = "Couldn't Delete ${receiveData.payload.animation.name}"
                                )
                            ))
                            return@runCatching
                        }
                    }

                    SendAndReceive.AnimationReceiveActions.CURVE_ADD -> {

                        if (receiveData.payload.bezier == null) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_ADD,
                                    status = false,
                                    message = "Couldn't Find New Curve In Payload."
                                )
                            ))

                            return@runCatching
                        }

                        val addCurve = animationController.curveAdd(curve = receiveData.payload.bezier)

                        if (addCurve) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_ADD,
                                    status = true,
                                    message = "New Curve Added Successfully."
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_ADD,
                                    status = false,
                                    message = "Couldn't Add New Curve"
                                )
                            ))
                            return@runCatching
                        }
                    }

                    SendAndReceive.AnimationReceiveActions.CURVE_EDIT -> {
                        if (receiveData.payload.bezier == null || receiveData.payload.bezierOld == null) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_ADD,
                                    status = false,
                                    message = "Couldn't Find Curve To Update Or Old Curve."
                                )
                            ))

                            return@runCatching
                        }

                        val addCurve = animationController.curveEdit(curve = receiveData.payload.bezier , old = receiveData.payload.bezierOld)

                        if (addCurve) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_EDIT,
                                    status = true,
                                    message = "Curve Edited Successfully."
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_EDIT,
                                    status = false,
                                    message = "Couldn't Edit Curve"
                                )
                            ))
                            return@runCatching
                        }
                    }

                    SendAndReceive.AnimationReceiveActions.CURVE_DELETE -> {

                        if (receiveData.payload.bezier == null) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_DELETE,
                                    status = false,
                                    message = "Couldn't Find Curve In Payload."
                                )
                            ))

                            return@runCatching
                        }

                        val deleteCurve = animationController.curveDelete(curve = receiveData.payload.bezier)

                        if (deleteCurve) {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_DELETE,
                                    status = true,
                                    message = "Curve Deleted Successfully."
                                )
                            ))
                            return@runCatching
                        } else {
                            sendSerialized(data = SendAndReceive.Send(
                                actionType = SendAndReceive.ActionType.MAIN_ANIMATION,
                                payload = SendAndReceive.AnimationActionResults(
                                    action = SendAndReceive.AnimationReceiveActions.CURVE_DELETE,
                                    status = false,
                                    message = "Couldn't Delete Curve"
                                )
                            ))
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
                                payload = SendAndReceive.AnimationError(
                                    status = SendAndReceive.AnimationErrorStatus.SERIALIZABLE,
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
                                payload = SendAndReceive.AnimationError(
                                    status = SendAndReceive.AnimationErrorStatus.INVALID_ACTION_TYPE,
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


