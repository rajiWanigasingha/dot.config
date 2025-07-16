<script lang="ts">
	import { keybindState } from '$lib';

	let signal = $state('');
</script>

<div class="p-4">
	<fieldset class="fieldset mt-3">
		<legend class="fieldset-legend">Add The signal</legend>
		<textarea
			name="text"
			class="textarea bg-base-200/60 border-base-content/10 h-24 w-full text-xs focus:outline-0"
			bind:value={signal}
			placeholder="print"
		></textarea>
		<p class="label">Add The Signal That Need To Send To Active Window</p>
	</fieldset>

	<button
		class="btn btn-success my-3 w-full text-xs"
		onclick={() => {
			keybindState.holdKeybinds.args = signal;
			keybindState.setSummery(true);
		}}>Create New Keybind</button
	>
</div>
