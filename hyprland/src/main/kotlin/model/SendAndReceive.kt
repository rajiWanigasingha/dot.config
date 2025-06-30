package org.dot.config.model

import kotlinx.serialization.Serializable
import org.dot.config.view.builderComponents.Sidebar
import org.hyprconfig.helpers.HyprlandTypes

object SendAndReceive {

    @Serializable
    enum class ActionType {
        SIDE_BAR, MAIN ,HELP ,CONNECT ,DISCONNECT ,ERROR
    }

    @Serializable
    sealed interface SendAndReceived<T> {
        val actionType: ActionType
        val payload: T
    }

    @Serializable
    data class Send<T>(
        override val actionType: ActionType,
        override val payload: T
    ) : SendAndReceived<T>

    @Serializable
    data class Receive<T>(
        override val actionType: ActionType,
        override val payload: T
    ) : SendAndReceived<T>


    // Sidebar Action Receive And Send Messages

    @Serializable
    data class ReceiveSidebarAction(
        val actionLink: Sidebar.ActionLinks,
    )

    @Serializable
    enum class PagesErrorCodes {
        INVALID_ACTION_TYPE ,SERIALIZABLE
    }

    @Serializable
    data class SendSideBarError(
        val code: PagesErrorCodes,
        val errorMessage: String
    )

    // Main Action Receive And Send Message

    @Serializable
    data class ReceiveMainActionForStandedInputs(
        val actionLink: Sidebar.ActionLinks,
        val name: String,
        val value: String,
        val type: HyprlandTypes,
        val category: String
    )

    @Serializable
    enum class SendUpdateStatus {
        SUCCESS, ERROR
    }

    @Serializable
    data class SendStandedCategoryValueUpdate(
        val status: SendUpdateStatus,
        val message: String
    )

    @Serializable
    enum class MainErrorCodes {
        SERIALIZABLE ,INVALID_ACTION_TYPE
    }

    @Serializable
    data class SendMainBarError(
        val code: MainErrorCodes,
        val errorMessage: String
    )


    // Help Action Receive And Send Message
    @Serializable
    data class ReceiveHelp(
        val actionLink: Sidebar.ActionLinks,
        val name: String,
        val category: String
    )

    @Serializable
    enum class HelpErrorCodes {
        SERIALIZABLE ,NO_HELP_FOUND
    }

    @Serializable
    data class SendHelpError(
        val code: HelpErrorCodes,
        val errorMessage: String
    )
}