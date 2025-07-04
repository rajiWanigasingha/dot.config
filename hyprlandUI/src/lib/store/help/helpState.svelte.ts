import { helpConn, sidebarState, type MainPageActionInputData } from "$lib"

function helpMessage() {

    let helpstate = $state({
        show: false,
        activeHelp: null as null | string,
        helpMd: null as null | string,
        helpSelect: null as null | MainPageActionInputData
    })

    return {
        helpstate,

        setShow(data: boolean) {
            this.helpstate.show = data
        },

        setActiveHelp(help: string | null, data: MainPageActionInputData) {
            this.helpstate.activeHelp = help
            this.helpstate.helpSelect = data
            this.helpstate.show = true

            if (help) {
                helpConn.loadHelp({
                    name: help,
                    category: data.category,
                    actionLink: sidebarState.sidebarState.sidebarActive
                })
            }
        },

        setHelpMd(help: string) {
            this.helpstate.helpMd = help
        },

        getActiveHelp() {
            return this.helpstate.helpSelect
        }
    }
}

export const helpState = helpMessage()