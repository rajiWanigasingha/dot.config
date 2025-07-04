<script lang="ts">
	import {
		ActionLinks,
		mainConn,
		sidebarState,
		updateChange,
		type MainPageInputData,
		type MainPageInputUI
	} from '$lib';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let value = $state(ui.value as string);

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
			value = ui.value as string;
		}
	});

	function changeUpdate(actionLink: ActionLinks, str: string) {
		if (ui.validation.regex !== undefined && ui.validation.regex !== null) {
			const regex = new RegExp(ui.validation.regex);

			if (!regex.test(str)) {
				errorValidation = ui.validationError;
				return;
			}
		}

		mainConn.update(actionLink, {
			name: data.settingsName,
			value: `${str}`,
			type: data.typeOfHyprland,
			category: data.category
		});
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
				class="btn btn-circle btn-sm btn-accent btn-soft"
				onclick={() => changeUpdate(sidebarState.sidebarState.sidebarActive, value)}
			>
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
					><path fill="currentColor" d="m10 16.4l-4-4L7.4 11l2.6 2.6L16.6 7L18 8.4z" /></svg
				>
			</button>
		</div>
	</div>
	<div>
		<input
			type="text"
			placeholder={ui.placeholder}
			class="input w-full bg-transparent text-xs font-medium"
			bind:value
		/>
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
