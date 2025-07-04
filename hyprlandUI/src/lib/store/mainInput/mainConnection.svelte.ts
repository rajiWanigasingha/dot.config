import { ActionLinks, ActionType, MainPageActionStatus, sidebarState, updateChange, type MainPageError, type MainPageSendUpdate, type MainPageUpdateStatus, type ReceivePageAction, type ReceviePageInitialValue } from "$lib"
import { toast } from "svelte-sonner"

class MainConnection {

    private url = "ws://localhost:8080/main"
    private connected = $state(false)

    websocketMainPage = $state(null as WebSocket | null)

    constructor() {
        const newConn = new WebSocket(this.url)
        this.websocketMainPage = newConn

        this.websocketMainPage.onmessage = (message: MessageEvent<string>) => {

            const reciveFrom = JSON.parse(message.data) as ReceivePageAction

            switch (reciveFrom.actionType) {
                case ActionType.CONNECT: {
                    this.connected = true

                    break;
                }

                case ActionType.MAIN: {

                    const data = reciveFrom as MainPageUpdateStatus

                    switch (data.payload.status) {

                        case MainPageActionStatus.SUCCESS: {
                            updateChange.setUpdate(true)

                            setTimeout(() => {
                                updateChange.clearUpdates()
                            }, 2000)

                            break;
                        }

                        case MainPageActionStatus.ERROR: {
                            updateChange.setUpdate(false)

                            setTimeout(() => {
                                updateChange.clearUpdates()
                            }, 2000)

                            updateChange.changeError(data.payload.message)

                            break;
                        }

                    }

                    break;
                }

                case ActionType.ERROR: {
                    const data = reciveFrom.payload as MainPageError

                    toast.error(`Error Code: ${data.code}`, { description: data.errorMessage })

                    break
                }
            }
        }
    }

    update(actionLink: ActionLinks, items: MainPageSendUpdate) {
        if (!this.connected) {
            toast.warning("Connection Failed. Please Reload Application")
            return
        }

        updateChange.setError({ name: items.name, category: items.category, error: '' })

        this.websocketMainPage?.send(JSON.stringify({
            actionType: ActionType.MAIN,
            payload: {
                actionLink: actionLink,
                name: items.name,
                value: items.value,
                category: items.category,
                type: items.type
            }
        }))
    }

}

export const mainConn = new MainConnection()