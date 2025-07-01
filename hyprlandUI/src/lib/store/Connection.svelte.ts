import { ActionLinks, ActionType, uiStore, type ReceviePageConnection, type Receive, type SendSideBarActions, type ReceiveMainPage, type ReceiveMainUpdateConnection, type SendMainUpdatesActionLink, type ReceiveMainUpdateActions, MainPageActionStatus, type SendMainStandedUpdate, updateChange, type SendHelpRequest, type SendHelp } from "$lib"

class WebSocketConnection {

    private url = "ws://localhost:8080"

    pageConnection = $state(null as WebSocket | null)

    mainConnection = $state(null as WebSocket | null)

    helpConnection = $state(null as WebSocket | null)

    connectToPage() {

        const ws = new WebSocket(`${this.url}/pages`)

        this.pageConnection = ws

        ws.onmessage = (message) => {

            const receiveData = JSON.parse(message.data) as Receive

            switch (receiveData.actionType) {

                case ActionType.CONNECT: {

                    const data = receiveData as ReceviePageConnection

                    uiStore.setSidebar(data.payload)

                    websocketConnection.sendActionToPage(data.payload[0].navigationSettings[0].actionLink)

                    break;
                }

                case ActionType.MAIN: {

                    const data = receiveData as ReceiveMainPage

                    uiStore.clearMainPage()

                    uiStore.setMainPage(data.payload)

                    break;
                }

                case ActionType.ERROR: { }
            }
        }
    }


    connectToMain() {

        const ws = new WebSocket(`${this.url}/main`)

        this.mainConnection = ws

        ws.onmessage = (message) => {

            const receiveData = JSON.parse(message.data) as Receive

            switch (receiveData.actionType) {

                case ActionType.CONNECT: {

                    const data = receiveData as ReceiveMainUpdateConnection

                    console.log(data.payload)

                    break;
                }

                case ActionType.MAIN: {

                    const data = receiveData as ReceiveMainUpdateActions

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

                        case MainPageActionStatus.MESSAGE: {
                            console.log(data.payload.message)
                            break;
                        }

                    }

                    break;
                }

                case ActionType.ERROR: { }
            }
        }
    }

    connectToHelp() {
        const ws = new WebSocket(`${this.url}/help`)

        this.helpConnection = ws

        ws.onmessage = (message) => {

            const receiveData = JSON.parse(message.data) as Receive

            switch (receiveData.actionType) {

                case ActionType.CONNECT: {
                    console.log(receiveData)
                    break;
                }

                case ActionType.HELP: {

                    const data = receiveData.payload as string

                    console.log(receiveData)

                    uiStore.setHelpMd(data)

                    break;
                }
            }
        }
    }

    sendActionToPage(actionLink: ActionLinks) {

        const message: SendSideBarActions = {
            actionType: ActionType.SIDE_BAR,
            payload: {
                actionLink: actionLink
            }
        }

        uiStore.activeSidebar = actionLink

        this.pageConnection?.send(JSON.stringify(message))

    }

    sendActionToMainUpdate(actionLink: ActionLinks, items: any) {

        const updateMessage = items as SendMainStandedUpdate

        const message: SendMainUpdatesActionLink = {
            actionType: ActionType.MAIN,
            payload: {
                actionLink: actionLink,
                name: updateMessage.name,
                value: updateMessage.value,
                category: updateMessage.category,
                type: updateMessage.type
            }
        }

        updateChange.setError({ name: updateMessage.name, category: updateMessage.category, error: '' })

        this.mainConnection?.send(JSON.stringify(message))
    }

    sendActionToHelp(item: SendHelpRequest) {
        const message: SendHelp = {
            actionType: ActionType.HELP,
            payload: item
        }

        console.log(message)

        this.helpConnection?.send(JSON.stringify(message))
    }

}

export const websocketConnection = new WebSocketConnection() 