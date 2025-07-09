import { ActionType, envState, type EnvAction, type EnvStatus, type ReceiveEnvAction } from "$lib"
import { toast } from "svelte-sonner"

export class EnvConnection {

    private url = "ws://localhost:8080/env"
    wsEnv = $state(null as null | WebSocket)
    private new = $state(null as null | EnvAction)
    private delete = $state(null as null | EnvAction)
    private update = $state(null as null | EnvAction)


    constructor() {
        this.connect()
    }

    connect() {
        const ws = new WebSocket(this.url)

        this.wsEnv = ws

        ws.onmessage = (message: MessageEvent<string>) => {

            const reciveEnv = JSON.parse(message.data) as ReceiveEnvAction

            switch (reciveEnv.actionType) {

                case ActionType.MAIN_ENV: {

                    const reciveEnvPayload = reciveEnv.payload as EnvStatus

                    switch (reciveEnvPayload.status) {

                        case "SUCCESS": {

                            if (this.new) {
                                toast.success(reciveEnvPayload.message)

                                const env = [...envState.getEnv(), { keyword: this.new.keyword, value: this.new.value }]
                                envState.setEnv(env)

                                this.new = null
                                break;
                            }

                            if (this.delete) {
                                toast.success(reciveEnvPayload.message)

                                const env = envState.getEnv().filter(item => !(item.keyword === this.delete?.keyword && item.value === this.delete?.value))
                                envState.setEnv(env)

                                this.delete = null
                                break;
                            }


                            if (this.update) {
                                toast.success(reciveEnvPayload.message)

                                const env = envState.getEnv().map(item => {
                                    if (item.keyword === this.update?.keyword && item.value === this.update.oldValue) {
                                        return {
                                            keyword: this.update.keyword,
                                            value: this.update.value
                                        }
                                    }

                                    return item
                                })

                                envState.setEnv(env)

                                this.update = null
                                break;
                            }

                            break;
                        }

                        case "ADD_NEW_ERROR": { }

                        case "UPDATE_ERROR": { }

                        case "DELETE_ERROR": { }

                        case "EMPTY_COMMAND": { }
                    }

                }

                case ActionType.ERROR: {
                    const data = reciveEnv.payload as string
                }
            }

        }
    }

    createNew(env: EnvAction) {
        this.new = env

        this.wsEnv?.send(JSON.stringify({
            actionType: ActionType.MAIN_ENV,
            payload: env
        }))
    }

    updateEnv(env: EnvAction) {
        this.update = env

        console.log(env)

        this.wsEnv?.send(JSON.stringify({
            actionType: ActionType.MAIN_ENV,
            payload: env
        }))
    }

    deleteEnv(env: EnvAction) {
        this.delete = env

        this.wsEnv?.send(JSON.stringify({
            actionType: ActionType.MAIN_ENV,
            payload: env
        }))
    }

    close() {
        this.wsEnv?.send(JSON.stringify({
            actionType: ActionType.DISCONNECT,
            payload: null
        }))
        this.wsEnv = null
    }
}

export const envConn = new EnvConnection()