import type { ActionType } from "$lib"

export interface ReceiveEnvAction {
    actionType: ActionType
    payload: any
}

export interface EnvReceiveUI {
    keyword: string,
    value: string
}

export interface EnvAction {
    actions: "ADD" | "UPDATE" | "DELETE"
    keyword: string,
    value: string,
    oldValue: string | null,
}

export interface EnvStatus {
    status: "EMPTY_COMMAND" | "SUCCESS" | "ADD_NEW_ERROR" | "UPDATE_ERROR" | "DELETE_ERROR",
    message: string
}