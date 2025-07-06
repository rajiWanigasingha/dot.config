import { ActionType, variableState, type ReceiveVariableAction } from "$lib"
import { toast } from "svelte-sonner"

class VariableConnection {
    private url = "ws://localhost:8080/variable"
    private newVariable = null as { name: string, value: string } | null
    private update = null as { oldName: string, name: string, value: string } | null
    private ws = $state(null as null | WebSocket)

    constructor() {
        const variableWs = new WebSocket(this.url)

        this.ws = variableWs

        variableWs.onmessage = (message: MessageEvent<string>) => {

            const reciveFrom = JSON.parse(message.data) as ReceiveVariableAction

            switch (reciveFrom.actionType) {
                case ActionType.MAIN_VARIABLES: {

                    console.log(reciveFrom)

                    if (this.newVariable) {
                        toast.success("New Variable Created")

                        variableState.state.variables = [...variableState.state.variables, this.newVariable]
                    }

                    if (this.update) {

                        const variables = variableState.state.variables.map((item) => {
                            if (item.name == this.update?.oldName) {
                                return {
                                    name: this.update.name,
                                    value: this.update.value
                                }
                            }
                            return item
                        })

                        variableState.setVariables(variables)

                        toast.success("Variable Is Updates Successfully")
                    }

                    break;
                }

                case ActionType.ERROR: {

                    if (!this.update) {
                        toast.error("Couldn't Create New Variable")
                    } else {
                        toast.error("Couldn't Update Variables")
                    }

                    break;
                }
            }
        }
    }

    addVariable(name: string, value: string) {

        this.newVariable = {
            name: name,
            value: value
        }

        this.ws?.send(JSON.stringify({
            actionType: ActionType.MAIN_VARIABLES,
            payload: "ADD"
        }))

        this.ws?.send(JSON.stringify({
            actionType: ActionType.MAIN_VARIABLES,
            payload: {
                name: name,
                value: value
            }
        }))
    }

    addUpdate(oldName: string, name: string, value: string) {

        this.update = { oldName, name, value }

        this.ws?.send(JSON.stringify({
            actionType: ActionType.MAIN_VARIABLES,
            payload: "UPDATE"
        }))

        this.ws?.send(JSON.stringify({
            actionType: ActionType.MAIN_VARIABLES,
            payload: {
                name: oldName,
                updateName: name,
                updateValue: value
            }
        }))
    }
}

export const variableConn = new VariableConnection()