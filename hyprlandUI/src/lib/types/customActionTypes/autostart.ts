import type { ActionType } from "$lib"

export interface ReceiveAutoStartAction {
    actionType: ActionType,
    payload: any
}

export interface AutoStartReceiveUI {
    keyword: string,
    command: string
}

export interface AutoStartStatus {
    status: "EMPTY_COMMAND" | "SUCCESS" | "ADD_NEW_ERROR" | "UPDATE_ERROR" | "DELETE_ERROR",
    message: string
}

export interface AutoStartSend {
    actions: "ADD" | "UPDATE" | "DELETE",
    keyword: string,
    command: string,
    oldCommand: string | null
}