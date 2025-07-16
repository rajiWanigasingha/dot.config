import type { Dispatchers, KeybindsLoad } from "$lib";
import { toast } from "svelte-sonner";

function KeybindsStates() {

    let store = $state({
        keybinds: [] as KeybindsLoad[],
        dispatchers: [] as Dispatchers[],
        loadDispatcher: '',
        showSummery: false
    })

    let help = $state({
        state: null as null | "KEYBIND_HELP" | "DISPATCHER_HELP",
        page: ""
    })


    let holdKeybinds = $state({
        flags: [] as string[],
        mod: [] as string[],
        key: [] as string[],
        dispatcher: null as string | null,
        args: null as string | null
    })

    return {

        store,

        help,

        holdKeybinds,

        setKeybinds(binds: KeybindsLoad[]) {
            this.store.keybinds = binds
        },

        getKeybinds() {
            return this.store.keybinds
        },

        setHelp(state: "KEYBIND_HELP" | "DISPATCHER_HELP", page: string = '') {
            help.state = state
            help.page = page
        },

        setDispatchers(dispatcher: Dispatchers[]) {
            this.store.dispatchers = dispatcher
        },

        getDispatcher() {
            return this.store.dispatchers
        },

        setLoadDispatcher(name: string) {
            this.store.loadDispatcher = name
        },

        getLoadDispatcher() {
            return this.store.loadDispatcher
        },

        setSummery(summery: boolean) {
            this.store.showSummery = summery
        },

        getSummery() {
            return this.store.showSummery
        },

        setHoldKeybinds(flags: string[], mod: string[], key: string[]) {

            const exist = this.store.keybinds.find(item =>
                arraysEqual(item.mod, mod) && arraysEqual(item.keys, key) && arraysEqual(item.flags, flags)
            )

            if (exist !== undefined) {
                toast.warning("This Keybind Already exist")
                return false
            }

            this.holdKeybinds = {
                flags: flags,
                mod: mod,
                key: key,
                dispatcher: null,
                args: null
            }

            return true
        },

        getHoldKeybinds() {
            return this.holdKeybinds
        }
    }
}

function arraysEqual(a: string[], b: string[]) {
    if (a.length !== b.length) return false;
    return a.every((val, i) => val === b[i]);
}


export const keybindState = KeybindsStates()