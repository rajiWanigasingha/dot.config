import type { EnvReceiveUI } from "$lib";

function EnvActionState() {

    let env = $state({
        envUI: [] as EnvReceiveUI[]
    })

    return {

        env,

        setEnv(env: EnvReceiveUI[]) {
            this.env.envUI = env
        },

        getEnv() {
            return this.env.envUI
        }
    }
}

export const envState = EnvActionState()