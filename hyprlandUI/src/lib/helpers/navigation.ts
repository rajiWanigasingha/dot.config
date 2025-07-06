import { goto } from "$app/navigation";
import { page } from "$app/state";
import { ActionLinks, helpState, sidebarConn, sidebarState } from "$lib";

export default function navigation(actionLink: ActionLinks) {

    switch (actionLink) {
        case ActionLinks.VARIABLES: {
            goto("/hyprland/custom/variables")
            sidebarConn.loadMainPage(actionLink)
            break
        }

        default: {
            if (page.url.pathname !== "/hyprland/standedInputs") {
                goto("/hyprland/standedInputs")
                sidebarConn.loadMainPage(actionLink);
                helpState.setShow(false);
            } else {
                sidebarConn.loadMainPage(actionLink);
                helpState.setShow(false);
            }
        }
    }
}