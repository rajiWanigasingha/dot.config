<script lang="ts">
	import {
		ActionLinks,
		HyprlandTypes,
		mainConn,
		sidebarState,
		type MainPageActionInputData,
		type MainPageActionInputUI
	} from '$lib';

	let { ui, data }: { ui: MainPageActionInputUI; data: MainPageActionInputData } = $props();

	function updateToggle(actionLink: ActionLinks, toggle: boolean) {
		mainConn.update(actionLink, {
			name: data.settingsName,
			value: `${toggle}`,
			type: HyprlandTypes.BOOL,
			category: data.category
		});
	}
</script>

<div class="flex w-full flex-row items-center justify-between gap-3">
	<div class="flex flex-col gap-[2px]">
		<h4 class="text-sm font-bold capitalize">{data.name}</h4>
		<p class="text-base-content/60 max-w-[700px] text-xs font-medium text-wrap capitalize">
			{data.description.replaceAll(';', ',')}
		</p>
	</div>
	<label class="toggle toggle-xl text-base-conten">
		<input
			type="checkbox"
			class="hidden"
			onclick={(e) =>
				updateToggle(sidebarState.sidebarState.sidebarActive, e.currentTarget.checked)}
			checked={ui.value as boolean}
		/>
		<svg
			aria-label="enabled"
			xmlns="http://www.w3.org/2000/svg"
			viewBox="0 0 24 24"
			fill="none"
			stroke="currentColor"
			stroke-width="4"
			stroke-linecap="round"
			stroke-linejoin="round"
		>
			<path d="M18 6 6 18" />
			<path d="m6 6 12 12" />
		</svg>
		<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
			<g
				stroke-linejoin="round"
				stroke-linecap="round"
				stroke-width="4"
				fill="none"
				stroke="currentColor"
			>
				<path d="M20 6 9 17l-5-5"></path>
			</g>
		</svg>
	</label>
</div>
