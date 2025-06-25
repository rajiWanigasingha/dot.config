import type { ActionLinks, ActionType, HyprlandTypes, HyprlandUIType, MainPageActionStatus } from "$lib"
import type { SidebarUI } from "./SidebarTypes"

export interface Receive {
    actionType: ActionType
    payload: any
}

export interface ReceviePageConnection {
    actionType: ActionType
    payload: SidebarUI[]
}


export interface SendSideBarActions {
    actionType: ActionType
    payload: {
        actionLink: ActionLinks
    }
}

export interface ReceiveMainPage {
    actionType: ActionType
    payload: MainPageInputs[]
}

export interface MainPageInputs {
    tab: string,
    settings: MainPageSettings[]
}

export interface MainPageSettings {
    inputUI: MainPageInputUI,
    data: MainPageInputData
}

export interface MainPageInputUI {
    type: HyprlandUIType,
    value: string | number | boolean | number[] | null,
    minVal: number | null,
    maxVal: number | null,
    valueOptions: string[] | number[] | null,
    optionExplain: string[] | null,
    placeholder: string,
    validation: {
        regex: string | null,
        range: number[] | null,
        optionsInt: number[] | null,
        optionStr: string[] | null,
        ints: number[] | null,
        floats: number[] | null
    },
    validationError: string,
    typeOfHyprland: HyprlandTypes
}

export interface MainPageInputData {
    category: string,
    name: string,
    settingsName: string,
    description: string,
    typeOfHyprland: HyprlandTypes,
    value: string | number | boolean | null
}

export interface ReceiveMainUpdateConnection {
    actionType: ActionType
    payload: string
}

export interface SendMainUpdatesActionLink {
    actionType: ActionType
    payload: {
        actionLink: ActionLinks
    }
}

export interface ReceiveMainUpdateActions {
    actionType: ActionType
    payload: {
        status: MainPageActionStatus,
        message: string
    }
}

export interface SendMainStandedUpdate {
    name: string,
    value: string,
    type: HyprlandTypes,
    category: string
}