<script lang="ts">
	import { GetIcons, monitorConn, monitorState, type MonitorData } from '$lib';

	let changedMonitor = $state(null as null | number);
	let bitDepth = $state(null as null | boolean);
	let sdrbrightness = $state(null as null | number);
	let sdrsaturation = $state(null as null | number);

	const colorModes = [
		{ name: 'Auto (sRGB for 8bpc, Wide for 10bpc)', value: 'auto' },
		{ name: 'sRGB (Standard RGB primaries)', value: 'srgb' },
		{ name: 'Wide (Wide color gamut, BT2020 primaries)', value: 'wide' },
		{ name: 'EDID (Primaries from EDID - may be inaccurate)', value: 'edid' },
		{ name: 'HDR (Wide gamut + HDR PQ transfer, experimental)', value: 'hdr' },
		{ name: 'HDR + EDID (HDR PQ + EDID primaries, experimental)', value: 'hdredid' }
	];

	function updateCM(monitor: MonitorData, cm: string) {
		const monitorData = {
			...monitor,
			cm: cm
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updateBit(monitor: MonitorData, bit: boolean) {
		const monitorData = {
			...monitor,
			bitDepth: bit ? 10 : null
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updateSDRBrightness(monitor: MonitorData, sdr: number | null) {
		const monitorData = {
			...monitor,
			sdrbrightness: sdr
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updateSDRSaturation(monitor: MonitorData, sdr: number | null) {
		const monitorData = {
			...monitor,
			sdrsaturation: sdr
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	$effect(() => {
		if (
			monitorState.getActive().monitor !== null &&
			changedMonitor !== monitorState.store.availableIndex
		) {
			changedMonitor = monitorState.store.availableIndex;

			sdrbrightness = monitorState.getActive().monitor?.sdrbrightness ?? null;

			sdrsaturation = monitorState.getActive().monitor?.sdrsaturation ?? null;

			bitDepth = monitorState.getActive().monitor!!.bitDepth !== null ? true : false;
		}
	});
</script>

<div class="p-4">
	<fieldset class="fieldset">
		<legend class="fieldset-legend">Color Managment</legend>
		<select
			class="select select-sm bg-base-300/60 border-base-content/10 h-fit w-full"
			onchange={(e) => updateCM(monitorState.getActive().monitor!!, e.currentTarget.value)}
		>
			{#each colorModes as mods}
				<option
					value={mods.value}
					selected={monitorState.getActive().monitor !== undefined &&
					monitorState.getActive().monitor?.cm !== null
						? monitorState.getActive().monitor!!.cm === mods.value
						: false}>{mods.name}</option
				>
			{/each}
		</select>
		<p class="label">Active Color Managment</p>
	</fieldset>
	<div class="divider my-3"></div>
	<div class="flex flex-row justify-between">
		<div class="flex flex-col gap-1">
			<p class="text-xs font-medium">10 Bit Support</p>
			<p class="text-base-content/60 text-xs">Activate 10 bit support for More Accurate Colors</p>
		</div>
		<label class="toggle toggle-xl text-base-conten">
			<input
				type="checkbox"
				class="hidden"
				bind:checked={bitDepth}
				onchange={(e) => updateBit(monitorState.getActive().monitor!!, e.currentTarget.checked)}
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
	<div class="divider my-3"></div>
	<div class="flex flex-col justify-between">
		<div class="flex flex-col gap-1">
			<p class="text-xs font-medium">SDR Brighness</p>
			<p class="text-base-content/60 text-xs">
				Controls brightness for Standard Dynamic Range content. Useful for displays that support
				both SDR and HDR.
			</p>
		</div>
		<div class="pt-3">
			<div class="join w-full">
				<input
					type="text"
					class="input join-item bg-base-300/60 border-base-content/10 h-fit w-4/5 p-3 text-xs focus:ring-0"
					placeholder="1.2"
					bind:value={sdrbrightness}
				/>
				<button
					class="btn btn-success join-item w-1/5 p-3 text-xs"
					onclick={() => updateSDRBrightness(monitorState.getActive().monitor!!, sdrbrightness)}
					>Change</button
				>
			</div>
			<div class="join-item flex flex-row pt-3">
				<button
					class="btn btn-primary join-item w-1/2"
					onclick={() => {
						if (sdrbrightness) {
							sdrbrightness = Number((sdrbrightness - 0.1).toFixed(1));
						} else {
							sdrbrightness = 1.0;
						}
					}}
				>
					{@html GetIcons('minus')}
				</button>
				<button
					class="btn btn-primary join-item w-1/2"
					onclick={() => {
						if (sdrbrightness) {
							sdrbrightness = Number((sdrbrightness + 0.1).toFixed(1));
						} else {
							sdrbrightness = 1.0;
						}
					}}
				>
					{@html GetIcons('add')}
				</button>
			</div>
			<div class="pt-3">
				<button
					class="btn btn-primary w-full text-xs font-medium"
					onclick={() => {
						sdrbrightness = null;
						updateSDRBrightness(monitorState.getActive().monitor!!, sdrbrightness);
					}}>Set Back To Default</button
				>
			</div>
		</div>
	</div>
	<div class="divider my-3"></div>
	<div class="flex flex-col justify-between">
		<div class="flex flex-col gap-1">
			<p class="text-xs font-medium">SDR Saturation</p>
			<p class="text-base-content/60 text-xs">
				Adjusts color intensity for Standard Dynamic Range content. Useful for fine-tuning
				appearance on wide-gamut or HDR-capable displays.
			</p>
		</div>
		<div class="pt-3">
			<div class="join w-full">
				<input
					type="text"
					class="input join-item bg-base-300/60 border-base-content/10 h-fit w-4/5 p-3 text-xs focus:ring-0"
					placeholder="1.2"
					bind:value={sdrsaturation}
				/>
				<button
					class="btn btn-success join-item w-1/5 p-3 text-xs"
					onclick={() => updateSDRSaturation(monitorState.getActive().monitor!!, sdrsaturation)}
					>Change</button
				>
			</div>
			<div class="join-item flex flex-row pt-3">
				<button
					class="btn btn-primary join-item w-1/2"
					onclick={() => {
						if (sdrsaturation) {
							sdrsaturation = Number((sdrsaturation - 0.1).toFixed(1));
						} else {
							sdrsaturation = 1.0;
						}
					}}
				>
					{@html GetIcons('minus')}
				</button>
				<button
					class="btn btn-primary join-item w-1/2"
					onclick={() => {
						if (sdrsaturation) {
							sdrsaturation = Number((sdrsaturation + 0.1).toFixed(1));
						} else {
							sdrsaturation = 1.0;
						}
					}}
				>
					{@html GetIcons('add')}
				</button>
			</div>
			<div class="pt-3">
				<button
					class="btn btn-primary w-full text-xs font-medium"
					onclick={() => {
						sdrsaturation = null;
						updateSDRSaturation(monitorState.getActive().monitor!!, sdrsaturation);
					}}>Set Back To Default</button
				>
			</div>
		</div>
	</div>
</div>
