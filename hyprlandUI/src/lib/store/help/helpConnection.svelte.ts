import { ActionType, helpState, type ReceiveHelp, type ReceiveHelpError, type SendHelpRequest, type SendHelpRequestAction } from "$lib"
import { toast } from "svelte-sonner"

class helpConnection {

    private url = "ws://localhost:8080/help"

    websocketHelp = $state(null as null | WebSocket)
    webSocketConnected = $state(false)

    constructor() {
        const newConn = new WebSocket(this.url)
        this.websocketHelp = newConn

        this.websocketHelp.onmessage = (message: MessageEvent<string>) => {

            const reciveFrom = JSON.parse(message.data) as ReceiveHelp

            switch (reciveFrom.actionType) {

                case ActionType.CONNECT: {
                    this.webSocketConnected = true
                    break
                }

                case ActionType.HELP: {
                    const helpMd = reciveFrom.payload as string

                    helpState.setHelpMd(helpMd)

                    toast.success("Request Help Has Been Loaded")

                    break
                }

                case ActionType.ERROR: {
                    const error = reciveFrom.payload as ReceiveHelpError

                    toast.error(`Error code: ${error.code}`, { description: error.message })
                }
            }
        }
    }

    loadHelp(help: SendHelpRequestAction) {

        if (!this.webSocketConnected) {
            toast.warning("Connection Error, Couldn't Connnect To Help")
            return
        }

        this.websocketHelp?.send(JSON.stringify({
            actionType: ActionType.HELP,
            payload: help
        }))
    }

}

export const helpConn = new helpConnection()