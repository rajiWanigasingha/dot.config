import { websocketConnection, type ActionLinks, type MainPageInputData, type MainPageInputs, type MainPageSettings, type SidebarUI } from "$lib";

export const uiStore = $state({
	sidebar: [] as SidebarUI[],
	mainPage: [] as MainPageInputs[],
	activeSidebar: null as ActionLinks | null,
	activeTabSettings: [] as MainPageSettings[],
	activeMainPageTab: '',
	activeHelp: '',
	openHelp: null as null | MainPageInputData,
	helpMd: '',


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
	},

	setActiveHelp(name: string) {
		this.activeHelp = name
	},

	clearHelp() {
		this.activeHelp = ''
	},

	setOpenHelp(data: MainPageInputData) {
		this.openHelp = data

		websocketConnection.sendActionToHelp({
			name: data.settingsName,
			category: data.category,
			actionLink: uiStore.activeSidebar!!
		})
	},

	clearOpen() {
		this.openHelp = null
	},

	setHelpMd(md: string) {
		this.helpMd = md
	}

});
