<script lang="ts">
	import { keybindConn, keybindState, type KeybindsLoad } from '$lib';

	let args = $state('');

	$effect(() => {
		if (keybindState.getEditKeyHold() !== null) {
			args = keybindState.getEditKeyHold()?.args ?? '';
		}
	});
</script>

<div class="p-4">
	<fieldset class="fieldset mt-3">
		<legend class="fieldset-legend">Add Args</legend>
		<textarea
			name="text"
			class="textarea bg-base-200/60 border-base-content/10 h-24 w-full text-xs focus:outline-0"
			bind:value={args}
			placeholder="Create The Args"
		></textarea>
		<p class="label">Add Argument for dispatchers</p>
	</fieldset>

	{#if keybindState.getEditKeyHold()}
		<button
			class="btn btn-warning my-3 w-full text-xs"
			onclick={() => {
				keybindState.editkeyHold!!.args = args;
				keybindState.setSummery(true);
			}}>Edit This Keybinds</button
		>
	{:else}
		<button
			class="btn btn-success my-3 w-full text-xs"
			onclick={() => {
				keybindState.holdKeybinds.args = args;
				keybindState.setSummery(true);
			}}>Create New Keybind</button
		>
	{/if}
</div>
