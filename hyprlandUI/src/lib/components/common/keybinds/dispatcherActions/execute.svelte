<script lang="ts">
	import { GetIcons, keybindState } from '$lib';
	import { open } from '@tauri-apps/plugin-dialog';

	let path = $state(null as null | boolean);

	let filePath = $state(null as null | string);
	let command = $state(null as null | string);

	function select(pathof: boolean) {
		path = pathof;
	}

	async function selectPath() {
		const file = await open({
			multiple: false,
			directory: false
		});

		filePath = `sh ${file}`;
	}

	function setFromPath() {
		keybindState.holdKeybinds.args = filePath;
		keybindState.setSummery(true);
	}

	function setFromCommand() {
		keybindState.holdKeybinds.args = command;
		keybindState.setSummery(true);
	}
</script>

<div class="p-4">
	<fieldset class="fieldset">
		<legend class="fieldset-legend">Chooes A Action</legend>
		<select
			class="select bg-base-200/60 border-base-content/10 w-full text-xs focus:outline-0"
			onchange={(e) => select(JSON.parse(e.currentTarget.value))}
		>
			<option disabled selected>Select One Of These Actions</option>
			<option value="false">Add A Script</option>
			<option value="true">Write A Command</option>
		</select>
		<p class="label">You can chooes to add a external script or create one</p>
	</fieldset>

	{#if path !== null}
		{#if path}
			<fieldset class="fieldset mt-3">
				<legend class="fieldset-legend">Write The Command</legend>
				<textarea
					name="text"
					class="textarea bg-base-200/60 border-base-content/10 h-68 w-full text-xs focus:outline-0"
					bind:value={command}
				></textarea>
				<p class="label">Write The Command That Needed To Be Executed</p>
			</fieldset>

			<button class="btn btn-success my-3 w-full text-xs" onclick={() => setFromCommand()}
				>Create New Keybind</button
			>
		{:else}
			<fieldset class="fieldset mt-3">
				<legend class="fieldset-legend">Select Script</legend>
				{#if filePath !== null}
					<div class="bg-base-300 my-3 flex w-full flex-col gap-2 rounded-md p-4">
						<p class="text-base-content/60 text-xs font-medium">Selected Path</p>
						<p class="text-xs font-semibold">{filePath}</p>
					</div>
					<button class="btn btn-success my-3 w-full text-xs" onclick={() => setFromPath()}
						>Create New Keybind</button
					>
				{:else}
					<button class="btn btn-neutral" onclick={() => selectPath()}>Open Files</button>
					<p class="label">Select The Script That Needed To Execute</p>
				{/if}
			</fieldset>
		{/if}
	{/if}
</div>
