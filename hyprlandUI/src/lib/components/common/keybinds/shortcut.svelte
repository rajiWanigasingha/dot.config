<script lang="ts">
	import { keybindConn, keybindState } from '$lib';

	const flags = ['l', 'r', 'c', 'g', 'o', 'e', 'n', 'm', 't', 'i', 's', 'd', 'p'];

	const hyprlandModifierKeys = [
		'SUPER',
		'WIN',
		'LOGO',
		'CONTROL',
		'CTRL',
		'ALT',
		'SHIFT',
		'CAPS',
		'MOD2',
		'MOD3',
		'MOD4',
		'MOD5'
	];

	let flagsNeed = $state([] as string[]);

	function filterKeys() {
		const keys = shortcut?.split('+').map((items) => items.trim());

		mod = [];
		key = [];

		keys?.forEach((item) => {
			if (item.split('&').length !== 1) {
				const splitKeys = item.split('&');

				const modVal = [] as string[];

				const keyVal = [] as string[];

				splitKeys.forEach((item) => {
					if (hyprlandModifierKeys.find((key) => key.toUpperCase() === item.trim().toUpperCase())) {
						modVal.push(item.trim().toUpperCase());
					} else {
						keyVal.push(item.trim().toUpperCase());
					}
				});

				flagsNeed.push('s');
				mod.push(modVal.join('&'));
				key.push(keyVal.join('&'));
			} else {
				if (hyprlandModifierKeys.find((key) => key.toUpperCase() === item.trim().toUpperCase())) {
					mod.push(item.trim().toUpperCase());
				} else {
					key.push(item.trim().toUpperCase());
				}
			}
		});

		mod = mod.filter((items) => items !== '');
		key = key.filter((items) => items !== '');
	}

	let shortcut = $state(null as null | string);

	let flag = $state([] as string[]);

	let mod = $state([] as string[]);

	let key = $state([] as string[]);

	let showDispatchers = $state(false);
</script>

<div class="flex flex-row items-center justify-between px-4 py-[13.5px]">
	<p class="text-xs font-semibold">Create Keybind</p>
</div>

<div class="divider m-0"></div>

<div class="p-4">
	<div>
		<fieldset class="fieldset py-2">
			<legend class="fieldset-legend">Shortcut Keys</legend>
			<textarea
				class="textarea bg-base-200/60 border-base-content/10 h-4 w-full resize-none text-xs focus:outline-0"
				placeholder="SUPER + SHIFT + T"
				bind:value={shortcut}
				oninput={(e) => (shortcut = e.currentTarget.value.toUpperCase())}
			></textarea>
			<div class="label">Create A Keybind With + In Between</div>
		</fieldset>

		<button
			class="btn btn-sm btn-soft btn-neutral mt-2 w-full"
			onclick={() => {
				filterKeys();
				const setKeys = keybindState.setHoldKeybinds(flag, mod, key);
				showDispatchers = setKeys;
			}}>Confirm Keys</button
		>
	</div>

	<div class="divider my-2"></div>

	<fieldset class="fieldset py-2">
		<legend class="fieldset-legend">Add Flags</legend>
		<select
			class="select bg-base-200/60 border-base-content/10 w-full text-xs focus:outline-0"
			oninput={(e) => flag.push(e.currentTarget.value)}
		>
			<option disabled selected>Pick a flag</option>
			{#each flags as flag}
				<option value={flag}>{flag}</option>
			{/each}
		</select>
		<div class="label">Add flags to super charge keybinds</div>

		{#if flag.length !== 0}
			<div class="flex flex-row gap-1 pt-2">
				{#each flag as f, index}
					<button class="kbd kbd-sm px-4" onclick={() => flag.splice(index, 1)}>{f}</button>
				{/each}
				<p class="text-base-content/60 py-1 text-xs font-medium">Click to remove flag</p>
			</div>
		{/if}
	</fieldset>

	<div class="divider m-0"></div>

	<div class="flex flex-col gap-2 py-2">
		<p class="text-xs font-medium">Summery Of Shortcut Keys</p>
		<div class="bg-base-300 flex w-full flex-row items-center justify-between p-4">
			<p class="text-xs font-medium">Mod Key</p>
			<div class="flex flex-row gap-2">
				{#if mod.length === 0}
					<p class="badge text-xs">No Mod Keys</p>
				{:else}
					{#each mod as m}
						<kbd class="kbd">{m}</kbd>
					{/each}
				{/if}
			</div>
		</div>
		<div class="bg-base-300 flex w-full flex-row items-center justify-between p-4">
			<p class="text-xs font-medium">Keys</p>
			<div class="flex flex-row gap-2">
				{#if key.length === 0}
					<p class="badge text-xs">No Keys</p>
				{:else}
					{#each key as k}
						<kbd class="kbd">{k}</kbd>
					{/each}
				{/if}
			</div>
		</div>
		<div class="bg-base-300 flex w-full flex-row items-center justify-between p-4">
			<p class="text-xs font-medium">Flags</p>
			<div class="flex flex-row gap-2">
				{#if flag.length === 0}
					<p class="badge text-xs">No Flags</p>
				{:else}
					{#each flag as f}
						<p class="badge text-xs">{f}</p>
					{/each}
				{/if}
			</div>
		</div>
	</div>

	<div class="divider my-2"></div>

	<div class="py-2">
		{#each flagsNeed as needed}
			<div role="alert" class="alert alert-vertical alert-warning sm:alert-horizontal">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					fill="none"
					viewBox="0 0 24 24"
					class="stroke-warning-content h-6 w-6 shrink-0"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
					></path>
				</svg>
				<div>
					<h3 class="font-bold">Flag <b>{needed}</b></h3>
					<div class="text-xs">This Flag Needed For This Keybind To Work</div>
				</div>
				<button
					class="btn btn-sm"
					onclick={() => {
						flag.push(needed);
						flagsNeed = flagsNeed.filter((item) => item !== needed);
					}}>Add This Flag</button
				>
			</div>
		{/each}
	</div>

	<div class="flex flex-col gap-2 py-2">
		{#if showDispatchers}
			<button
				class="btn btn-success w-full text-xs"
				onclick={() => {
					keybindConn.getDispatchers();
				}}>Add Dispatcher</button
			>
		{:else}
			<button class="btn btn-success w-full text-xs" disabled>Add Dispatcher</button>
		{/if}
		<p class="text-base-content/60 text-xs font-medium">
			Need To Have A Shortcut Key To Add Dispatcher Actions
		</p>
	</div>
</div>
