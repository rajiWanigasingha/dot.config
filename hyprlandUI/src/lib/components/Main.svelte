<script lang="ts">
	import {
		HyprlandUIType,
		InputFloat,
		InputRange,
		InputSelectStr,
		InputToggle,
		InputVec,
		uiStore
	} from '$lib';
	import InputColor from './basicInputs/InputColor.svelte';
	import InputGradiant from './basicInputs/InputGradiant.svelte';
	import InputInt from './basicInputs/InputInt.svelte';
	import InputSelectInt from './basicInputs/InputSelectInt.svelte';
	import InputStr from './basicInputs/InputStr.svelte';
	// $inspect(uiStore.activeTabSettings)
</script>

<div>
	<div class="flex flex-row items-center justify-between px-4 pt-2">
		<div role="tablist" class="tabs tabs-border">
			{#if uiStore.mainPage.length > 1}
				{#each uiStore.mainPage as main}
					<!-- svelte-ignore a11y_missing_attribute -->
					<button onclick={() => uiStore.setActiveTab(main.tab)}>
						<a
							role="tab"
							class="tab capitalize {uiStore.activeMainPageTab === main.tab ? 'tab-active' : ''}"
							>{main.tab}</a
						>
					</button>
				{/each}
			{/if}
		</div>
		<div class="flex flex-row gap-3">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle">
				<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
					><path
						fill="currentColor"
						d="M15.5 14h-.79l-.28-.27A6.47 6.47 0 0 0 16 9.5A6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5S14 7.01 14 9.5S11.99 14 9.5 14"
					/></svg
				>
			</button>
		</div>
	</div>
	<div class="divider m-0"></div>
	<div class="flex max-h-[94vh] flex-col gap-6 overflow-y-auto px-8 py-8">
		{#each uiStore.activeTabSettings as main (main.data.settingsName + main.data.category)}
			{#if main.inputUI.type === HyprlandUIType.INPUT_TOGGLE}
				<InputToggle ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{:else if main.inputUI.type === HyprlandUIType.INPUT_RANGE}
				<InputRange ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{:else if main.inputUI.type === HyprlandUIType.INPUT_INT}
				<InputInt ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{:else if main.inputUI.type === HyprlandUIType.INPUT_FLOAT}
				<InputFloat ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{:else if main.inputUI.type === HyprlandUIType.INPUT_STR_SELECT}
				<InputSelectStr ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{:else if main.inputUI.type === HyprlandUIType.INPUT_INT_SELECT}
				<InputSelectInt ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{:else if main.inputUI.type === HyprlandUIType.INPUT_VEC}
				<InputVec ui={main.inputUI} data={main.data} />
			{:else if main.inputUI.type === HyprlandUIType.INPUT_COLOR}
				<InputColor ui={main.inputUI} data={main.data} />
			{:else}
				<InputStr ui={main.inputUI} data={main.data} />
				<div class="divider m-0"></div>
			{/if}
		{/each}
		<InputGradiant />
	</div>
</div>
