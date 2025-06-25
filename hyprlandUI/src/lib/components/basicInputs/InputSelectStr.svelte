<script lang="ts">
	import { CmpStr, type CmpStrResult } from 'cmpstr';
	import type { MainPageInputData, MainPageInputUI } from '$lib';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let selectedValue = $state(null as string | null);
	let selectedItme = $state(ui.value as string | null);
	let focusOn = $state(null as string | null);

	let selectShow = $state(false);

	if (ui.optionExplain !== null && ui.value !== null && ui.value !== '') {
		selectedValue = ui.optionExplain[(ui.valueOptions as string[]).indexOf(ui.value as string)];
		focusOn = ui.optionExplain[(ui.valueOptions as string[]).indexOf(ui.value as string)];
	}

	function changeSelectedValue(value: string) {
		if (value === '') return;

		const cmp = CmpStr.create({
			metric: 'levenshtein', // use Levenshtein distance
			flags: 'i' // case-insensitive
		});

		const explain = ui.optionExplain;

		if (explain === null) return;

		const result = (cmp.batchTest(value.toLocaleLowerCase(), [...explain]) as CmpStrResult[]).sort(
			(item1, item2) => item2.match - item1.match
		);

		focusOn = result[0].target;
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
	</div>
	<div class="relative inline-block">
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
			{#if ui.optionExplain !== null && ui.valueOptions !== null}
				<input
					name={data.settingsName}
					type="search"
					class="grow text-xs font-medium ring-0"
					placeholder={data.description}
					onfocusin={() => (selectShow = true)}
					onfocusout={() => (selectShow = false)}
					bind:value={selectedValue}
					oninput={(e) => changeSelectedValue(e.currentTarget.value)}
					onkeypress={(e) => {
						if (e.key.toLocaleLowerCase() === 'enter') {
							selectedItme = focusOn;
							selectedValue = ui.optionExplain!![ui.optionExplain!!.indexOf(selectedItme!!)];
							e.currentTarget.blur();
						}
					}}
				/>
			{/if}
		</label>
		<div
			class="bg-base-100 absolute z-20 mt-4 mb-8 flex h-fit w-full flex-col gap-3 border-1 p-4 {selectShow
				? ''
				: 'hidden'}"
		>
			{#if ui.optionExplain !== null && ui.valueOptions !== null}
				{#each ui.optionExplain as option, index}
					<div
						class="{selectedItme === option ? 'bg-base-300' : ''}
				{focusOn === option ? 'border-1' : ''}
				flex flex-row items-center justify-between p-3"
					>
						<div class="flex flex-row gap-2">
							<p class="text-base-content/60 w-[50px] text-xs">{ui.valueOptions[index]}</p>
							<div class="divider divider-horizontal"></div>
							<p class="max-w-[600px] text-xs font-semibold text-wrap">{option}</p>
						</div>
						<div>
							<!-- svelte-ignore a11y_consider_explicit_label -->
							{#if selectedItme === option}
								<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
									><path
										fill="currentColor"
										d="M9 16.17L4.83 12l-1.42 1.41L9 19L21 7l-1.41-1.41z"
									/></svg
								>
							{/if}
						</div>
					</div>
				{/each}
			{/if}
		</div>
	</div>
</div>
