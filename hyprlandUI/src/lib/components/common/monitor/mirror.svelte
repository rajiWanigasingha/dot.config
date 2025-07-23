<script lang="ts">
	import { GetIcons, monitorConn, monitorState, type MonitorData } from '$lib';
	import { toast } from 'svelte-sonner';

	let active = $state(0);
	let show = $state(false);
	let mirror = $state([] as MonitorData[]);

	function createMirror(name: string) {
		const monitor = {
			...mirror[0],
			mirror: name
		};

		monitorConn.changeMirror(monitor);

		active = 0;
	}

	function removeMirror(monitor: MonitorData) {
		const removeMirrorMonitor = {
			...monitor,
			mirror: null
		};

		monitorConn.changeMirror(removeMirrorMonitor);
	}

	$effect(() => {
		let updated = false;

		monitorState.store.availableMonitors.forEach((item) => {
			if (item.monitor?.mirror !== null) {
				show = true;
				updated = true;
			}
		});

		if (!updated) {
			show = false;
		}
	});
</script>

<div class="flex w-full flex-col gap-3 p-4">
	<div class="flex flex-row justify-between">
		<p class="text-sm font-medium">Mirror Monitor</p>
		<button class="btn btn-sm btn-circle" onclick={() => (active = 1)}
			>{@html GetIcons('add', 16)}</button
		>
	</div>

	<div class="divider m-0"></div>

	{#if active === 0}
		<div class="p-8">
			{#if show}
				{#each monitorState.store.availableMonitors as monitor}
					<div class="flex flex-col gap-4 py-2">
						{#if monitor.monitor?.mirror !== null}
							<div class="mockup-code bg-base-100 flex w-full flex-col gap-1 text-xs">
								<pre data-prefix="$"><code class="text-base-content/60">Mirror Monitor</code></pre>
								<pre></pre>
								<pre data-prefix=">"><code class="text-info-content">&#123</code></pre>

								<pre data-prefix=">"><code
										><span class="text-blue-400">  "mirror_from"</span>: <span
											class="text-green-400">"{monitor.monitor?.name}"</span
										>,</code
									></pre>
								<pre data-prefix=">"><code
										><span class="text-blue-400">  "mirror_to"</span>: <span class="text-green-400"
											>"{monitor.monitor?.mirror}"</span
										>,</code
									></pre>
								<pre data-prefix=">"><code class="text-info-content">&#125</code></pre>
								<pre></pre>
								<pre><code class="text-info-content flex items-center justify-end">
									<button class="btn btn-error text-xs" onclick={() => removeMirror(monitor.monitor!!)}
											>Remove This Mirror</button
										>
								</code></pre>
							</div>
						{/if}
					</div>
				{/each}
			{:else}
				<div class="flex justify-center">
					<p class="text-sm font-semibold">No Mirror Monitor Is Available Add One.</p>
				</div>
			{/if}
		</div>
	{:else}
		<div class="flex flex-row gap-4 p-8">
			<div class="flex w-1/2 flex-col gap-2">
				<p class="text-xs">Mirror From</p>
				{#each monitorState.store.availableMonitors as monitor}
					<button
						onclick={() => {
							if (mirror.length > 0) {
								mirror[0] = monitor.monitor!!;
							} else {
								mirror.push(monitor.monitor!!);
							}
						}}
						class="hover:border-base-content cursor-pointer border text-start {mirror[0] !==
							undefined && mirror[0].name === monitor.monitor?.name
							? 'border-info'
							: 'border-base-content/10'}"
					>
						<div class="bg-base-100 rounded-md p-4">
							<p class="text-xs font-bold">{monitor.monitor?.name}</p>
							<p class="text-xs font-light">{monitor.monitorInfo?.description}</p>
						</div>
					</button>
				{/each}
			</div>
			<div class="divider divider-horizontal m-0"></div>
			<div class="flex w-1/2 flex-col gap-2">
				<p class="text-xs">Mirror To</p>
				{#each monitorState.store.availableMonitors as monitor}
					{#if mirror[0] === undefined}
						<button
							class="border-base-content/10 hover:border-base-content cursor-pointer border text-start"
						>
							<div class="bg-base-100 rounded-md p-4">
								<p class="text-xs font-bold">{monitor.monitor?.name}</p>
								<p class="text-xs font-light">{monitor.monitorInfo?.description}</p>
							</div>
						</button>
					{:else if mirror[0].name !== monitor.monitor?.name}
						<button
							class="border-base-content/10 hover:border-base-content cursor-pointer border text-start"
							onclick={() => {
								if (monitor.monitor != null) {
									createMirror(monitor.monitor.name);
								} else {
									toast.warning("Mirror Monitor Doesn't Have A Name");
								}
							}}
						>
							<div class="bg-base-100 rounded-md p-4">
								<p class="text-xs font-bold">{monitor.monitor?.name}</p>
								<p class="text-xs font-light">{monitor.monitorInfo?.description}</p>
							</div>
						</button>
					{/if}
				{/each}
			</div>
		</div>
	{/if}
</div>
