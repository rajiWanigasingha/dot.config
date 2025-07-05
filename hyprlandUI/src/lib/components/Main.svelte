<script lang="ts">
	import {
		helpState,
		HyprlandUIType,
		InputFloat,
		InputRange,
		InputSelectStr,
		InputToggle,
		InputVec,
		mainPageState
	} from '$lib';
	import InputColor from './basicInputs/InputColor.svelte';
	import InputGradiant from './basicInputs/InputGradiant.svelte';
	import InputInt from './basicInputs/InputInt.svelte';
	import InputSelectInt from './basicInputs/InputSelectInt.svelte';
	import InputStr from './basicInputs/InputStr.svelte';
</script>

<div>
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<div role="tablist" class="tabs tabs-border">
			{#if mainPageState.mainInputState.mainPageTabs.length > 1}
				{#each mainPageState.mainInputState.mainPageTabs as tabs}
					<!-- svelte-ignore a11y_missing_attribute -->
					<button
						onclick={() => {
							mainPageState.setTabs(tabs);
							helpState.setShow(false);
						}}
					>
						<a
							role="tab"
							class="tab capitalize {mainPageState.get().tab === tabs ? 'tab-active' : ''}"
							>{tabs}</a
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
	{#if mainPageState.mainInputState.activeStandedInputs}
		<div class="flex max-h-[94vh] flex-col gap-6 overflow-y-auto px-8 py-8">
			{#each mainPageState.get().settings as main (main.data.settingsName + main.data.category)}
				<div
					id={main.data.settingsName}
					class=" p-4 {main.data.settingsName === helpState.helpstate.activeHelp
						? 'custom_pules'
						: ''}"
				>
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
					{:else if main.inputUI.type === HyprlandUIType.INPUT_GRADIANT}
						<InputGradiant ui={main.inputUI} data={main.data} />
					{:else}
						<InputStr ui={main.inputUI} data={main.data} />
						<div class="divider m-0"></div>
					{/if}
				</div>
			{/each}
		</div>
	{/if}
</div>

<style>
	@keyframes pulse {
		50% {
			opacity: 0.5;
		}
	}

	.custom_pules {
		animation: pulse 0.5s cubic-bezier(0.4, 0, 0.6, 1) infinite;
	}
</style>
