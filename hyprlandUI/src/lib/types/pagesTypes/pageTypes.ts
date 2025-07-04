import type { ActionType, SidebarUI } from "$lib"

export interface ReceivePageAction {
    actionType: ActionType
    payload: any
}

export interface ReceviePageInitialValue {
    actionType: ActionType
    payload: SidebarUI[]
}

export interface RecivePageError {
    code: string,
    messaage: string
}