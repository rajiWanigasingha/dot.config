<script lang="ts">
	import { autoStartConn, autostartState, GetIcons } from '$lib';
	import { tick } from 'svelte';
	import { toast } from 'svelte-sonner';
	import { slide } from 'svelte/transition';

	let createAutoStart = $state(false);
	let updateAutoStart = $state(false);

	let keyword = $state('exec-once');
	let command = $state('');
	let oldCommand = $state('');

	function createNewExecute() {
		if (keyword === '' || command === '') {
			return;
		}

		autoStartConn.addExecute(keyword, command);

		keyword = 'exec-once';
		command = '';
		oldCommand = '';

		createAutoStart = false;
	}

	function updateExecute() {
		if (keyword === '' || command === '') {
			return;
		}

		autoStartConn.updateExecute(keyword, command, oldCommand);

		keyword = 'exec-once';
		command = '';
		oldCommand = '';

		updateAutoStart = false;
	}

	function openEdit(keywordExe: string, commandExe: string) {
		createAutoStart = false;
		updateAutoStart = true;

		command = oldCommand = commandExe;

		keyword = keywordExe;

		tick().then(() => {
			const div = document.querySelector('#editAutoStartDiv') as HTMLDivElement;
			if (div) {
				div.scrollIntoView({ behavior: 'smooth' });
			}
		});
	}
</script>

<div class="bg-base-200 max-h-screen min-h-screen w-full overflow-y-auto">
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<div role="tablist" class="tabs tabs-border">
			<!-- svelte-ignore a11y_missing_attribute -->
			<button
				onclick={(e) => {
					e.preventDefault();
					autostartState.setExecTab('exec-once');
				}}
				class="tab text-xs {autostartState.autoState.activeTab === 'exec-once' ? 'tab-active' : ''}"
				>Execute Once</button
			>
			<!-- svelte-ignore a11y_missing_attribute -->
			<button
				onclick={(e) => {
					e.preventDefault();
					autostartState.setExecTab('exec');
				}}
				class="tab text-xs {autostartState.autoState.activeTab === 'exec' ? 'tab-active' : ''}"
				>Execute</button
			>
			<!-- svelte-ignore a11y_missing_attribute -->
			<button
				onclick={(e) => {
					e.preventDefault();
					autostartState.setExecTab('exec-shutdown');
				}}
				class="tab text-xs {autostartState.autoState.activeTab === 'exec-shutdown'
					? 'tab-active'
					: ''}">Execute Shutdown</button
			>
		</div>
		<div class="flex flex-row gap-3">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button
				class="btn btn-circle btn-soft"
				onclick={() => {
					createAutoStart = true;
				}}
			>
				{@html GetIcons('add')}
			</button>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle btn-soft" onclick={() => {}}>
				{@html GetIcons('search')}
			</button>
		</div>
	</div>
	<div class="divider m-0"></div>

	{#if createAutoStart}
		<div
			in:slide={{ duration: 300 }}
			out:slide={{ duration: 300 }}
			class="w-full px-8 py-4"
			id="AddAutoStartDiv"
		>
			<fieldset class="fieldset bg-base-300 border-base-300 rounded-box w-full border p-4">
				<legend class="fieldset-legend">Add New Execute</legend>
				<label class="select w-full">
					<span class="text-base-content/60 w-[80px] text-xs">Keyword</span>
					<select class="text-xs focus:ring-0" onchange={(e) => (keyword = e.currentTarget.value)}>
						<option class="text-xs" value="exec-once" selected>Execute Once</option>
						<option class="text-xs" value="exec">Execute</option>
						<option class="text-xs" value="exec-shutdown">Execute Shutdown</option>
					</select>
				</label>
				<label class="input w-full">
					<span class="text-base-content/60 w-[70px] text-xs">Command</span>
					<input
						type="text"
						class="text-xs focus:ring-0"
						placeholder="Enter Your Command"
						oninput={(e) => (command = e.currentTarget.value)}
					/>
				</label>
				<button class="btn btn-success my-3 w-full text-xs" onclick={() => createNewExecute()}
					>Create New Command</button
				>
				<button
					class="btn"
					onclick={() => {
						keyword = 'exec-once';
						command = '';
						oldCommand = '';

						createAutoStart = false;
					}}
				>
					Close
				</button>
			</fieldset>
		</div>
	{/if}

	{#if updateAutoStart}
		<div
			in:slide={{ duration: 300 }}
			out:slide={{ duration: 300 }}
			class="w-full px-8 py-4"
			id="editAutoStartDiv"
		>
			<fieldset class="fieldset bg-base-300 border-base-300 rounded-box w-full border p-4">
				<legend class="fieldset-legend">Edit Execute</legend>
				<label class="select w-full">
					<span class="text-base-content/60 w-[80px] text-xs">Keyword</span>
					<select
						class="text-xs focus:ring-0"
						onchange={(e) => (keyword = e.currentTarget.value)}
						bind:value={keyword}
					>
						<option class="text-xs" value="exec-once" selected>Execute Once</option>
						<option class="text-xs" value="exec">Execute</option>
						<option class="text-xs" value="exec-shutdown">Execute Shutdown</option>
					</select>
				</label>
				<label class="input w-full">
					<span class="text-base-content/60 w-[70px] text-xs">Command</span>
					<input
						type="text"
						class="text-xs focus:ring-0"
						placeholder="Enter Your Command"
						oninput={(e) => (command = e.currentTarget.value)}
						bind:value={command}
					/>
				</label>
				<button class="btn btn-success my-3 w-full text-xs" onclick={() => updateExecute()}
					>Update Command</button
				>
				<button
					class="btn"
					onclick={() => {
						keyword = 'exec-once';
						command = '';
						oldCommand = '';

						updateAutoStart = false;
					}}
				>
					Close
				</button>
			</fieldset>
		</div>
	{/if}

	<div class="flex max-h-[94vh] min-h-[94vh] w-full flex-col gap-3 p-8">
		{#each autostartState.getAutoStart() as exec}
			<div
				class="bg-base-300/30 border-base-100/50 flex w-full flex-row justify-between border p-4"
			>
				<p class="text-xs font-semibold">{exec.command}</p>

				<div class="flex flex-row justify-end gap-3">
					<button
						class="btn btn-xs btn-warning btn-circle"
						onclick={() => openEdit(exec.keyword, exec.command)}
					>
						{@html GetIcons('edit', 16)}
					</button>
					<button
						class="btn btn-xs btn-error btn-circle"
						onclick={() => {
							toast.warning('Delete This AutoStart', {
								action: {
									label: 'Delete',
									onClick: () => autoStartConn.deleteExecute(exec.keyword, exec.command)
								}
							});
						}}
					>
						{@html GetIcons('delete', 16)}
					</button>
				</div>
			</div>
		{/each}
	</div>
</div>
