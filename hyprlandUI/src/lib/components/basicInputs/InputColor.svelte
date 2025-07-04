<script lang="ts">
	import {
		mainConn,
		sidebarState,
		updateChange,
		type ActionLinks,
		type MainPageInputData,
		type MainPageInputUI
	} from '$lib';
	import { onMount } from 'svelte';
	import ColorPicker from 'svelte-awesome-color-picker';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let divEl: HTMLDivElement;
	let width = $state(0);
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

	onMount(() => {
		width = divEl.offsetWidth;
	});

	let rgb = $state({
		r: 79,
		g: 116,
		b: 202,
		a: 0.95
	});

	$effect(() => {
		if (
			updateChange.error?.name === data.settingsName &&
			updateChange.error.category === data.category &&
			updateChange.error.error !== ''
		) {
			const rgbaValues = (ui.value as string)
				.split('(')[1]
				.split(')')[0]
				.split(',')
				.map((item) => item.trim());

			rgb = {
				r: Number(rgbaValues[0]),
				g: Number(rgbaValues[1]),
				b: Number(rgbaValues[2]),
				a: Number(rgbaValues[3])
			};
		}
	});

	if (ui.value !== null && ui.value !== '') {
		const rgbaValues = (ui.value as string)
			.split('(')[1]
			.split(')')[0]
			.split(',')
			.map((item) => item.trim());

		rgb = {
			r: Number(rgbaValues[0]),
			g: Number(rgbaValues[1]),
			b: Number(rgbaValues[2]),
			a: Number(rgbaValues[3])
		};
	}

	function changeUpdate(actionLink: ActionLinks, rgb: string) {
		mainConn.update(actionLink, {
			name: data.settingsName,
			value: `${rgb}`,
			type: data.typeOfHyprland,
			category: data.category
		});
	}
</script>

<div class="flex w-full flex-col gap-3" bind:this={divEl}>
	<div class="flex flex-row items-center justify-between">
		<div class="flex flex-col gap-[2px]">
			<h4 class="text-sm font-bold capitalize">{data.name}</h4>
			<p class="text-base-content/60 max-w-[700px] text-xs font-medium text-wrap capitalize">
				{data.description.replaceAll(';', ',')}
			</p>
		</div>
		<div
			class="mr-3 h-[30px] w-[50px] rounded-xl shadow-md"
			style="background-color: rgba({rgb.r}, {rgb.g}, {rgb.b}, {rgb.a});"
		></div>
	</div>
	<div class="dark flex w-full flex-col items-center justify-center gap-2">
		<ColorPicker
			bind:rgb
			sliderDirection="vertical"
			isDialog={false}
			isTextInput={false}
			--picker-height="{(width * 1) / 5}px"
			--picker-width="{(width * 17) / 18}px"
			--slider-width="15px"
			--picker-indicator-size="15px"
		/>
		<div class="flex flex-row gap-2">
			<label class="input">
				<span class="label">R</span>
				<input
					type="text"
					class="w-full grow text-center text-xs ring-0"
					placeholder="Color Red"
					bind:value={rgb.r}
				/>
			</label>
			<label class="input">
				<span class="label">G</span>
				<input
					type="text"
					class="w-full grow text-center text-xs ring-0"
					placeholder="Color Green"
					bind:value={rgb.g}
				/>
			</label>
			<label class="input">
				<span class="label">B</span>
				<input
					type="text"
					class="w-full grow text-center text-xs ring-0"
					placeholder="Color Blue"
					bind:value={rgb.b}
				/>
			</label>
			<label class="input">
				<span class="label">A</span>
				<input
					type="text"
					class="w-full grow text-center text-xs ring-0"
					placeholder="Color Alpha"
					bind:value={rgb.a}
				/>
			</label>
		</div>
		<button
			class="btn btn-success btn-soft w-full"
			onclick={() =>
				changeUpdate(
					sidebarState.sidebarState.sidebarActive,
					`rgba(${rgb.r} ,${rgb.g} ,${rgb.b} ,${rgb.a})`
				)}>Apply This Color</button
		>
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

<style>
	.dark {
		--cp-bg-color: var(--color-base-200);
		--cp-border-color: var(--color-base-200);
		--cp-text-color: var(--color-base-content);
		--cp-input-color: var(--color-base-300);
		--cp-button-hover-color: var(--color-primary);
	}
</style>
