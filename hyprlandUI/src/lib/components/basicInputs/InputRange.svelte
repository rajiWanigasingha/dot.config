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

	let initValue = $state(ui.value as number);
	let Increment = $state('0');

	let errorValidation = $state('');
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

	$effect(() => {
		if (
			updateChange.error?.name === data.settingsName &&
			updateChange.error.category === data.category &&
			updateChange.error.error !== ''
		) {
			initValue = ui.value as number;
		}
	});

	function changeUpdate(actionLink: ActionLinks, range: number) {
		if (ui.validation.range === null) {
			errorValidation = 'No Validation Is Done, This Is A Bug, Please Try To Refresh Or Report It';
			return;
		}

		if (!(ui.validation.range[0] <= range && range <= ui.validation.range[1])) {
			errorValidation = ui.validationError;
			return;
		}

		const message: SendMainStandedUpdate = {
			name: data.settingsName,
			value: `${range}`,
			type: data.typeOfHyprland,
			category: data.category
		};

		websocketConnection.sendActionToMainUpdate(actionLink, message);
	}

	$inspect(ui.validation.range);
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
			<p class="w-[50px] text-center text-xl font-semibold">{initValue}</p>
		</div>
	</div>
	<div>
		<input
			type="range"
			min={ui.validation.range!![0]}
			max={ui.validation.range!![1]}
			bind:value={initValue}
			step="0.01"
			class="range w-full"
			onchange={(e) => {
				initValue = Number(Number(e.currentTarget.value).toFixed(2));
				changeUpdate(uiStore.activeSidebar!!, initValue);
			}}
		/>
	</div>
	<div class="join w-full">
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button
			class="btn btn-outline join-item hover:bg-secondary hover:text-secondary-content hover:border-base-content w-1/2 hover:border-1"
			onclick={() => {
				initValue = Number((initValue - 0.01 - Number(Increment)).toFixed(2));
				changeUpdate(uiStore.activeSidebar!!, initValue);
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
				initValue = Number((initValue + 0.01 + Number(Increment)).toFixed(2));
				changeUpdate(uiStore.activeSidebar!!, initValue);
			}}
		>
			<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
				><path fill="currentColor" d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6z" /></svg
			>
		</button>
	</div>
	<div class="collapse-plus collapse mx-0 border-0 px-0 focus:border-0">
		<input type="checkbox" />
		<div class="collapse-title mx-0 px-0 text-sm font-semibold">Increment Custome Value Amount</div>
		<div class="collapse-content mx-0 px-0 text-sm">
			<div class="join w-full">
				<!-- svelte-ignore a11y_consider_explicit_label -->
				<button
					class="join-item btn btn-primary w-1/5"
					onclick={() => (Increment = (Number(Increment) - 0.1).toFixed(2))}
				>
					<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
						><path fill="currentColor" d="M6 13v-2h12v2z" /></svg
					>
				</button>
				<input
					type="text"
					class="join-item input w-3/5 text-center text-xs focus:border-0"
					bind:value={Increment}
					oninput={(e) => {
						if (isNaN(Number(e.currentTarget.value))) {
							if (e.currentTarget.value[e.currentTarget.value.length - 1] !== '.')
								Increment = e.currentTarget.value.replace(/[^\d.]/g, '');
						} else {
							Increment = e.currentTarget.value;
							if (Increment[0] === '.') Increment = Increment.slice(1);
						}
					}}
				/>
				<!-- svelte-ignore a11y_consider_explicit_label -->
				<button
					class="join-item btn btn-primary w-1/5"
					onclick={() => (Increment = Number(Increment + 0.1).toFixed(2))}
				>
					<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
						><path fill="currentColor" d="M11 21v-8H3v-2h8V3h2v8h8v2h-8v8z" /></svg
					>
				</button>
			</div>
		</div>
	</div>
</div>

{#if errorValidation !== ''}
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
			<h3 class="text-error-content font-bold">Validation Error</h3>
			<div class="text-error-content text-xs font-semibold">{errorValidation}</div>
		</div>
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button class="btn btn-sm btn-ghost btn-circle" onclick={() => (errorValidation = '')}>
			<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
				><path
					fill="currentColor"
					d="M6.4 19L5 17.6l5.6-5.6L5 6.4L6.4 5l5.6 5.6L17.6 5L19 6.4L13.4 12l5.6 5.6l-1.4 1.4l-5.6-5.6z"
				/></svg
			>
		</button>
	</div>
{/if}

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
