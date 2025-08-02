import type { ActionType } from "$lib";

export enum WorkspaceRules {
    MONITOR = "MONITOR",
    DEFAULT = "DEFAULT",
    GAPSIN = "GAPSIN",
    GAPSOUT = "GAPSOUT",
    BORDERSIZE = "BORDERSIZE",
    BORDER = "BORDER",
    SHADOW = "SHADOW",
    ROUNDING = "ROUNDING",
    DECORATE = "DECORATE",
    PERSISTENT = "PERSISTENT",
    ON_CREATE_EMPTY = "ON_CREATE_EMPTY",
    DEFAULT_NAME = "DEFAULT_NAME"
}

export interface WorkspaceData {
    monitor?: string;
    default?: boolean;
    gapsIn?: number;
    gapsOut?: number;
    borderSize?: number;
    border?: boolean;
    shadow?: boolean;
    rounding?: boolean;
    decorate?: boolean;
    persistent?: boolean;
    onCreatedEmpty?: string;
    defaultName?: string;
}

export interface WorkspaceRulesPayload {
    name: string,
    rules: WorkspaceData
}

export interface WorkspaceRecieve {
    actionType: ActionType
    payload: any
}

export interface WorkspaceRecievePayload {
    action: "EDIT" | "DELETE" | "ADD",
    status: boolean,
    message: string
}

export interface WorkspaceError {
    status: "SERIALIZABLE" | "INVALID_ACTION_TYPE" | "EMPTY_PAYLOAD" | "EMPTY_DELETE_PAYLOAD",
    message: string
}