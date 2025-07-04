import type { ActionLinks, SidebarUI } from "$lib";

function sidebarStatePage() {
    let sidebarState = $state({
        sidebarItems: [] as SidebarUI[],
        sidebarActive: '' as ActionLinks
    })

    return {

        sidebarState,

        setSidebarItmes: (items: SidebarUI[]) => sidebarState.sidebarItems.push(...items),

        setSidebarActive: (actionLink: ActionLinks) => sidebarState.sidebarActive = actionLink

    }
}

export const sidebarState = sidebarStatePage()