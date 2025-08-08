import { ActionType, windowState, type WindowError, type WindowRecieve, type WindowRecievePayload, type WindowRulesPayload } from "$lib"
import { toast } from "svelte-sonner"

class WindowConnection {

    private url = "ws://localhost:8080/window"
    wsWindow = $state(null as null | WebSocket)
    private windowAdd = $state(null as null | WindowRulesPayload)
    private windowEdit = $state(null as null | { old: WindowRulesPayload, new: WindowRulesPayload })

    connect() {
        const ws = new WebSocket(this.url)
        this.wsWindow = ws

        console.log("Connect To Window")

        ws.onmessage = (message: MessageEvent<string>) => {
            const reciveWindow = JSON.parse(message.data) as WindowRecieve

            switch (reciveWindow.actionType) {
                case ActionType.MAIN_WINDOW: {

                    const result = reciveWindow.payload as WindowRecievePayload

                    switch (result.action) {
                        case "EDIT": {

                            if (result.status) {

                                if (this.windowEdit === null) {
                                    toast.error("Something went wrong")
                                    return
                                }

                                const edited = [] as WindowRulesPayload[]

                                let newWindow = true

                                windowState.getWindow().forEach((item) => {
                                    if (JSON.stringify(item) === JSON.stringify(this.windowEdit?.old)) {
                                        edited.push(this.windowEdit!!.new)
                                        newWindow = false
                                    } else {
                                        edited.push(item)
                                    }
                                })

                                if (newWindow) {
                                    edited.push(this.windowEdit!!.new)
                                }

                                windowState.setWindow(edited)

                                windowState.ui.open = false
                                windowState.ui.edit = null

                                toast.success(result.message)

                            } else {
                                toast.error(result.message)
                            }

                            break
                        }

                        case "DELETE": {

                            if (result.status) {

                                if (this.windowAdd === null) {
                                    toast.error("Something went wrong")
                                    return
                                }

                                const deleted = windowState.getWindow().filter((item) => JSON.stringify(item) !== JSON.stringify(this.windowAdd))

                                windowState.setWindow(deleted)

                                toast.success(result.message)

                            } else {
                                toast.error(result.message)
                            }

                            break
                        }

                        case "ADD": {

                            if (result.status) {

                                if (this.windowAdd === null) {
                                    toast.error("Something went wrong")
                                    return
                                }

                                windowState.setWindow([...windowState.getWindow(), this.windowAdd!!])

                                windowState.ui.open = false
                                windowState.ui.edit = null

                                toast.success(result.message)

                            } else {
                                toast.error(result.message)
                            }

                            break
                        }

                        case "GET": {

                            if (result.status) {

                                toast.success(result.message)

                                windowState.setWindowGetData(result.getData)

                                windowState.store.rulesType = result.getWindow

                            } else {
                                toast.error(result.message)
                            }

                            break
                        }
                    }

                    break;
                }

                case ActionType.ERROR: {

                    const error = reciveWindow.payload as WindowError

                    toast.error(error.status, { description: error.message })

                    break
                }
            }
        }
    }


    getWindowData(window: "STATIC" | "DYNAMIC" | "PARAMS") {
        if (this.wsWindow === null) {
            return
        }

        this.wsWindow.send(JSON.stringify({
            actionType: ActionType.MAIN_WINDOW,
            payload: {
                action: "GET",
                data: null,
                get: window,
                oldData: null
            }
        }))
    }

    desconnect() {
        if (this.wsWindow === null) {
            return
        }

        this.wsWindow.send(JSON.stringify({
            actionType: ActionType.DISCONNECT,
            payload: null
        }))

        this.wsWindow = null
    }


    addNewWindow(window: WindowRulesPayload) {
        if (this.wsWindow === null) {
            return
        }

        this.windowAdd = window

        this.wsWindow.send(JSON.stringify({
            actionType: ActionType.MAIN_WINDOW,
            payload: {
                action: "ADD",
                data: window,
                get: null,
                oldData: null
            }
        }))
    }

    editWindow(oldWindow: WindowRulesPayload, newWindow: WindowRulesPayload) {
        if (this.wsWindow === null) {
            return
        }

        this.windowEdit = { old: oldWindow, new: newWindow }

        this.wsWindow.send(JSON.stringify({
            actionType: ActionType.MAIN_WINDOW,
            payload: {
                action: "EDIT",
                data: newWindow,
                oldData: oldWindow,
                get: null
            }
        }))
    }

    deleteWindow(window: WindowRulesPayload) {
        if (this.wsWindow === null) {
            return
        }

        this.windowAdd = window

        this.wsWindow.send(JSON.stringify({
            actionType: ActionType.MAIN_WINDOW,
            payload: {
                action: "DELETE",
                data: window,
                get: null,
                oldData: null
            }
        }))
    }

}

export const windowConn = new WindowConnection()