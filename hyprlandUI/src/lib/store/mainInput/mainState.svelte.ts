import type { MainPageActions, MainPageActionSettings } from "$lib";

function mainInputState() {
    let mainInputState = $state({
        activeStandedInputs: false as boolean,
        mainPageData: [] as MainPageActions[],
        mainPageTabs: [] as string[],
        mainPageActive: { tab: null as null | string, settings: [] as MainPageActionSettings[] }
    })

    return {
        mainInputState,

        get() {
            return this.mainInputState.mainPageActive
        },

        setMainPageData(data: MainPageActions[]) {
            this.mainInputState.mainPageData = data.sort((item1, item2) => item1.tab.localeCompare(item2.tab))

            this.mainInputState.activeStandedInputs = true

            const tabs = [] as string[]

            data.forEach(item => {
                tabs.push(item.tab)
            })

            this.mainInputState.mainPageTabs = tabs.sort()

            this.get().tab = this.mainInputState.mainPageTabs[0]

            this.get().settings = data.filter(item => item.tab === this.get().tab)[0].settings
        },

        setTabs(tab: string) {
            this.get().tab = tab

            this.get().settings = this.mainInputState.mainPageData.filter(item => item.tab === this.get().tab)[0].settings
        }
    }
}

export const mainPageState = mainInputState()