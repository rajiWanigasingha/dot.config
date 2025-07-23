import type { ActionType } from "$lib";

export interface MonitorData {
    name: string;
    disable: boolean;
    addreserved: number[] | null;
    resolution: string;
    position: string;
    scale: string;
    mirror: string | null;
    bitDepth: number | null;
    transform: number | null;
    cm: string | null;
    sdrsaturation: number | null;
    sdrbrightness: number | null;
    vrr: number | null;
}

export interface MonitorInfo {
    id: number;
    name: string;
    description: string;
    make: string;
    model: string;
    serial: string;
    width: number;
    height: number;
    refreshRate: number;
    x: number;
    y: number;
    activeWorkspace: {
        id: number;
        name: string;
    };
    specialWorkspace: {
        id: number;
        name: string;
    };
    reserved: [number, number, number, number];
    scale: number;
    transform: number;
    focused: boolean;
    dpmsStatus: boolean;
    vrr: boolean;
    solitary: string;
    activelyTearing: boolean;
    directScanoutTo: string;
    disabled: boolean;
    currentFormat: string;
    mirrorOf: string;
    availableModes: string[];
}

export interface MonitorActive {
    name: string,
    active: boolean,
    data: MonitorData,
    info?: MonitorInfo
}

export interface MonitorActionRecevice {
    actionType: ActionType
    payload: any
}

export interface MonitorActionError {
    status: "SERIALIZABLE" | "INVALID_ACTION_TYPE" | "EMPTY_PAYLOAD" | "INVALID_MONITOR_ACTION",
    message: string
}

export interface MonitorActionPayload {
    action: "CHANGE_GENERAL" | "ERROR" | "DISABLE" | "ADD_RESERVED" | "MIRROR",
    status: boolean,
    message: string
}