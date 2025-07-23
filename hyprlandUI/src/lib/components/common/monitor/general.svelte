<script lang="ts">
	import { GetIcons, monitorConn, monitorState, type MonitorData } from '$lib';
	import { onMount } from 'svelte';

	let selectRes = $state('supported');

	let resolutions = $state([] as string[]);
	let changedMonitor = $state(null as null | number);
	let position = $state('');
	let scale = $state('');
	let vrr = $state(0);

	const rotate = [
		{ rotate: 0, text: 'Normal (no rotation)' },
		{ rotate: 1, text: 'Rotated 90° clockwise' },
		{ rotate: 2, text: 'Rotated 180°' },
		{ rotate: 3, text: 'Rotated 270° clockwise' },
		{ rotate: 4, text: 'Flipped horizontally (mirrored)' },
		{ rotate: 5, text: 'Flipped + rotated 90°' },
		{ rotate: 6, text: 'Flipped + rotated 180°' },
		{ rotate: 7, text: 'Flipped + rotated 270°' }
	];

	$effect(() => {
		if (
			monitorState.getActive().monitor !== null &&
			changedMonitor !== monitorState.store.availableIndex
		) {
			resolutions = [
				'preferred',
				'highres@highrr',
				...monitorState.getActive().monitorInfo!!.availableModes.map((item) => {
					return item.replace('Hz', '').trim();
				}),
				monitorState.getActive().monitor!!.resolution
			];

			resolutions = [...new Set(resolutions)];
			changedMonitor = monitorState.store.availableIndex;

			position = monitorState.getActive().monitor?.position ?? 'auto';

			scale = monitorState.getActive().monitor?.scale ?? 'auto';

			vrr =
				(monitorState.getActive().monitor?.vrr ?? monitorState.getActive().monitorInfo?.vrr)
					? 1
					: 0;
		}
	});

	function updateResolution(monitor: MonitorData, resolution: string) {
		const monitorData = {
			...monitor,
			resolution: resolution
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updatePosistion(monitor: MonitorData, position: string) {
		const monitorData = {
			...monitor,
			position: position
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updateScale(monitor: MonitorData, scale: string) {
		const monitorData = {
			...monitor,
			scale: scale
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updateRotation(monitor: MonitorData, transform: number) {
		const monitorData = {
			...monitor,
			transform: transform
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}

	function updateVrr(monitor: MonitorData, vrr: number) {
		const monitorData = {
			...monitor,
			vrr: vrr
		};

		if (monitor.name === '') {
			monitorData.name = monitorState.getActive().monitorInfo!!.name;
		}

		monitorConn.changeGeneral(monitorData);
	}
</script>

<div class="p-8">
	{#if monitorState.getActive().monitor !== null}
		<div class="mockup-code bg-base-100 flex w-full flex-col gap-1 text-xs">
			<pre data-prefix="$"><code class="text-base-content/60">Basic Information</code></pre>
			<pre></pre>
			<pre data-prefix=">"><code class="text-info-content">&#123</code></pre>

			<pre data-prefix=">"><code
					><span class="text-blue-400">  "id"</span>: <span class="text-amber-300"
						>{monitorState.getActive().monitorInfo?.id}</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "name"</span>: <span class="text-green-400"
						>"{monitorState.getActive().monitorInfo?.name}"</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "description"</span>: <span class="text-green-400"
						>"{monitorState.getActive().monitorInfo?.description}"</span
					>,</code
				></pre>

			<pre data-prefix=">"><code
					><span class="text-blue-400">  "make_and_model"</span>: <span class="text-info-content"
						>&#123</span
					></code
				></pre>
			<pre data-prefix=">"><code>    <span class="text-blue-400">"make"</span>: <span
						class="text-green-400">"{monitorState.getActive().monitorInfo?.make}"</span
					>,</code
				></pre>
			<pre data-prefix=">"><code>    <span class="text-blue-400">"model"</span>: <span
						class="text-green-400">"{monitorState.getActive().monitorInfo?.model}"</span
					></code
				></pre>
			<pre data-prefix=">"><code class="text-info-content">  &#125</code></pre>

			<pre data-prefix=">"><code
					><span class="text-blue-400">  "dpmsStatus"</span>: <span class="text-amber-300"
						>{monitorState.getActive().monitorInfo?.dpmsStatus}</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "vrr"</span>: <span class="text-amber-300"
						>{monitorState.getActive().monitorInfo?.vrr}</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "activelyTearing"</span>: <span class="text-amber-300"
						>{monitorState.getActive().monitorInfo?.activelyTearing}</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "solitary"</span>: <span class="text-amber-300"
						>{monitorState.getActive().monitorInfo?.solitary}</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "directScanoutTo"</span>: <span class="text-amber-300"
						>{monitorState.getActive().monitorInfo?.directScanoutTo}</span
					>,</code
				></pre>
			<pre data-prefix=">"><code
					><span class="text-blue-400">  "currentFormat"</span>: <span class="text-green-400"
						>"{monitorState.getActive().monitorInfo?.currentFormat}"</span
					></code
				></pre>

			<pre data-prefix=">"><code class="text-info-content">&#125</code></pre>
		</div>
	{/if}

	<div class="divider"></div>

	<fieldset class="fieldset">
		<legend class="fieldset-legend">Resolution & Refresh Rate</legend>
		<select
			class="select select-sm bg-base-300/60 border-base-content/10 h-fit w-full focus:outline-0"
			oninput={(e) => (selectRes = e.currentTarget.value)}
		>
			<option value="supported" selected>Supported Resolution</option>
			<option value="manual">Manual Resolution</option>
		</select>
		{#if selectRes === 'supported'}
			<select
				class="select select-sm bg-base-300/60 border-base-content/10 my-2 h-fit w-full"
				onchange={(e) =>
					updateResolution(monitorState.getActive().monitor!!, e.currentTarget.value)}
			>
				{#each resolutions as res}
					<option value={res} selected={monitorState.getActive().monitor?.resolution === res}
						>{res}</option
					>
				{/each}
			</select>
		{:else}
			<div class="join my-2 w-full">
				<label class="input join-item bg-base-300/60 border-base-content/10 h-fit w-2/5 text-xs">
					Resolution
					<input type="text" class="grow p-3 text-xs focus:ring-0" placeholder="1920x1080" />
				</label>
				<label class="input join-item bg-base-300/60 border-base-content/10 h-fit w-2/5 text-xs">
					Refresh Rate
					<input type="text" class="w-2/5 grow p-3 text-xs focus:ring-0" placeholder="60" />
				</label>
				<button class="btn btn-success w-1/5 p-3 text-xs">Change</button>
			</div>
		{/if}
		<p class="label">You can edit Resolution Of The Selected Or Default Monitor</p>
	</fieldset>

	<div class="divider my-3"></div>

	<fieldset class="fieldset">
		<legend class="fieldset-legend">Position</legend>
		<div class="join">
			<input
				type="text"
				class="input bg-base-300/60 border-base-content/10 join-item h-4 w-full p-5 text-xs"
				bind:value={position}
				placeholder="0x0"
			/>
			<button
				class="btn btn-success join-item p-5 text-xs"
				onclick={() => updatePosistion(monitorState.getActive().monitor!!, position)}>Change</button
			>
		</div>
		<p class="label">
			Position Of The Monitor. It should be formmat of leftxtop (0x0) or auto or
			auto-right/left/up/down
		</p>
	</fieldset>

	<div class="divider my-3"></div>

	<fieldset class="fieldset">
		<legend class="fieldset-legend">Scale</legend>
		<div class="join">
			<input
				type="text"
				class="input bg-base-300/60 border-base-content/10 join-item h-4 w-full p-5 text-xs"
				placeholder="1"
				bind:value={scale}
			/>
			<button
				class="btn btn-success join-item p-5 text-xs"
				onclick={() => updateScale(monitorState.getActive().monitor!!, scale)}>Change</button
			>
		</div>
		<p class="label">Scale Of The Monitor. This can be a int or auto</p>
	</fieldset>

	<div class="divider my-3"></div>

	<fieldset class="fieldset">
		<legend class="fieldset-legend">Rotate</legend>
		<select
			class="select select-sm bg-base-300/60 border-base-content/10 my-2 h-fit w-full"
			onchange={(e) =>
				updateRotation(monitorState.getActive().monitor!!, Number(e.currentTarget.value))}
		>
			{#each rotate as rotation}
				<option
					value={rotation.rotate}
					selected={monitorState.getActive().monitor?.transform !== undefined
						? monitorState.getActive().monitor?.transform === rotation.rotate
						: false}>{rotation.text}</option
				>
			{/each}
		</select>
		<p class="label">Rotate Monitor.</p>
	</fieldset>

	<div class="divider my-3"></div>

	<div class="flex flex-col">
		<div class="flex flex-col gap-1">
			<p class="text-xs font-medium">Variable Refresh Rate (VRR)</p>
			<p class="text-base-content/60 text-xs">
				Enables dynamic refresh rate adjustment to reduce screen tearing and stuttering during fast
				motion. Best used with supported displays and GPUs.
			</p>
		</div>
		<div class="pt-3">
			<select
				class="select select-sm bg-base-300/60 border-base-content/10 my-2 h-fit w-full"
				onchange={(e) =>
					updateVrr(monitorState.getActive().monitor!!, Number(e.currentTarget.value))}
			>
				<option selected={vrr === 0} value="0">Variable Refresh Rate Disable</option>
				<option selected={vrr === 1} value="1">Variable Refresh Rate Enable</option>
				<option selected={vrr === 2} value="2"
					>Variable Refresh Rate Enable On Fullscreen Only</option
				>
				<option selected={vrr === 3} value="3"
					>Variable Refresh Rate Enable On Fullscreen When Playing Games Or Watching Videos Content
					Types.</option
				>
			</select>
		</div>
	</div>
</div>
