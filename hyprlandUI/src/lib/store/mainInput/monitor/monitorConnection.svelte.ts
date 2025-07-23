import { ActionType, monitorState, type MonitorActionError, type MonitorActionPayload, type MonitorActionRecevice, type MonitorData, type MonitorInfo } from "$lib"
import { toast } from "svelte-sonner"

class MonitorConnection {

    private url = "ws://localhost:8080/monitor"
    wsMonitor = $state(null as null | WebSocket)
    private changeMonitor = $state(null as null | MonitorData)
    private addReservedMonitor = $state(null as null | { monitor: MonitorData, reset: boolean })

    connect() {

        const ws = new WebSocket(this.url)
        this.wsMonitor = ws

        console.log("Connect To Monitor")

        ws.onmessage = (message: MessageEvent<string>) => {


            const reciveMonitor = JSON.parse(message.data) as MonitorActionRecevice

            switch (reciveMonitor.actionType) {

                case ActionType.MAIN_MONITOR: {

                    const monitorPayload = reciveMonitor.payload as MonitorActionPayload

                    switch (monitorPayload.action) {

                        case "CHANGE_GENERAL": {

                            if (this.changeMonitor === null) {
                                toast.error("Couldn't update. Something went wrong")
                                return
                            }

                            if (monitorPayload.status) {
                                toast.success(monitorPayload.message)

                                let update = false
                                let index = 0

                                const monitor = monitorState.getMonitor().map((item, indexof) => {
                                    if (item.name === this.changeMonitor?.name) {
                                        update = true
                                        index = indexof
                                        return this.changeMonitor
                                    } else {
                                        return item
                                    }
                                })

                                if (!update) {
                                    index = monitor.length
                                    monitor.push(this.changeMonitor)
                                }

                                monitorState.setMonitor(monitor)

                                monitorState.setMonitorInfo(monitorState.getMonitorInfo(), index)
                            } else {
                                toast.error(monitorPayload.message)
                            }

                            break;
                        }

                        case "DISABLE": {

                            if (this.changeMonitor === null) {
                                toast.error("Couldn't disable monitor. Something went wrong")
                                return
                            }

                            if (monitorPayload.status) {
                                toast.success(monitorPayload.message)

                                const disable = [] as { disable: Boolean, monitor: null | MonitorData, monitorInfo: null | MonitorInfo }[]

                                monitorState.getDisable().forEach(item => {

                                    if (item.monitor?.name === this.changeMonitor?.name) {
                                        disable.push({
                                            ...item,
                                            disable: !item.disable
                                        })
                                    } else {
                                        disable.push(item)
                                    }

                                })

                            } else {
                                toast.error(monitorPayload.message)
                            }

                            break;
                        }

                        case "ADD_RESERVED": {

                            if (this.addReservedMonitor === null) {
                                toast.error("Couldn't disable monitor. Something went wrong")
                                return
                            }

                            if (monitorPayload.status) {

                                if (this.addReservedMonitor.reset) {

                                    if (monitorState.store.available.monitor !== null && monitorState.store.available.monitor.addreserved !== null) {
                                        monitorState.store.available.monitor.addreserved = this.addReservedMonitor.monitor.addreserved
                                    } else if (monitorState.store.available.monitor !== null && monitorState.store.available.monitor.addreserved === null) {
                                        monitorState.store.available.monitor.addreserved = this.addReservedMonitor.monitor.addreserved
                                    }

                                    toast.success(monitorPayload.message)

                                } else {

                                    if (monitorState.store.available.monitor !== null && monitorState.store.available.monitor.addreserved !== null) {
                                        monitorState.store.available.monitor.addreserved = null
                                    }

                                    toast.success(monitorPayload.message)
                                }


                            } else {
                                console.log("hello")
                                toast.error(monitorPayload.message)
                            }

                            break;
                        }

                        case "MIRROR": {

                            if (this.changeMonitor === null) {
                                toast.error("Couldn't mirror monitor. Something went wrong")
                                return
                            }


                            if (monitorPayload.status) {
                                monitorState.store.availableMonitors.forEach((item) => {
                                    if (item.monitor !== null && item.monitor?.name === this.changeMonitor?.name) {
                                        item.monitor.mirror = this.changeMonitor?.mirror
                                    }
                                })

                                monitorState.setActive(monitorState.store.availableIndex)

                                toast.success(monitorPayload.message)

                            } else {
                                toast.error(monitorPayload.message)
                            }

                            break;
                        }

                        case "ERROR": {

                            const monitorPayload = reciveMonitor.payload as MonitorActionPayload

                            toast.error(monitorPayload.message)

                            break;
                        }
                    }

                    break;
                }

                case ActionType.DISCONNECT: {
                    console.log("Dissconect form /monitor websocket")
                    break;
                }

                case ActionType.ERROR: {

                    const message = reciveMonitor.payload as MonitorActionError

                    toast.error(`Error code ${message.status}`, { description: message.message })

                    break;
                }
            }
        }
    }


    changeGeneral(monitor: MonitorData) {
        if (this.wsMonitor === null) {
            return
        }

        this.changeMonitor = monitor

        this.wsMonitor?.send(JSON.stringify({
            actionType: ActionType.MAIN_MONITOR,
            payload: {
                actions: "CHANGE_GENERAL",
                monitor: monitor
            }
        }))
    }

    disableMonitor(monitor: MonitorData, disable: boolean) {
        if (this.wsMonitor === null) {
            return
        }

        this.changeMonitor = monitor

        this.wsMonitor?.send(JSON.stringify({
            actionType: ActionType.MAIN_MONITOR,
            payload: {
                actions: "DISABLE",
                monitor: monitor,
                disable: disable
            }
        }))
    }

    addReserved(monitor: MonitorData, reset: boolean) {
        if (this.wsMonitor === null) {
            return
        }

        this.addReservedMonitor = { monitor: monitor, reset: reset }

        this.wsMonitor?.send(JSON.stringify({
            actionType: ActionType.MAIN_MONITOR,
            payload: {
                actions: "ADD_RESERVED",
                monitor: monitor,
                reset: reset
            }
        }))
    }

    changeMirror(monitor: MonitorData) {
        if (this.wsMonitor === null) {
            return
        }

        this.changeMonitor = monitor

        this.wsMonitor?.send(JSON.stringify({
            actionType: ActionType.MAIN_MONITOR,
            payload: {
                actions: "MIRROR",
                monitor: monitor
            }
        }))
    }

    disconnect() {
        if (this.wsMonitor === null) {
            return
        }

        this.wsMonitor?.send(JSON.stringify({
            actionType: ActionType.DISCONNECT,
            payload: null
        }))

        this.wsMonitor = null
    }

}


export const monitorConn = new MonitorConnection()