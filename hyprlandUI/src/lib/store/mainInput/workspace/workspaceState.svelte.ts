import type { WorkspaceData, WorkspaceRulesPayload } from "$lib"
import { toast } from "svelte-sonner"

function WorkspaceState() {

    let store = $state({
        workspaces: [] as WorkspaceRulesPayload[]
    })

    let ui = $state({
        delete: null as null | number,
        addnewRules: false,
        addTo: null as null | "edit" | "new",
        editName: null as WorkspaceRulesPayload | null,
        newUIData: null as WorkspaceData | null,
        pageChange: false,
        action: false
    })

    return {
        store,
        ui,

        setWorkspaceRules(rules: WorkspaceRulesPayload[]) {
            this.store.workspaces = rules
        },

        getWorkspaceRules() {
            return this.store.workspaces
        },

        setNewEdit(rule: WorkspaceRulesPayload) {
            this.store.workspaces = this.store.workspaces.map(item => {
                if (item.name === rule.name) {
                    return {
                        name: item.name,
                        rules: rule.rules
                    }
                } else {
                    return item
                }
            })

            this.setUiAddNew(false, null)

            toast.warning("This Will Not Effect Settings Until You Change It Value.")
        },

        setUiDelete(index: number | null) {
            this.ui.delete = index
        },

        getUiDelete() {
            return this.ui.delete
        },

        setUiAddNew(bool: boolean, addTo: "edit" | "new" | null) {
            this.ui.addnewRules = bool
            this.ui.addTo = addTo
        },

        getUiAddNew() {
            return this.ui.addnewRules
        },

        deleteNewUI(data: WorkspaceData) {
            this.ui.newUIData = data
        }
    }
}

export const workspaceState = WorkspaceState()