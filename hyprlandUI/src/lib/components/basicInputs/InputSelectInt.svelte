<script lang="ts">
	import { CmpStr, type CmpStrResult } from 'cmpstr';
	import {
		ActionLinks,
		uiStore,
		updateChange,
		websocketConnection,
		type MainPageInputData,
		type MainPageInputUI,
		type SendMainStandedUpdate
	} from '$lib';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let open = $state(false);
	let dialogRef = $state(null as null | HTMLDialogElement);
	let search = $state('');
	let filterSelect = $state([] as { value: string; index: number }[]);
	let selected = $state(null as { value: string; index: number } | null);

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

	function changeUpdate(actionLink: ActionLinks, num: number) {
		if (ui.validation.optionsInt !== undefined && ui.validation.optionsInt !== null) {
			const include = ui.validation.optionsInt.includes(num);
			if (!include) {
				errorValidation = ui.validationError;
				selected = null;
			}
		}

		const message: SendMainStandedUpdate = {
			name: data.settingsName,
			value: `${num}`,
			type: data.typeOfHyprland,
			category: data.category
		};

		websocketConnection.sendActionToMainUpdate(actionLink, message);
	}

	function updateSelectingValue(value: string) {
		if (value === '') return;

		if (!isNaN(Number(value))) {
			const sortedResult = [] as { value: string; index: number }[];

			if ((ui.valueOptions as number[])!!.length < Number(value)) {
				return;
			}

			filterSelect.forEach((item) => {
				if (item.index === Number(value)) {
					sortedResult.unshift(item);
				} else {
					sortedResult.push(item);
				}
			});

			filterSelect = sortedResult;
			return;
		}

		const cmp = CmpStr.create({ metric: 'levenshtein', flags: 'i' });

		if (filterSelect.length !== 0) {
			const resultForSort = [] as string[];

			filterSelect.forEach((item) => resultForSort.push(item.value));

			const result = cmp.batchSorted(value.toLocaleLowerCase(), resultForSort) as CmpStrResult[];

			const sortedResult = [] as { value: string; index: number }[];

			result
				.sort((a, b) => b.match - a.match)
				.forEach((items) => {
					const filter = filterSelect.find((item) => item.value === items.target);

					if (filter !== undefined) {
						sortedResult.push(filter);
					}
				});

			filterSelect = [];
			filterSelect.push(...sortedResult);
		}
	}

	$effect(() => {
		if (
			updateChange.error?.name === data.settingsName &&
			updateChange.error.category === data.category &&
			updateChange.error.error !== ''
		) {
			if (selected === null && ui.optionExplain !== null && ui.value !== null && ui.value !== '') {
				selected = {
					value: ui.optionExplain!![(ui.valueOptions as string[])!!.indexOf(ui.value as string)],
					index: (ui.valueOptions as string[])!!.indexOf(ui.value as string)
				};
			}
		}

		if (selected === null && ui.optionExplain !== null && ui.value !== null && ui.value !== '') {
			selected = {
				value: ui.optionExplain!![(ui.valueOptions as number[])!!.indexOf(ui.value as number)],
				index: (ui.valueOptions as string[])!!.indexOf(ui.value as string)
			};
		}

		if (open && !dialogRef?.open) {
			dialogRef?.showModal();

			if (filterSelect.length === 0) {
				ui.optionExplain?.forEach((item, index) => {
					filterSelect.push({
						value: item,
						index: (ui.valueOptions as number[])!![index]
					});
				});
			}
		} else if (!open && dialogRef?.open) {
			dialogRef.close();
		}
	});
</script>

<div class="flex w-full flex-col gap-3">
	<div class="flex flex-row items-center justify-between">
		<div class="flex flex-col gap-[2px]">
			<h4 class="text-sm font-bold capitalize">{data.name}</h4>
			<p class="text-base-content/60 max-w-[700px] text-xs font-medium text-wrap capitalize">
				{data.description.replaceAll(';', ',')}
			</p>
		</div>
	</div>
	<!-- svelte-ignore a11y_label_has_associated_control -->
	<label class="input w-full bg-transparent">
		<svg class="h-[1em] opacity-50" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
			<g
				stroke-linejoin="round"
				stroke-linecap="round"
				stroke-width="2.5"
				fill="none"
				stroke="currentColor"
			>
				<circle cx="11" cy="11" r="8"></circle>
				<path d="m21 21-4.3-4.3"></path>
			</g>
		</svg>
		<!-- svelte-ignore a11y_no_static_element_interactions -->
		<!-- svelte-ignore a11y_click_events_have_key_events -->
		<div class="grow" onclick={() => (open = !open)}>
			{#if selected !== null}
				<p class="text text-xs font-medium">{selected.value}</p>
			{:else}
				<p class="text text-base-content/60 text-xs font-medium">
					Select One Of The Options Below Or Leave Empty
				</p>
			{/if}
		</div>
	</label>
</div>

<dialog id="my_modal_1" class="modal" bind:this={dialogRef}>
	<div class="modal-box max-w-4xl bg-transparent">
		<div class="bg-base-300 w-3xl">
			<label
				class="input w-full border-0 border-b-1 bg-transparent text-sm shadow-none focus-within:outline-0 focus:border-b-1 focus:outline-0"
			>
				<svg class="h-[1em] opacity-50" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
					<g
						stroke-linejoin="round"
						stroke-linecap="round"
						stroke-width="2.5"
						fill="none"
						stroke="currentColor"
					>
						<circle cx="11" cy="11" r="8"></circle>
						<path d="m21 21-4.3-4.3"></path>
					</g>
				</svg>
				<input
					type="search"
					class="grow text-sm ring-0"
					placeholder="Search"
					bind:value={search}
					oninput={(e) => updateSelectingValue(e.currentTarget.value)}
					onkeypress={(e) => {
						if (e.key.toLocaleLowerCase() === 'enter') {
							selected = filterSelect[0];
							search = '';
							open = false;
							changeUpdate(uiStore.activeSidebar!!, selected.index);
						}
					}}
				/>
			</label>
			<div class="flex flex-col gap-2 p-3 py-4">
				{#if filterSelect.length !== 0}
					{#each filterSelect as option, index}
						<div
							class="bg-base-200/60 border-base-100 flex flex-row justify-start gap-2 rounded-md border p-2 {index ===
							0
								? 'border-base-content/60 border'
								: ''}"
						>
							<p class="text-base-content/60 text-xs font-light">
								{option.index}
							</p>
							<p class="w-full text-xs font-semibold text-wrap capitalize">{option.value}</p>
						</div>
					{/each}
				{/if}
			</div>
		</div>
	</div>
	<form method="dialog" class="modal-backdrop">
		<button>close</button>
	</form>
</dialog>

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
