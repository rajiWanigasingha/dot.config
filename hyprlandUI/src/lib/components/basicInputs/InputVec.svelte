<script lang="ts">
	import {
		uiStore,
		updateChange,
		websocketConnection,
		type ActionLinks,
		type MainPageInputData,
		type MainPageInputUI,
		type SendMainStandedUpdate
	} from '$lib';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let value = $state(ui.value as number[]);

	let focusOn = $state(null as number | null);

	let errorFromServer = $derived(() => {
		if (
			updateChange.error?.name === data.settingsName &&
			updateChange.error.category === data.category &&
			updateChange.error.error !== ''
		) {
			return updateChange.error.error;
		} else {
			return '';
		}
	});

	function changeUpdate(actionLink: ActionLinks, num1: number, num2: number) {
		const message: SendMainStandedUpdate = {
			name: data.settingsName,
			value: `${num1}, ${num2}`,
			type: data.typeOfHyprland,
			category: data.category
		};

		websocketConnection.sendActionToMainUpdate(actionLink, message);
	}
</script>

<div class="flex w-full flex-col gap-3">
	<div class="flex flex-row items-center justify-between">
		<div class="flex flex-col gap-[2px]">
			<h4 class="text-sm font-bold capitalize">{data.name}</h4>
			<p class="text-base-content/60 max-w-[700px] text-xs font-medium text-wrap capitalize">
				{data.description.replaceAll(';', ',')}
			</p>
		</div>
		<div>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button
				class="btn btn-circle btn-sm btn-success btn-soft"
				onclick={() => changeUpdate(uiStore.activeSidebar!!, value[0], value[1])}
			>
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
					><path fill="currentColor" d="M11 13H5v-2h6V5h2v6h6v2h-6v6h-2z" /></svg
				>
			</button>
		</div>
	</div>
	<div class="join w-full">
		<input
			type="text"
			class="input join-item w-1/2 border-0 border-b-1 bg-transparent text-center text-sm shadow-none focus:outline-0 {focusOn ===
			0
				? 'border-b-base-content'
				: ''}"
			onfocusin={() => (focusOn = 0)}
			bind:value={value[0]}
		/>
		<input
			type="text"
			class="input join-item w-1/2 border-0 border-b-1 bg-transparent text-center text-sm shadow-none focus:outline-0 {focusOn ===
			1
				? 'border-b-base-content'
				: ''}"
			onfocusin={() => (focusOn = 1)}
			bind:value={value[1]}
		/>
	</div>
	<div class="join w-full">
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button
			class="btn btn-outline join-item hover:bg-secondary hover:text-secondary-content hover:border-base-content w-1/2 hover:border-1"
			onclick={() => {
				if (focusOn !== null) value[focusOn]--;
			}}
		>
			<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
				><path fill="currentColor" d="M19 12.998H5v-2h14z" /></svg
			>
		</button>
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button
			class="btn btn-outline join-item hover:bg-secondary hover:text-secondary-content hover:border-base-content w-1/2 hover:border-1"
			onclick={() => {
				if (focusOn !== null) value[focusOn] = value[focusOn] + 1;
			}}
		>
			<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
				><path fill="currentColor" d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6z" /></svg
			>
		</button>
	</div>
</div>

{#if errorFromServer() !== ''}
	<div role="alert" class="alert alert-vertical alert-error sm:alert-horizontal">
		<svg
			xmlns="http://www.w3.org/2000/svg"
			fill="none"
			viewBox="0 0 24 24"
			class="stroke-error-content h-5 w-5 shrink-0"
		>
			<path
				stroke-linecap="round"
				stroke-linejoin="round"
				stroke-width="2"
				d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
			></path>
		</svg>
		<div>
			<h3 class="text-error-content font-bold">Error</h3>
			<div class="text-error-content text-xs font-semibold">{errorFromServer()}</div>
		</div>
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button class="btn btn-sm btn-ghost btn-circle" onclick={() => updateChange.clearError()}>
			<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
				><path
					fill="currentColor"
					d="M6.4 19L5 17.6l5.6-5.6L5 6.4L6.4 5l5.6 5.6L17.6 5L19 6.4L13.4 12l5.6 5.6l-1.4 1.4l-5.6-5.6z"
				/></svg
			>
		</button>
	</div>
{/if}
