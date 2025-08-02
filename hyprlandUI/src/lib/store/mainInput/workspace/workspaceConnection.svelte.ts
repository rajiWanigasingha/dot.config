import { goto } from "$app/navigation"
import { ActionType, workspaceState, type WorkspaceData, type WorkspaceError, type WorkspaceRecieve, type WorkspaceRecievePayload, type WorkspaceRulesPayload } from "$lib"
import { toast } from "svelte-sonner"

class WorkspaceConnection {
    private url = "ws://localhost:8080/workspace"
    wsWorkspace = $state(null as null | WebSocket)
    private editData = $state(null as null | WorkspaceRulesPayload)
    private deleteData = $state(null as null | { rules: WorkspaceRulesPayload, delete: "ALL" | "SINGLE" })
    private addNew = $state(null as null | WorkspaceRulesPayload)

    connect() {
        const ws = new WebSocket(this.url)
        this.wsWorkspace = ws

        console.log("Connect To Workspace")

        ws.onmessage = (message: MessageEvent<string>) => {


            const reciveAnimation = JSON.parse(message.data) as WorkspaceRecieve

            switch (reciveAnimation.actionType) {

                case ActionType.MAIN_WORKSPACE: {

                    const result = reciveAnimation.payload as WorkspaceRecievePayload

                    switch (result.action) {
                        case "EDIT": {
                            if (this.editData === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (result.status) {

                                const newWorkspace = workspaceState.getWorkspaceRules().map((item) => {
                                    if (item.name === this.editData?.name) {
                                        return this.editData
                                    } else {
                                        return item
                                    }
                                })

                                workspaceState.setWorkspaceRules(newWorkspace)

                                toast.success(result.message)
                            } else {
                                toast.error(result.message)
                            }

                            break;
                        }

                        case "DELETE": {
                            if (this.deleteData === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (result.status) {

                                if (this.deleteData?.delete === 'ALL') {
                                    const deleteWorkspace = workspaceState.getWorkspaceRules().filter((item) => item.name !== this.deleteData?.rules.name)

                                    workspaceState.setWorkspaceRules(deleteWorkspace)

                                } else {
                                    const deleteWorkspace = workspaceState.getWorkspaceRules().map((item) => {
                                        if (item.name === this.deleteData?.rules.name) {
                                            return {
                                                name: item.name, rules: {
                                                    ...Object.fromEntries(
                                                        Object.entries(this.deleteData.rules.rules).filter(([_, value]) => value !== undefined)
                                                    )
                                                }
                                            }
                                        } else {
                                            return item
                                        }
                                    })

                                    workspaceState.setWorkspaceRules(deleteWorkspace)
                                }

                                workspaceState.setUiDelete(null)
                                toast.success(result.message)
                            } else {
                                toast.error(result.message)
                            }

                            break
                        }

                        case "ADD": {

                            if (this.addNew === null) {
                                toast.error("Something Went Wrong")
                            }

                            if (result.status) {

                                const newWorkspaceRules = Object.fromEntries(Object.entries(this.addNew!!.rules).filter(([_, value]) => value !== undefined)) as WorkspaceData;

                                const newWorkspace = [...workspaceState.getWorkspaceRules(), { name: this.addNew!!.name, rules: newWorkspaceRules }]

                                workspaceState.setWorkspaceRules(newWorkspace)

                                toast.success(result.message)

                                workspaceState.ui.pageChange = false

                                goto("/hyprland/custom/workspace")
                            } else {
                                toast.error(result.message)
                            }


                            break;
                        }
                    }

                    break
                }

                case ActionType.ERROR: {

                    const error = reciveAnimation.payload as WorkspaceError

                    toast.error(error.status, { description: error.message })

                    break
                }
            }
        }
    }

    desconnect() {
        if (this.wsWorkspace === null) {
            return
        }

        this.wsWorkspace.send(JSON.stringify({
            actionType: ActionType.DISCONNECT,
            payload: null
        }))
    }

    edit(workspace: WorkspaceRulesPayload) {
        if (this.wsWorkspace === null) {
            return
        }

        this.editData = workspace

        this.wsWorkspace.send(JSON.stringify({
            actionType: ActionType.MAIN_WORKSPACE,
            payload: {
                action: "EDIT",
                data: workspace
            }
        }))
    }

    delete(workspace: WorkspaceRulesPayload, deleteRule: "ALL" | "SINGLE") {
        if (this.wsWorkspace === null) {
            return
        }

        this.deleteData = { rules: workspace, delete: deleteRule }

        this.wsWorkspace.send(JSON.stringify({
            actionType: ActionType.MAIN_WORKSPACE,
            payload: {
                action: "DELETE",
                data: workspace,
                delete: deleteRule
            }
        }))
    }

    add(workspace: WorkspaceRulesPayload) {
        if (this.wsWorkspace === null) {
            return
        }

        workspaceState.ui.action = false

        this.addNew = workspace

        this.wsWorkspace.send(JSON.stringify({
            actionType: ActionType.MAIN_WORKSPACE,
            payload: {
                action: "ADD",
                data: workspace
            }
        }))
    }
}

export const workspaceConn = new WorkspaceConnection()