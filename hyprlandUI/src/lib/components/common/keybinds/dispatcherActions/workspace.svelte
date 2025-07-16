<script lang="ts">
	import { keybindState } from '$lib';

	let workspace = $state('');
</script>

<div class="p-4">
	<fieldset class="fieldset mt-3">
		<legend class="fieldset-legend">Add The workspace</legend>
		<textarea
			name="text"
			class="textarea bg-base-200/60 border-base-content/10 h-24 w-full text-xs focus:outline-0"
			bind:value={workspace}
			placeholder="0"
		></textarea>
		<p class="label">Enter The Workspace That Need To Change Into</p>
	</fieldset>

	<button
		class="btn btn-success my-3 w-full text-xs"
		onclick={() => {
			keybindState.holdKeybinds.args = workspace;
			keybindState.setSummery(true);
		}}>Create New Keybind</button
	>
</div>
