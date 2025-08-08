import type { ActionType } from "$lib"

export interface WindowRulesPayload {
    rules: {
        name: string,
        value: string | null
    }[],
    params: string[]
}

export interface WindowRecieve {
    actionType: ActionType
    payload: any
}

export interface WindowError {
    status: "SERIALIZABLE" | "INVALID_ACTION_TYPE" | "EMPTY_PAYLOAD" | "EMPTY_GET_PAYLOAD",
    message: string
}

export interface WindowGetData {
    name: string,
    actionName: string,
    description: string,
    actionSupport: string,
    help: string
}

export interface WindowRecievePayload {
    action: "EDIT" | "DELETE" | "ADD" | "GET",
    status: boolean,
    message: string,
    getWindow: "STATIC" | "DYNAMIC" | "PARAMS",
    getData: WindowGetData[]
}