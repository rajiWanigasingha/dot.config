package org.dot.config.model

import kotlinx.serialization.Serializable
import org.dot.config.view.builderComponents.Sidebar
import org.hyprconfig.helpers.HyprlandTypes

object SendAndReceive {

    @Serializable
    enum class ActionType {
        SIDE_BAR, MAIN ,HELP ,CONNECT ,DISCONNECT ,ERROR ,MAIN_VARIABLES ,MAIN_AUTOSTART ,MAIN_ENV ,MAIN_KEYBINDS
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

    // Variable Receives And Sends Message
    @Serializable
    enum class VariableAction {
        ADD ,UPDATE
    }

    @Serializable
    data class AddVariable(
        val name: String,
        val value: String
    )

    @Serializable
    data class UpdateVariable(
        val name: String,
        val updateName: String,
        val updateValue: String
    )

    // Execute Receive and send
    @Serializable
    enum class ExecuteActions {
        ADD ,UPDATE ,DELETE
    }

    @Serializable
    data class Executes(
        val actions: ExecuteActions,
        val keyword: String,
        val command: String,
        val oldCommand: String?
    )

    @Serializable
    enum class ExecuteStateCommand {
        EMPTY_COMMAND ,SUCCESS ,ADD_NEW_ERROR ,UPDATE_ERROR ,DELETE_ERROR
    }

    @Serializable
    data class ExecuteStatus(
        val status: ExecuteStateCommand,
        val message: String
    )

    // Env Receive and send
    @Serializable
    enum class EnvActions {
        ADD ,UPDATE ,DELETE
    }

    @Serializable
    data class Env(
        val actions: EnvActions,
        val keyword: String,
        val value: String,
        val oldValue: String?
    )

    @Serializable
    enum class EnvStateCommand {
        EMPTY_COMMAND ,SUCCESS ,ADD_NEW_ERROR ,UPDATE_ERROR ,DELETE_ERROR
    }

    @Serializable
    data class EnvStatus(
        val status: EnvStateCommand,
        val message: String
    )

    // Keybinds Receive And Send
    @Serializable
    enum class KeybindActionStatus {
        KEYBIND_HELP ,DISPATCHER_HELP ,GET_DISPATCHERS ,CREATE_NEW ,DELETE
    }

    @Serializable
    data class KeybindDelete(
        val mod: List<String>,
        val key: List<String>,
        val flags: List<Char>
    )

    @Serializable
    data class KeybindAction(
        val action: KeybindActionStatus,
        val dispatcherTitle: String? = null,
        val data: Tables.KeybindTable? = null,
        val delete: KeybindDelete? = null
    )

    @Serializable
    enum class KeybindErrStatus {
        SERIALIZABLE ,INVALID_ACTION_TYPE ,EMPTY_HELP_PAGE ,EMPTY_PAYLOAD ,EMPTY_DISPATCHERS ,EMPTY_DISPATCHER_COMMAND ,EMPTY_DATA ,NEW_BIND_CREATING_FAILED ,EMPTY_DELETE ,DELETE_FAILS
    }

    @Serializable
    data class KeybindError(
        val status: KeybindErrStatus,
        val message: String
    )

    @Serializable
    data class KeybindActionResult(
        val action: KeybindActionStatus,
        val helpPage: String? = null,
        val dispatcher: List<Tables.KeybindDispatcher>? = null,
        val actionStatus: Boolean? = null,
        val actionMessage: String? = null
    )
}