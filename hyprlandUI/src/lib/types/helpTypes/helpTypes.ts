import type { ActionLinks, ActionType, MainPageActionInputData } from "$lib"

export interface ReceiveHelp {
    actionType: ActionType
    payload: any
}

export interface SendHelpRequestAction {
    actionLink: ActionLinks,
    name: string,
    category: string
}

export interface ReceiveHelpError {
    code: string,
    message: string
}
