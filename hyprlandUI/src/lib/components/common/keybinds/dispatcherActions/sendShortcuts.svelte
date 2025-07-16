<script lang="ts">
	import { keybindState } from '$lib';
	import { Command } from '@tauri-apps/plugin-shell';
	import { onMount } from 'svelte';

	let mod = $state('');
	let key = $state('');
	let window = $state('');
	let windows = $state('');
	let modError = $state('');

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
		'MOD5',
		''
	];

	function validate() {
		const modKeys = mod.split('+');

		let invalidMod = false;

		modKeys.forEach((item) => {
			const inMod = hyprlandModifierKeys.includes(item);
			if (!inMod) invalidMod = inMod;
		});

		if (invalidMod) {
			modError = 'Invalid Mod Key';
			return;
		}

		keybindState.holdKeybinds.args = `${mod} ,${key} ,${window}`;
		keybindState.setSummery(true);
	}

	onMount(async () => {
		let result = await Command.create('hyprctl-clients', ['clients']).execute();
		windows = result.stdout;
	});
</script>

<div class="max-h-[94vh] overflow-y-auto p-4">
	<fieldset class="fieldset mt-3">
		<legend class="fieldset-legend">Mod Keys</legend>
		<textarea
			name="text"
			class="textarea bg-base-200/60 border-base-content/10 h-12 w-full text-xs focus:outline-0"
			bind:value={mod}
			oninput={(e) => (mod = e.currentTarget.value.toUpperCase())}
			placeholder="SUPER+SHIFT"
		></textarea>
		<p class="label">
			Enter the mod key needed in window shortcut (CTRL, SHIFT, MOD2 ,MOD3 ,SUPER/WIN ,ALT ,MOD5)
		</p>
	</fieldset>

	<fieldset class="fieldset mt-3">
		<legend class="fieldset-legend">Keys</legend>
		<textarea
			name="text"
			class="textarea bg-base-200/60 border-base-content/10 h-12 w-full text-xs focus:outline-0"
			bind:value={key}
			oninput={(e) => (key = e.currentTarget.value.toUpperCase())}
			placeholder="k"
		></textarea>
		<p class="label">
			Enter the key needed in window shortcut (Ex: Ctrl+k enter k in here ctrl in mod key)
		</p>
	</fieldset>

	<fieldset class="fieldset mt-3">
		<legend class="fieldset-legend">Add The Window</legend>
		<textarea
			name="text"
			class="textarea bg-base-200/60 border-base-content/10 h-24 w-full text-xs focus:outline-0"
			bind:value={window}
			placeholder="pid:423"
		></textarea>
		<p class="label">Write A Regex That Will Match The Window</p>
	</fieldset>

	{#if modError !== ''}
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
			<span class="text-xs font-medium">{modError}</span>
			<div>
				<button class="btn btn-sm">Close</button>
			</div>
		</div>
	{/if}

	<button
		class="btn btn-success my-3 w-full text-xs"
		onclick={() => {
			validate();
		}}>Create New Keybind</button
	>

	<pre
		class="bg-base-100 mt-4 max-h-[600px] overflow-y-auto rounded-md px-2 text-sm whitespace-pre">
        {windows}
    </pre>
</div>
