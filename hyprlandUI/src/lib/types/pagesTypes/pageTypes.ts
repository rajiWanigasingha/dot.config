import type { ActionLinks, ActionType } from "$lib"

export interface ReceivePageAction {
    actionType: ActionType
    payload: any
}

export interface SidebarUI {
    componentTitle: string,
    navigationSettings: {
        icon: string,
        name: string,
        actionLink: ActionLinks
    }[]
}

export interface ReceviePageInitialValue {
    actionType: ActionType
    payload: SidebarUI[]
}

export interface RecivePageError {
    code: string,
    messaage: string
}