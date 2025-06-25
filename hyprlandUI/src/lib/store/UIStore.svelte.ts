import type { ActionLinks, MainPageInputs, MainPageSettings, SidebarUI } from "$lib";

export const uiStore = $state({
	sidebar: [] as SidebarUI[],
	mainPage: [] as MainPageInputs[],
	activeSidebar: null as ActionLinks | null,
	activeTabSettings: [] as MainPageSettings[],
	activeMainPageTab: '',

	setSidebar(data: SidebarUI[]) {
		this.sidebar.push(...data);
	},

	getSidebar() {
		return this.sidebar;
	},

	setMainPage(settings: MainPageInputs[]) {
		this.mainPage.push(...settings);
		this.activeMainPageTab = this.mainPage[0].tab;

		const matched = settings.find((item) => item.tab === this.activeMainPageTab);
		if (matched) this.activeTabSettings.push(...matched.settings);
	},

	getMainPage() {
		return this.mainPage;
	},

	setActiveTab(tab: string) {
		this.activeMainPageTab = tab;

		const matched = this.mainPage.find((item) => item.tab === tab);
		this.activeTabSettings.length = 0;
		if (matched) this.activeTabSettings.push(...matched.settings);
	},

	clearMainPage() {
		this.mainPage = [];
		this.activeMainPageTab = '';
		this.activeTabSettings = [];
	}
});
