<script lang="ts">
	import { keybindState } from '$lib';

	let args = $state('');
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

	<button
		class="btn btn-success my-3 w-full text-xs"
		onclick={() => {
			keybindState.holdKeybinds.args = args;
			keybindState.setSummery(true);
		}}>Create New Keybind</button
	>
</div>
