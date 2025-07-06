import type { VariablesReciveUI } from "$lib/types/customActionTypes/variables";

function VariableState() {

    let state = $state({
        variables: [] as VariablesReciveUI[]
    })

    return {
        state,

        setVariables(variables: VariablesReciveUI[]) {
            this.state.variables = variables
        },

        checkVariables(name: string) {
            const constains = this.state.variables.filter(item => item.name === name)

            if (constains.length === 0) {
                return true
            } else {
                return false
            }
        }
    }
}

export const variableState = VariableState()