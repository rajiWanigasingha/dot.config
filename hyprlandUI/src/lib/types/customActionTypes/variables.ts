import type { ActionType } from "$lib"

export interface ReceiveVariableAction {
    actionType: ActionType
    payload: any
}

export interface VariablesReciveUI {
    name: string,
    value: string
}