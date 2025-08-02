import { goto } from "$app/navigation";
import { page } from "$app/state";
import { ActionLinks, helpState, sidebarConn, sidebarState } from "$lib";

export default function navigation(actionLink: ActionLinks) {

    console.log(actionLink)

    switch (actionLink) {
        case ActionLinks.VARIABLES: {
            goto("/hyprland/custom/variables")
            sidebarConn.loadMainPage(actionLink)
            break
        }

        case ActionLinks.AUTOSTART: {
            goto("/hyprland/custom/execute")
            sidebarConn.loadMainPage(actionLink)
            break
        }

        case ActionLinks.ENV: {
            goto("/hyprland/custom/env")
            sidebarConn.loadMainPage(actionLink)
            break;
        }

        case ActionLinks.KEYBINDS: {
            goto("/hyprland/custom/keybinds")
            sidebarConn.loadMainPage(actionLink)
            break;
        }

        case ActionLinks.DISPLAY_AND_MONITOR: {
            goto("/hyprland/custom/monitor")
            sidebarConn.loadMainPage(actionLink)
            break;
        }

        case ActionLinks.ANIMATION: {
            goto("/hyprland/custom/animation")
            sidebarConn.loadMainPage(actionLink)
            break
        }

        case ActionLinks.WORKSPACE_RULES: {
            goto("/hyprland/custom/workspace")
            sidebarConn.loadMainPage(actionLink)
            break;
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