import { ActionLinks, ActionType, autostartState, mainPageState, sidebarState, variableState, type AutoStartReceiveUI, type MainPageActions, type ReceivePageAction, type ReceviePageInitialValue, type RecivePageError, type VariablesReciveUI } from "$lib"
import { toast } from "svelte-sonner"

class SideBarConnection {

    private url = "ws://localhost:8080/pages"
    private idelSidebarAction = $state(null as null | ActionLinks)

    websocketSidebar = $state(null as WebSocket | null)

    constructor() {
        const newConn = new WebSocket(this.url)
        this.websocketSidebar = newConn

        this.websocketSidebar.onmessage = (message: MessageEvent<string>) => {

            const reciveFrom = JSON.parse(message.data) as ReceivePageAction

            switch (reciveFrom.actionType) {
                case ActionType.CONNECT: {
                    toast.success("Connect To Server Successfully")

                    const data = reciveFrom as ReceviePageInitialValue

                    sidebarState.setSidebarItmes(data.payload)

                    const defaultActionLink = data.payload[0].navigationSettings[0].actionLink

                    sidebarState.setSidebarActive(defaultActionLink)

                    this.loadMainPage(defaultActionLink)

                    break;
                }

                case ActionType.MAIN: {
                    const data = reciveFrom.payload as MainPageActions[]

                    if (this.idelSidebarAction === null) {
                        toast.error("Could Not Load These Settings. Try To Reload The Appliaction")
                        break
                    }

                    sidebarState.setSidebarActive(this.idelSidebarAction)
                    mainPageState.setMainPageData(data)

                    toast.success(`Requested Settings Has Been Loaded`)

                    break;
                }

                case ActionType.MAIN_VARIABLES: {
                    const data = reciveFrom.payload as VariablesReciveUI[]


                    if (this.idelSidebarAction === null) {
                        toast.error("Could Not Load These Settings. Try To Reload The Appliaction")
                        break
                    }

                    sidebarState.setSidebarActive(this.idelSidebarAction)
                    variableState.setVariables(data)

                    toast.success(`Variable Settings Has Been Loaded`)

                    break;
                }

                case ActionType.MAIN_AUTOSTART: {
                    const data = reciveFrom.payload as AutoStartReceiveUI[]

                    if (this.idelSidebarAction === null) {
                        toast.error("Could Not Load These Settings. Try To Reload The Appliaction")
                        break
                    }

                    sidebarState.setSidebarActive(this.idelSidebarAction)
                    autostartState.setAutoStart(data)

                    toast.success(`Autostart Settings Has Been Loaded`)

                    break;
                }

                case ActionType.ERROR: {
                    const data = reciveFrom.payload as RecivePageError

                    toast.error(`Error Code: ${data.code}`, { description: data.messaage })
                }
            }
        }
    }

    loadMainPage(actionLink: ActionLinks) {
        this.idelSidebarAction = actionLink
        this.websocketSidebar?.send(
            JSON.stringify({
                actionType: ActionType.SIDE_BAR,
                payload: {
                    actionLink: actionLink
                }
            })
        )
    }

}

export const sidebarConn = new SideBarConnection()