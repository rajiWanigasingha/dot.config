<script lang="ts">
	import { keybindState } from '$lib';
	import { Command } from '@tauri-apps/plugin-shell';
	import { onMount } from 'svelte';

	let window = $state('');
	let windows = $state('');

	onMount(async () => {
		let result = await Command.create('hyprctl-clients', ['clients']).execute();
		windows = result.stdout;
	});
</script>

<div class="p-4">
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

	<button
		class="btn btn-success my-3 w-full text-xs"
		onclick={() => {
			keybindState.holdKeybinds.args = window;
			keybindState.setSummery(true);
		}}>Create New Keybind</button
	>

	<pre
		class="bg-base-100 mt-4 max-h-[600px] overflow-y-auto rounded-md px-2 text-sm whitespace-pre">
    {windows}
    </pre>
</div>
