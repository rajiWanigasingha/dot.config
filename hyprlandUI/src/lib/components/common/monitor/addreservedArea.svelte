<script lang="ts">
	import { GetIcons, monitorConn, monitorState, type MonitorData } from '$lib';

	let action = $state('show' as 'show' | 'edit' | 'add');
	let top = $state(null as null | number);
	let bottom = $state(null as null | number);
	let left = $state(null as null | number);
	let right = $state(null as null | number);

	function addNewReserved() {
		const active = monitorState.getActive().monitor!!;

		const monitor: MonitorData = {
			name: active.name,
			disable: active.disable,
			addreserved: [Number(top) ?? 0, Number(bottom) ?? 0, Number(left) ?? 0, Number(right) ?? 0],
			resolution: active.resolution,
			position: active.position,
			scale: active.scale,
			mirror: active.mirror,
			bitDepth: active.bitDepth,
			transform: active.transform,
			cm: active.cm,
			sdrsaturation: active.sdrsaturation,
			sdrbrightness: active.sdrbrightness,
			vrr: active.vrr
		};

		monitorConn.addReserved(monitor, true);

		action = 'show';
	}

	function editReserved() {
		const active = monitorState.getActive().monitor!!;

		const monitor: MonitorData = {
			name: active.name,
			disable: active.disable,
			addreserved: [Number(top) ?? 0, Number(bottom) ?? 0, Number(left) ?? 0, Number(right) ?? 0],
			resolution: active.resolution,
			position: active.position,
			scale: active.scale,
			mirror: active.mirror,
			bitDepth: active.bitDepth,
			transform: active.transform,
			cm: active.cm,
			sdrsaturation: active.sdrsaturation,
			sdrbrightness: active.sdrbrightness,
			vrr: active.vrr
		};

		monitorConn.addReserved(monitor, true);

		action = 'show';
	}

	function deleteReserved() {
		const active = monitorState.getActive().monitor!!;

		const monitor: MonitorData = {
			name: active.name,
			disable: active.disable,
			addreserved: [Number(top) ?? 0, Number(bottom) ?? 0, Number(left) ?? 0, Number(right) ?? 0],
			resolution: active.resolution,
			position: active.position,
			scale: active.scale,
			mirror: active.mirror,
			bitDepth: active.bitDepth,
			transform: active.transform,
			cm: active.cm,
			sdrsaturation: active.sdrsaturation,
			sdrbrightness: active.sdrbrightness,
			vrr: active.vrr
		};

		monitorConn.addReserved(monitor, false);

		action = 'show';
	}

	$effect(() => {
		if (action === 'edit') {
			top = monitorState.getActive().monitor?.addreserved!![0] ?? 0;
			bottom = monitorState.getActive().monitor?.addreserved!![1] ?? 1;
			left = monitorState.getActive().monitor?.addreserved!![2] ?? 2;
			right = monitorState.getActive().monitor?.addreserved!![3] ?? 3;
		}
	});

	$inspect(monitorState.getActive())
</script>

<div class="p-8">
	<div class="flex flex-row justify-between">
		<p class="text-sm font-medium">All Reserved Areas</p>
		<button class="btn btn-sm btn-circle" onclick={() => (action = 'add')}
			>{@html GetIcons('add', 16)}</button
		>
	</div>

	<div class="divider m-0"></div>

	{#if action === 'show'}
		{#if monitorState.getActive().monitor !== undefined && monitorState.getActive().monitor?.addreserved !== null}
			<div class="my-3 flex flex-col gap-3">
				<div class="mockup-code bg-base-100 flex w-full flex-col gap-1 text-xs">
					<pre data-prefix="$"><code class="text-base-content/60">Reserved Area of edp-1</code
						></pre>
					<pre></pre>
					<pre data-prefix=">"><code class="text-info-content">&#123</code></pre>
					<pre data-prefix=">"><code
							><span class="text-sky-400">  top</span> : <span class="text-amber-300"
								>{monitorState.getActive().monitor!!.addreserved!![0]}</span
							></code
						></pre>
					<pre data-prefix=">"><code
							><span class="text-sky-400">  bottom</span> : <span class="text-amber-300"
								>{monitorState.getActive().monitor!!.addreserved!![1]}</span
							></code
						></pre>
					<pre data-prefix=">"><code
							><span class="text-sky-400">  left</span> : <span class="text-amber-300"
								>{monitorState.getActive().monitor!!.addreserved!![2]}</span
							></code
						></pre>
					<pre data-prefix=">"><code
							><span class="text-sky-400">  right</span> : <span class="text-amber-300"
								>{monitorState.getActive().monitor!!.addreserved!![3]}</span
							></code
						></pre>
					<pre data-prefix=">"><code class="text-info-content">&#125</code></pre>
				</div>
				<div class="flex flex-row justify-end gap-4">
					<button class="btn btn-warning text-xs" onclick={() => (action = 'edit')}
						>{@html GetIcons('edit', 16)}</button
					>
					<button class="btn btn-error text-xs" onclick={() => deleteReserved()}
						>{@html GetIcons('delete', 16)}</button
					>
				</div>
			</div>
		{:else}
			<div class="flex flex-col gap-4 py-2">
				<p class="text-xs font-semibold">
					No Reserved Areas In Your Settings. There might be present outside from these settings
				</p>
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
								><span class="text-blue-400">  "Reserved"</span>: <span class="text-green-400"
									>"{monitorState.getActive().monitorInfo?.reserved}"</span
								>,</code
							></pre>
						<pre data-prefix=">"><code class="text-info-content">&#125</code></pre>
					</div>
				{/if}
			</div>
		{/if}
	{:else if action === 'add'}
		<div class="flex flex-col">
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Top</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={top}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (top) {
									top = top - 1;
								} else {
									top = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (top) {
									top = top + 1;
								} else {
									top = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Bottom</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={bottom}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (bottom) {
									bottom = bottom - 1;
								} else {
									bottom = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (bottom) {
									bottom = bottom + 1;
								} else {
									bottom = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Left</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={left}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (left) {
									left = left - 1;
								} else {
									left = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (left) {
									left = left + 1;
								} else {
									left = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Right</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={right}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (right) {
									right = right - 1;
								} else {
									right = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (right) {
									right = right + 1;
								} else {
									right = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="mt-4 flex flex-row justify-end gap-3">
				<button
					class="btn btn-accent w-1/5 text-xs"
					onclick={() => {
						action = 'show';
						top = bottom = left = right = null;
					}}>Close</button
				>
				<button class="btn btn-success w-1/5 text-xs" onclick={() => addNewReserved()}
					>Add New</button
				>
			</div>
		</div>
	{:else if action === 'edit'}
		<div class="flex flex-col">
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Top</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={top}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (top) {
									top = top - 1;
								} else {
									top = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (top) {
									top = top + 1;
								} else {
									top = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Bottom</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={bottom}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (bottom) {
									bottom = bottom - 1;
								} else {
									bottom = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (bottom) {
									bottom = bottom + 1;
								} else {
									bottom = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Left</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={left}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (left) {
									left = left - 1;
								} else {
									left = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (left) {
									left = left + 1;
								} else {
									left = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="flex flex-col justify-between">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">Right</p>
				</div>
				<div class="pt-3">
					<input
						type="text"
						class="input join-item bg-base-300/60 border-base-content/10 h-fit w-full p-3 text-xs focus:ring-0"
						placeholder="1.2"
						bind:value={right}
					/>
					<div class="join-item flex flex-row pt-3">
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (right) {
									right = right - 1;
								} else {
									right = 1;
								}
							}}
						>
							{@html GetIcons('minus')}
						</button>
						<button
							class="btn btn-primary join-item w-1/2"
							onclick={() => {
								if (right) {
									right = right + 1;
								} else {
									right = 1;
								}
							}}
						>
							{@html GetIcons('add')}
						</button>
					</div>
				</div>
			</div>
			<div class="mt-4 flex flex-row justify-end gap-3">
				<button
					class="btn btn-accent w-1/5 text-xs"
					onclick={() => {
						action = 'show';
						top = bottom = left = right = null;
					}}>Close</button
				>
				<button class="btn btn-warning w-1/5 text-xs" onclick={() => editReserved()}>Edit</button>
			</div>
		</div>
	{/if}
</div>
