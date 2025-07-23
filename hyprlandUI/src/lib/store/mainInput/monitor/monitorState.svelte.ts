import type { MonitorActive, MonitorData, MonitorInfo } from "$lib";
import { toast } from "svelte-sonner";

function MonitorState() {

    let store = $state({
        monitor: [] as MonitorData[],
        monitorInfo: [] as MonitorInfo[],
        available: { monitor: null, monitorInfo: null } as { monitor: null | MonitorData, monitorInfo: null | MonitorInfo },
        availableMonitors: [] as { monitor: null | MonitorData, monitorInfo: null | MonitorInfo }[],
        disableMonitors: [] as { disable: Boolean, monitor: null | MonitorData, monitorInfo: null | MonitorInfo }[],
        availableIndex: 0
    })

    return {

        store,

        setMonitor(monitor: MonitorData[]) {
            this.store.monitor = monitor
        },

        getMonitor() {
            return this.store.monitor
        },

        setMonitorInfo(monitorInfo: MonitorInfo[], setAction: number = 0) {
            this.store.monitorInfo = monitorInfo

            monitorInfo.forEach(item => {

                const monitor = this.store.monitor

                let monitorAvailable = {
                    monitor: null as null | MonitorData,
                    monitorInfo: null as null | MonitorInfo
                }

                monitor.forEach(monitor => {
                    if (monitor.name === item.name) {
                        monitorAvailable = {
                            monitor: monitor,
                            monitorInfo: item
                        }
                    }
                })

                if (monitorAvailable.monitor === null) {
                    monitor.forEach(monitor => {
                        if (monitor.name === '') {
                            monitorAvailable = {
                                monitor: monitor,
                                monitorInfo: item
                            }
                        }
                    })


                    if (monitorAvailable.monitor === null) {
                        toast.warning("No Settings Found For This Display. Default Apply")
                        monitorAvailable = {
                            monitor: {
                                name: '',
                                disable: false,
                                addreserved: null,
                                resolution: 'preferred',
                                position: 'auto',
                                scale: 'auto',
                                mirror: null,
                                bitDepth: null,
                                transform: null,
                                cm: null,
                                sdrbrightness: null,
                                sdrsaturation: null,
                                vrr: null
                            },
                            monitorInfo: item
                        }
                    }
                }

                this.store.availableMonitors.push(monitorAvailable)
            })

            this.store.available = this.store.availableMonitors[setAction]
        },

        getMonitorInfo() {
            return this.store.monitorInfo
        },

        setActive(active: number) {
            this.store.available = this.store.availableMonitors[active]
            this.store.availableIndex = active
        },

        getActive() {
            return this.store.available
        },

        getDisable() {

            this.store.disableMonitors = []

            this.store.availableMonitors.forEach((item) => {

                this.store.monitor.forEach((monitor) => {

                    if (item.monitor?.name === monitor.name && monitor.disable) {
                        this.store.disableMonitors.push({
                            disable: true,
                            monitor: item.monitor,
                            monitorInfo: item.monitorInfo
                        })
                    } else if (item.monitor?.name === monitor.name) {
                        this.store.disableMonitors.push({
                            disable: false,
                            monitor: item.monitor,
                            monitorInfo: item.monitorInfo
                        })
                    }
                })

            })

            return this.store.disableMonitors
        },

        setDisable(disable: { disable: Boolean, monitor: null | MonitorData, monitorInfo: null | MonitorInfo }[]) {
            this.store.disableMonitors = disable
        }
    }
}

export const monitorState = MonitorState()