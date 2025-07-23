<script lang="ts">
	import {
		keybindState,
		MonitorAddReservedArea,
		MonitorColor,
		monitorConn,
		MonitorDisable,
		MonitorGeneral,
		MonitorMirror,
		monitorState,
		type MonitorInfo
	} from '$lib';
	import { Command } from '@tauri-apps/plugin-shell';
	import { onDestroy, onMount } from 'svelte';

	onMount(async () => {
		let result = await Command.create('hyprctl-monitor', ['monitors all', '-j']).execute();
		if (result.stderr === '') {
			const monitorInfo = JSON.parse(result.stdout) as MonitorInfo[];
			monitorState.setMonitorInfo(monitorInfo);
		}

		if (monitorConn.wsMonitor === null) {
			monitorConn.connect();
		}
	});

	onDestroy(() => {
		monitorConn.disconnect();
	});

	let tabState = $state(0);

	const tabNames = [
		'General Settings',
		'Mirror Monitors',
		'Display Color Settings',
		'Disable And Enable Monitor',
		'Add Reserved Areas'
	];
</script>

<div class="bg-base-200 max-h-screen min-h-screen w-full overflow-y-auto">
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<p class="text-xs font-semibold">Display And Monitor</p>
		<div class="flex flex-row gap-3">
			<div role="tablist" class="tabs tabs-border">
				{#each tabNames as tab, index}
					<button
						onclick={() => (tabState = index)}
						role="tab"
						class="tab {index === tabState ? 'tab-active' : ''} text-xs">{tab}</button
					>
				{/each}
			</div>
			<div>
				<!-- svelte-ignore a11y_consider_explicit_label -->
				<button class="btn btn-sm btn-circle btn-soft">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
						><path fill="currentColor" d="M6 13v-2h12v2z" /></svg
					>
				</button>
				<!-- svelte-ignore a11y_consider_explicit_label -->
				<button class="btn btn-sm btn-circle btn-soft">
					<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
						><path
							fill="currentColor"
							d="M20 15v-5q0-1.25-.875-2.125T17 7H6V4q0-.825.588-1.412T8 2h12q.825 0 1.413.588T22 4v9q0 .825-.587 1.413T20 15M4 22q-.825 0-1.412-.587T2 20v-9q0-.825.588-1.412T4 9h12q.825 0 1.413.588T18 11v9q0 .825-.587 1.413T16 22z"
						/></svg
					>
				</button>
				<!-- svelte-ignore a11y_consider_explicit_label -->
				<button class="btn btn-sm btn-circle btn-soft">
					<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
						><path
							fill="currentColor"
							d="m8.382 17.025l-1.407-1.4L10.593 12L6.975 8.4L8.382 7L12 10.615L15.593 7L17 8.4L13.382 12L17 15.625l-1.407 1.4L12 13.41z"
						/></svg
					>
				</button>
			</div>
		</div>
	</div>
	<div class="divider m-0"></div>
	<div class="max-h-[94vh] min-h-[94vh] w-full overflow-y-auto p-4">
		{#if tabState === 0}
			{#if monitorState.getMonitorInfo().length >= 2}
				<div class="flex flex-row justify-center px-4 py-1">
					<div class="join">
						{#each monitorState.getMonitorInfo() as monitor, index}
							<button
								class="join-item btn bg-base-100 text-base-content w-[300px] text-xs {monitorState
									.store.availableIndex === index
									? 'bg-base-300 font-bold'
									: 'font-light'}"
								onclick={() => monitorState.setActive(index)}>{monitor.name}</button
							>
						{/each}
					</div>
				</div>
			{/if}
			<MonitorGeneral />
		{:else if tabState === 1}
			<MonitorMirror />
		{:else if tabState === 2}
			{#if monitorState.getMonitorInfo().length >= 2}
				<div class="flex flex-row justify-center px-4 py-1">
					<div class="join">
						{#each monitorState.getMonitorInfo() as monitor, index}
							<button
								class="join-item btn bg-base-100 text-base-content w-[300px] text-xs {monitorState
									.store.availableIndex === index
									? 'bg-base-300 font-bold'
									: 'font-light'}"
								onclick={() => monitorState.setActive(index)}>{monitor.name}</button
							>
						{/each}
					</div>
				</div>
			{/if}
			<MonitorColor />
		{:else if tabState === 3}
			<MonitorDisable />
		{:else}
			{#if monitorState.getMonitorInfo().length >= 2}
				<div class="flex flex-row justify-center px-4 py-1">
					<div class="join">
						{#each monitorState.getMonitorInfo() as monitor, index}
							<button
								class="join-item btn bg-base-100 text-base-content w-[300px] text-xs {monitorState
									.store.availableIndex === index
									? 'bg-base-300 font-bold'
									: 'font-light'}"
								onclick={() => monitorState.setActive(index)}>{monitor.name}</button
							>
						{/each}
					</div>
				</div>
			{/if}
			<MonitorAddReservedArea />
		{/if}
	</div>
</div>
