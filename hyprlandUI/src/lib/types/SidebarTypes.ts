import type { ActionLinks } from "$lib"

export interface SidebarUI {
    componentTitle: string,
    navigationSettings: {
        name: string,
        icon: string,
        actionLink: ActionLinks
    }[]
}