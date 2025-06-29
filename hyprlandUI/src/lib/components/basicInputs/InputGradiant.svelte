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
	import { onMount } from 'svelte';
	import ColorPicker from 'svelte-awesome-color-picker';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let divEl: HTMLDivElement;
	let width = $state(0);
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

	let gradiant = $state([] as string[]);
	let angle = $state(0);
	let gradiantHex = $state([] as string[]);

	$effect(() => {
		if (
			updateChange.error?.name === data.settingsName &&
			updateChange.error.category === data.category &&
			updateChange.error.error !== ''
		) {
			if (ui.value !== null && ui.value !== '') {
				const gradiantValue = (ui.value as string).match(
					/rgba\(\s*\d+\s*,\s*\d+\s*,\s*\d+\s*,\s*[\d.]+\s*\)|-?\d+deg/g
				);

				if (gradiantValue === null || gradiantValue.length === 0) return;

				const settings = [] as string[];
				const settingsHex = [] as string[];

				if (gradiantValue.length >= 2) {
					const lastValue = gradiantValue.pop();

					const haveAngle = lastValue!!.match(/-?\d+deg/g);

					if (haveAngle !== null) {
						angle = !isNaN(Number(lastValue?.replace('deg', '').trim()))
							? Number(lastValue?.replace('deg', '').trim())
							: 0;
					} else {
						if (lastValue !== undefined) gradiantValue.push(lastValue);
					}
				}

				gradiantValue.forEach((item) => {
					const rgbaValues = item
						.split('(')[1]
						.split(')')[0]
						.split(',')
						.map((item) => item.trim());

					settings.push(
						`rgba(${rgbaValues[0]} ,${rgbaValues[1]} ,${rgbaValues[2]} ,${rgbaValues[3]})`
					);

					settingsHex.push(`rgba(${rgbaToHex(rgb.r, rgb.g, rgb.b, rgb.a)})`);
				});

				gradiant = [];
				gradiant.push(...settings);
				gradiantHex = [];
				gradiantHex.push(...settingsHex);

				const rgbaFirst = gradiant[0]
					.split('(')[1]
					.split(')')[0]
					.split(',')
					.map((item) => item.trim());

				rgb = {
					r: Number(rgbaFirst[0]),
					g: Number(rgbaFirst[1]),
					b: Number(rgbaFirst[2]),
					a: Number(rgbaFirst[3])
				};
			}
		}
	});

	onMount(() => {
		width = divEl.offsetWidth;

		if (ui.value !== null && ui.value !== '') {
			const gradiantValue = (ui.value as string).match(
				/rgba\(\s*\d+\s*,\s*\d+\s*,\s*\d+\s*,\s*[\d.]+\s*\)|-?\d+deg/g
			);

			if (gradiantValue === null || gradiantValue.length === 0) return;

			const settings = [] as string[];
			const settingsHex = [] as string[];

			if (gradiantValue.length >= 2) {
				const lastValue = gradiantValue.pop();

				const haveAngle = lastValue!!.match(/-?\d+deg/g);

				if (haveAngle !== null) {
					angle = !isNaN(Number(lastValue?.replace('deg', '').trim()))
						? Number(lastValue?.replace('deg', '').trim())
						: 0;
				} else {
					if (lastValue !== undefined) gradiantValue.push(lastValue);
				}
			}

			gradiantValue.forEach((item) => {
				const rgbaValues = item
					.split('(')[1]
					.split(')')[0]
					.split(',')
					.map((item) => item.trim());

				settings.push(
					`rgba(${rgbaValues[0]} ,${rgbaValues[1]} ,${rgbaValues[2]} ,${rgbaValues[3]})`
				);

				settingsHex.push(`rgba(${rgbaToHex(rgb.r, rgb.g, rgb.b, rgb.a)})`);
			});

			gradiant = [];
			gradiant.push(...settings);
			gradiantHex = [];
			gradiantHex.push(...settingsHex);

			const rgbaFirst = gradiant[0]
				.split('(')[1]
				.split(')')[0]
				.split(',')
				.map((item) => item.trim());

			rgb = {
				r: Number(rgbaFirst[0]),
				g: Number(rgbaFirst[1]),
				b: Number(rgbaFirst[2]),
				a: Number(rgbaFirst[3])
			};
		}
	});

	let rgb = $state({
		r: 79,
		g: 116,
		b: 202,
		a: 0.95
	});

	function changeUpdate(actionLink: ActionLinks, gradiant: string) {
		const message: SendMainStandedUpdate = {
			name: data.settingsName,
			value: `${gradiant}`,
			type: data.typeOfHyprland,
			category: data.category
		};

		websocketConnection.sendActionToMainUpdate(actionLink, message);
	}

	function decToHex(value: number): string {
		return Math.max(0, Math.min(255, Math.round(value)))
			.toString(16)
			.padStart(2, '0');
	}

	function rgbaToHex(r: number, g: number, b: number, a: number): string {
		const alpha = a * 255;
		return `${decToHex(r)}${decToHex(g)}${decToHex(b)}${decToHex(alpha)}`;
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
		<div class="flex flex-row items-center gap-3">
			<div
				class="mr-3 h-[30px] w-[150px] rounded-xl shadow-md"
				style="background: linear-gradient({angle}deg, {gradiant.join(' ,')});"
			></div>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button
				class="btn btn-sm btn-circle btn-soft btn-success"
				onclick={() => {
					if (gradiantHex.length === 0) {
						errorValidation = 'No Gradiant Colors Were Found';
						return;
					}

					changeUpdate(uiStore.activeSidebar!!, `${gradiantHex.join(' ')} ${angle}deg`);
				}}
			>
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
					><path fill="currentColor" d="M11 13H5v-2h6V5h2v6h6v2h-6v6h-2z" /></svg
				>
			</button>
		</div>
	</div>
	<div class="flex flex-row items-center justify-between">
		<div>
			<p class="text-sm font-semibold">{angle} Deg</p>
			<p class="text-xs font-medium">Angle Of Gradiant</p>
		</div>
		<div class="flex flex-row items-center gap-2">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button
				class="btn btn-sm btn-soft btn-primary btn-square"
				onclick={() => (angle = angle - 10)}
			>
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
					><path fill="currentColor" d="M19 12.998H5v-2h14z" /></svg
				>
			</button>
			<input
				type="text"
				class="input input-xl border-0 border-b-1 bg-transparent text-center text-sm shadow-none focus:border-b-1 focus:outline-0"
				bind:value={angle}
			/>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button
				class="btn btn-sm btn-soft btn-primary btn-square"
				onclick={() => (angle = angle + 10)}
			>
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
					><path fill="currentColor" d="M11 13H5v-2h6V5h2v6h6v2h-6v6h-2z" /></svg
				>
			</button>
		</div>
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
		<div class="join flex w-full flex-row justify-center">
			<button
				class="btn btn-error btn-soft join-item grow border text-xs"
				onclick={() => {
					gradiant = [];
					gradiantHex = [];
				}}>Reset Gradiant</button
			>
			<button
				class="btn btn-info btn-soft join-item grow text-xs"
				onclick={() => {
					gradiant.push(`rgba(${rgb.r} ,${rgb.g} ,${rgb.b} ,${rgb.a})`);
					gradiantHex.push(`rgba(${rgbaToHex(rgb.r, rgb.g, rgb.b, rgb.a)})`);
				}}>Add To Gradiant</button
			>
		</div>
		<div class="flex w-full flex-col gap-2">
			{#each gradiant as color, index}
				<div
					class="bg-base-100 border-base-content/20 flex w-full flex-row items-center justify-between border p-2"
				>
					<div class="flex flex-row items-center gap-3">
						<div class="h-[20px] w-[20px]" style="background-color: {color};"></div>
						<p class="text-xs">{color}</p>
					</div>
					<div>
						<!-- svelte-ignore a11y_consider_explicit_label -->
						<button
							class="btn btn-sm btn-soft btn-error btn-circle"
							onclick={() => {
								gradiant.splice(index, 1);
								gradiantHex.splice(index, 1);
							}}
						>
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
								><path
									fill="currentColor"
									d="m8.382 17.025l-1.407-1.4L10.593 12L6.975 8.4L8.382 7L12 10.615L15.593 7L17 8.4L13.382 12L17 15.625l-1.407 1.4L12 13.41z"
								/></svg
							>
						</button>
					</div>
				</div>
			{/each}
		</div>
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

<style>
	.dark {
		--cp-bg-color: var(--color-base-200);
		--cp-border-color: var(--color-base-200);
		--cp-text-color: var(--color-base-content);
		--cp-input-color: var(--color-base-300);
		--cp-button-hover-color: var(--color-primary);
	}
</style>
