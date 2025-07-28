import type { ActionType } from "$lib"

export interface Animation {
    name: string,
    onOff: number,
    speed: string,
    curve: string,
    style: string
}

export interface Curve {
    name: string,
    x0: string,
    y0: string,
    x1: string,
    y1: string
}

export interface ReciveAnimationPayload {
    animation: Animation[],
    bezier: Curve[]
}

export interface AnimationRecive {
    actionType: ActionType
    payload: any
}

export interface AnimationRecivePayload {
    action: "ADD_NEW" | "ENABLE_OR_DISABLE" | "EDIT" | "DELETE" | "CURVE_ADD" | "CURVE_DELETE" | "CURVE_EDIT",
    status: boolean,
    message: string
}

export interface AnimationError {
    status: "SERIALIZABLE" | "INVALID_ACTION_TYPE" | "EMPTY_PAYLOAD",
    message: string
}