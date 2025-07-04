import type { ActionType, HyprlandTypes, HyprlandUIType, MainPageActionStatus } from "$lib"

export interface MainPageActions {
    tab: string,
    settings: MainPageActionSettings[]
}

export interface MainPageActionSettings {
    inputUI: MainPageActionInputUI,
    data: MainPageActionInputData
}

export interface MainPageActionInputUI {
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

export interface MainPageActionInputData {
    category: string,
    name: string,
    settingsName: string,
    description: string,
    typeOfHyprland: HyprlandTypes,
    value: string | number | boolean | null
}

export interface MainPageSendUpdate {
    name: string,
    value: string,
    type: HyprlandTypes,
    category: string
}

export interface MainPageUpdateStatus {
    actionType: ActionType
    payload: {
        status: MainPageActionStatus,
        message: string
    }
}

export interface MainPageError {
    code: string,
    errorMessage: string
}