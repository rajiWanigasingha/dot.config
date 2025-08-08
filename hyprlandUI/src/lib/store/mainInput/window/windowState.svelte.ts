import type { WindowGetData, WindowRulesPayload } from "$lib";

function WindowState() {

    let store = $state({
        window: [] as WindowRulesPayload[],
        rules: [] as WindowGetData[],
        rulesType: '' as "STATIC" | "DYNAMIC" | "PARAMS"
    })

    let ui = $state({
        addActionArgHelp: '',
        open: false,
        edit: null as null | WindowRulesPayload,
        deleteWindow: null as null | {old: WindowRulesPayload ,new: WindowRulesPayload}
    })

    return {
        store,
        ui,

        setWindow(window: WindowRulesPayload[]) {
            this.store.window = window
        },

        getWindow() {
            return this.store.window
        },

        setWindowGetData(window: WindowGetData[]) {
            this.store.rules = window
        },

        getWindowGetData() {
            return this.store.rules
        },

        findWindowRule(name: string) {
            const args = this.store.rules.filter((item) => item.name === name)

            this.setHelp(name)

            if (args[0].actionSupport === 'arg') {
                return true
            } else {
                return false
            }
        },

        setHelp(name: string) {
            const value = this.store.rules.filter((item) => item.name === name)[0].help

            if (value !== "null") {
                this.ui.addActionArgHelp = value
            } else {
                this.ui.addActionArgHelp = this.store.rules.filter((item) => item.actionName === name)[0].description
            }
        },

        setEdit(rules: WindowRulesPayload | null) {
            this.ui.edit = rules
        },

        getEdit() {
            return this.ui.edit
        }
    }
}

export const windowState = WindowState()