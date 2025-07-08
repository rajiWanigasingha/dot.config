import type { AutoStartReceiveUI } from "$lib";

function AutoStratState() {

    let autoState = $state({
        autoStart: [] as AutoStartReceiveUI[],
        activeTab: "exec-once" as "exec" | "exec-shutdown" | "exec-once"
    })

    return {
        autoState,

        setAutoStart(state: AutoStartReceiveUI[]) {
            this.autoState.autoStart = state
        },

        setExecTab(execute: "exec" | "exec-shutdown" | "exec-once") {
            this.autoState.activeTab = execute
        },

        getAutoStart() {
            return this.autoState.autoStart.filter(item => item.keyword === this.autoState.activeTab)
        }
    }
}

export const autostartState = AutoStratState()