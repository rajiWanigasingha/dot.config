import type { ActionType } from "$lib"

export interface KeybindsLoad {
    flags: string[],
    mod: string[],
    keys: string[],
    description: string,
    dispatcher: string,
    args: string
}

export interface Dispatchers {
    name: string,
    command: string,
    description: string
}

export interface ReceiveKeybindAction {
    actionType: ActionType
    payload: any
}

export interface ReceiveKeybindHelp {
    action: "KEYBIND_HELP" | "DISPATCHER_HELP" | "GET_DISPATCHERS" | "CREATE_NEW" | "DELETE",
    helpPage: string | null,
    dispatcher: Dispatchers[] | null,
    actionStatus: boolean | null,
    actionMessage: string | null
}

export interface ReceiveKeybindActionError {
    status: "SERIALIZABLE" | "INVALID_ACTION_TYPE" | "EMPTY_HELP_PAGE" | "EMPTY_PAYLOAD" | "EMPTY_DISPATCHERS" | "EMPTY_DISPATCHER_COMMAND" | "EMPTY_DATA" | "NEW_BIND_CREATING_FAILED" | "EMPTY_DELETE",
    message: string
}