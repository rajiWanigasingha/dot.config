import { goto } from "$app/navigation"
import { ActionType, keybindState, type KeybindsLoad, type ReceiveKeybindAction, type ReceiveKeybindActionError, type ReceiveKeybindHelp } from "$lib"
import { toast } from "svelte-sonner"

class KeybindsConnection {

    private url = "ws://localhost:8080/keybinds"
    wsKeybind = $state(null as null | WebSocket)
    private newbind = $state(null as null | KeybindsLoad)
    private deleteKey = $state(null as null | { mod: string[], key: string[], flags: string[] })

    constructor() {
        this.connection()
    }

    connection() {
        const ws = new WebSocket(this.url)

        this.wsKeybind = ws

        ws.onmessage = (message: MessageEvent<string>) => {

            const reciveKeybind = JSON.parse(message.data) as ReceiveKeybindAction

            switch (reciveKeybind.actionType) {

                case ActionType.MAIN_KEYBINDS: {

                    const keybind = reciveKeybind.payload as ReceiveKeybindHelp

                    switch (keybind.action) {

                        case "KEYBIND_HELP": {
                            keybindState.setHelp("KEYBIND_HELP", keybind.helpPage ?? "")
                            break;
                        }

                        case "DISPATCHER_HELP": {
                            keybindState.setHelp("DISPATCHER_HELP", keybind.helpPage ?? "")
                            break;
                        }

                        case "GET_DISPATCHERS": {
                            keybindState.setDispatchers(keybind.dispatcher!!)
                            keybindState.setHelp("DISPATCHER_HELP", keybind.helpPage ?? "")
                            break
                        }

                        case "CREATE_NEW": {
                            const success = keybind.actionStatus

                            if (success && this.newbind !== null) {
                                toast.success(keybind.actionMessage!!)

                                keybindState.setKeybinds([...keybindState.getKeybinds(), this.newbind])

                                keybindState.setSummery(false)

                                keybindState.setLoadDispatcher('')

                                keybindState.setDispatchers([])

                                goto("/hyprland/custom/keybinds")
                            } else {
                                toast.error("Couldn't create new keybind")
                            }
                        }

                        case "DELETE": {
                            const success = keybind.actionStatus

                            if (success) {
                                toast.success(keybind.actionMessage!!)

                                const filterOutDelete = keybindState.getKeybinds().filter(item => !(item.mod === this.deleteKey?.mod && item.keys === this.deleteKey.key && item.flags === this.deleteKey.flags))

                                keybindState.setKeybinds(filterOutDelete)
                            }
                        }
                    }

                    break;
                }

                case ActionType.ERROR: {

                    const message = reciveKeybind.payload as ReceiveKeybindActionError

                    toast.error(`Error code ${message.status}`, { description: message.message })

                    break;
                }
            }

        }
    }

    sendHelp(action: "KEYBIND_HELP" | "DISPATCHER_HELP", args: string | null = null) {

        console.log("Send Help")

        if (this.wsKeybind === null) {
            return
        }

        this.wsKeybind?.send(JSON.stringify({
            actionType: ActionType.MAIN_KEYBINDS,
            payload: {
                action: action,
                dispatcherTitle: args,
            }
        }))
    }

    getDispatchers() {
        console.log("Get Dispatchers for keybinds")

        if (this.wsKeybind === null) {
            return
        }

        this.wsKeybind?.send(JSON.stringify({
            actionType: ActionType.MAIN_KEYBINDS,
            payload: {
                action: "GET_DISPATCHERS",
                dispatcherTitle: null,
            }
        }))
    }

    sendDisconnect() {

        console.log("Send Disconnect")

        if (this.wsKeybind === null) {
            return
        }

        this.wsKeybind?.send(JSON.stringify({
            actionType: ActionType.DISCONNECT,
            payload: null
        }))

        this.wsKeybind = null
    }

    createNew(keybind: KeybindsLoad) {
        console.log("Create New Keybind")

        if (this.wsKeybind === null) {
            return
        }

        this.newbind = keybind

        this.wsKeybind?.send(JSON.stringify({
            actionType: ActionType.MAIN_KEYBINDS,
            payload: {
                action: "CREATE_NEW",
                data: keybind
            }
        }))
    }

    delete(mod: string[], key: string[], flags: string[]) {
        console.log("Delete Keybind")

        this.deleteKey = { mod: mod, key: key, flags: flags }

        this.wsKeybind?.send(JSON.stringify({
            actionType: ActionType.MAIN_KEYBINDS,
            payload: {
                action: "DELETE",
                delete: {
                    mod: mod,
                    key: key,
                    flags: flags
                }
            }
        }))
    }
}

export const keybindConn = new KeybindsConnection()