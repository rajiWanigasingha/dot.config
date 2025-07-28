import { ActionType, type AnimationError, type AnimationRecive, type AnimationRecivePayload, type Animation, type Curve, animationState } from "$lib"
import { toast } from "svelte-sonner"

class AnimationConnection {

    private url = "ws://localhost:8080/animationTree"
    wsAnimation = $state(null as null | WebSocket)
    private addNewAnimation = $state(null as null | { animation: Animation, curve: Curve | null })
    private enableOrDisableAnimation = $state(null as null | Animation)
    private editAnimation = $state(null as null | Animation)
    private deleteAnimation = $state(null as null | Animation)
    private curve = $state(null as { curve: Curve, old: null | Curve } | null)
    private placeholder = $state({ name: '', speed: '', onOff: 0, curve: '', style: '' } as Animation)

    connect() {
        const ws = new WebSocket(this.url)
        this.wsAnimation = ws

        console.log("Connect To Animation Tree")

        ws.onmessage = (message: MessageEvent<string>) => {


            const reciveAnimation = JSON.parse(message.data) as AnimationRecive

            switch (reciveAnimation.actionType) {
                case ActionType.MAIN_ANIMATION: {

                    const payload = reciveAnimation.payload as AnimationRecivePayload

                    switch (payload.action) {
                        case "ADD_NEW": {

                            if (this.addNewAnimation === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                animationState.setAnimation([...animationState.getAnimation(), this.addNewAnimation!!.animation])

                                if (this.addNewAnimation?.curve !== null) {
                                    animationState.setCurves([...animationState.getCurves(), this.addNewAnimation!!.curve])
                                }

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }

                        case "ENABLE_OR_DISABLE": {

                            if (this.enableOrDisableAnimation === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                const newAnimations = animationState.getAnimation().map((item) => {
                                    if (item.name === this.enableOrDisableAnimation?.name) {
                                        return {
                                            ...item,
                                            onOff: this.enableOrDisableAnimation.onOff
                                        }
                                    } else {
                                        return item
                                    }
                                })

                                animationState.setAnimation(newAnimations)

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }

                        case "EDIT": {

                            if (this.editAnimation === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                const newAnimations = animationState.getAnimation().map((item) => {
                                    if (item.name === this.editAnimation?.name) {
                                        return this.editAnimation!!
                                    } else {
                                        return item
                                    }
                                })

                                animationState.setAnimation(newAnimations)

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }

                        case "DELETE": {

                            if (this.deleteAnimation === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                const newAnimations = animationState.getAnimation().filter((item) => item.name !== this.deleteAnimation?.name)

                                animationState.setAnimation(newAnimations)

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }

                        case "CURVE_ADD": {

                            if (this.curve === null || this.curve?.curve === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                animationState.setCurves([...animationState.getCurves(), this.curve!!.curve!!])

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }

                        case "CURVE_EDIT": {

                            if (this.curve === null || this.curve?.curve === null || this.curve?.old === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                const filterd = animationState.getCurves().map(item => {
                                    if (item.name === this.curve?.old?.name) {
                                        return this.curve.curve
                                    } else {
                                        return item
                                    }
                                })

                                animationState.setCurves(filterd)

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }

                        case "CURVE_DELETE": {

                            if (this.curve === null || this.curve?.curve === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (payload.status) {

                                const filterAnimation = animationState.getAnimation().filter((item) => item.curve !== this.curve?.curve.name)
                                const filterBezier = animationState.getCurves().filter((item) => item.name !== this.curve?.curve.name)

                                animationState.setAnimation(filterAnimation)

                                animationState.setCurves(filterBezier)

                                toast.success(payload.message)

                            } else {
                                toast.error(payload.message)
                            }

                            break
                        }
                    }

                    break
                }

                case ActionType.ERROR: {

                    const error = reciveAnimation.payload as AnimationError

                    toast.error(error.status, { description: error.message })

                    break
                }
            }
        }
    }

    disconnect() {
        if (this.wsAnimation === null) {
            return
        }

        this.wsAnimation.send((JSON.stringify({
            actionType: ActionType.DISCONNECT,
            payload: null
        }))
        )

        this.wsAnimation = null
    }


    addNew(animation: Animation, curve: Curve | null) {
        if (this.wsAnimation === null) {
            return
        }

        this.addNewAnimation = { animation: animation, curve: curve }

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "ADD_NEW",
                animation: animation,
                bezier: curve
            }
        }))
    }

    enableOrDisable(animation: Animation) {
        if (this.wsAnimation === null) {
            return
        }

        this.enableOrDisableAnimation = animation

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "ENABLE_OR_DISABLE",
                animation: animation,
                bezier: null
            }
        }))
    }

    edit(animation: Animation) {
        if (this.wsAnimation === null) {
            return
        }

        this.editAnimation = animation

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "EDIT",
                animation: animation,
                bezier: null
            }
        }))
    }

    delete(animation: Animation) {
        if (this.wsAnimation === null) {
            return
        }

        this.deleteAnimation = animation

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "DELETE",
                animation: animation,
                bezier: null
            }
        }))
    }

    addNewCurve(curve: Curve) {
        if (this.wsAnimation === null) {
            return
        }

        this.curve = { curve: curve, old: null }

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "CURVE_ADD",
                animation: this.placeholder,
                bezier: curve
            }
        }))
    }

    editCurve(curve: Curve, old: Curve) {
        if (this.wsAnimation === null) {
            return
        }

        this.curve = { curve: curve, old: old }

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "CURVE_EDIT",
                animation: this.placeholder,
                bezier: curve,
                bezierOld: old
            }
        }))
    }

    deleteCurve(curve: Curve) {
        if (this.wsAnimation === null) {
            return
        }

        this.curve = { curve: curve, old: null }

        this.wsAnimation.send(JSON.stringify({
            actionType: ActionType.MAIN_ANIMATION,
            payload: {
                action: "CURVE_DELETE",
                animation: this.placeholder,
                bezier: curve
            }
        }))
    }

}

export const animationConn = new AnimationConnection()