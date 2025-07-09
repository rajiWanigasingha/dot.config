import { ActionType, autostartState, type AutoStartSend, type AutoStartStatus, type ReceiveAutoStartAction } from "$lib"
import { toast } from "svelte-sonner"

class AutoStartConnection {
    private url = "ws://localhost:8080/execute"

    wsAutoStart = $state(null as WebSocket | null)

    private newAutoStart = $state(null) as { keyword: string, command: string } | null
    private updateAutoStart = $state(null) as { keyword: string, command: string, oldCommand: string } | null
    private deleteAutoStart = $state(null) as { keyword: string, command: string } | null

    constructor() {

        const ws = new WebSocket(this.url)

        this.wsAutoStart = ws

        ws.onmessage = (message: MessageEvent<string>) => {

            const reciveFrom = JSON.parse(message.data) as ReceiveAutoStartAction

            switch (reciveFrom.actionType) {

                case ActionType.MAIN_AUTOSTART: {

                    const result = reciveFrom.payload as AutoStartStatus

                    switch (result.status) {
                        case "SUCCESS": {

                            if (this.newAutoStart !== null) {
                                toast.success(result.message)

                                autostartState.setAutoStart([...autostartState.getAutoStart(), this.newAutoStart])
                            }

                            if (this.updateAutoStart !== null) {
                                toast.success(result.message)

                                autostartState.setAutoStart(autostartState.getAutoStart().map(item => {
                                    if (item.command == this.updateAutoStart?.oldCommand) {
                                        return {
                                            keyword: this.updateAutoStart.keyword,
                                            command: this.updateAutoStart.command
                                        }
                                    }
                                    return item
                                }
                                ))
                            }

                            if (this.deleteAutoStart !== null) {
                                toast.success(result.message)

                                autostartState.setAutoStart(autostartState.getAutoStart().filter(item => !(item.command === this.deleteAutoStart?.command)))
                            }

                            break;
                        }

                        case "ADD_NEW_ERROR": {
                            toast.error(result.status, { description: result.message })

                            break;
                        }

                        case "UPDATE_ERROR": {
                            toast.error(result.status, { description: result.message })

                            break
                        }

                        case "DELETE_ERROR": {
                            toast.error(result.status, { description: result.message })

                            break;
                        }

                        case "EMPTY_COMMAND": {
                            toast.error(result.status, { description: result.message })

                            break;
                        }
                    }

                }

                case ActionType.ERROR: { }

            }

        }

    }

    addExecute(keyword: string, command: string) {

        this.newAutoStart = { keyword: keyword, command: command }

        this.wsAutoStart?.send(JSON.stringify({
            actionType: ActionType.MAIN_AUTOSTART,
            payload: {
                actions: "ADD",
                keyword: keyword,
                command: command,
                oldCommand: null
            } as AutoStartSend
        }))
    }

    updateExecute(keyword: string, command: string, oldCommand: string) {
        this.updateAutoStart = { keyword: keyword, command: command, oldCommand: oldCommand }

        this.wsAutoStart?.send(JSON.stringify({
            actionType: ActionType.MAIN_AUTOSTART,
            payload: {
                actions: "UPDATE",
                keyword: keyword,
                command: command,
                oldCommand: oldCommand
            } as AutoStartSend
        }))
    }

    deleteExecute(keyword: string, command: string) {
        this.deleteAutoStart = { keyword: keyword, command: command }

        this.wsAutoStart?.send(JSON.stringify({
            actionType: ActionType.MAIN_AUTOSTART,
            payload: {
                actions: "DELETE",
                keyword: keyword,
                command: command,
                oldCommand: null
            } as AutoStartSend
        }))
    }
}

export const autoStartConn = new AutoStartConnection()