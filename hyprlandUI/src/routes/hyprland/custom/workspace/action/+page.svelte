<script lang="ts">
	import {
		GetIcons,
		WorkspaceRulesAddNew,
		WorkspaceRulesComponents,
		WorkspaceRulesDelete,
		workspaceState
	} from '$lib';

	$effect(() => {
		workspaceState.ui.addTo = 'new';
		workspaceState.ui.pageChange = true;
	});

	let name = $state('');
</script>

<WorkspaceRulesAddNew />

<div class="bg-base-200 max-h-screen min-h-screen w-full">
	<div class="flex flex-row items-center justify-between px-4 py-[7.5px]">
		<p class="text-xs font-semibold">Add New Workspace Rules</p>
		<div class="flex flex-row gap-4">
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
	<div class="flex flex-col gap-4 p-8">
		<div class="flex flex-col gap-1">
			<label for="workspace" class="label text-xs">Workspace Name</label>
			<textarea
				class="textarea w-full p-4 text-xs font-medium focus:ring-0 focus:outline-0"
				placeholder="ex: r[1-4] or 6 or w[f1]"
				bind:value={name}
			></textarea>
		</div>
		<div class="flex flex-row justify-end gap-4">
			<button class="btn btn-error btn-circle text-xs" onclick={() => workspaceState.setUiDelete(0)}
				>{@html GetIcons('delete', 16)}</button
			>
			<button
				class="btn btn-info btn-circle text-xs"
				onclick={() => workspaceState.setUiAddNew(true, 'new')}>{@html GetIcons('add', 16)}</button
			>
		</div>
		<div class="flex max-h-[65vh] flex-col gap-4 overflow-y-auto py-4">
			{#if workspaceState.ui.newUIData !== null}
				<WorkspaceRulesDelete name={''} data={workspaceState.ui.newUIData} index={0} />
				<WorkspaceRulesComponents {name} rule={workspaceState.ui.newUIData} />
			{:else}
				<div class="flex flex-col items-center justify-center gap-2">
					<div class="bg-warning text-warning-content w-fit rounded-full">
						{@html GetIcons('error', 64)}
					</div>
					<div>
						<p class="text-xs font-semibold">
							No Rules Added. Must Have Rules To Create Workspace Rules
						</p>
					</div>
				</div>
			{/if}
		</div>
		<div>
			{#if workspaceState.ui.newUIData !== null}
				<button
					class="btn btn-success w-full text-xs"
					onclick={() => {
						workspaceState.ui.action = true;
					}}>Create Workspace</button
				>
			{:else}
				<button class="btn btn-success w-full text-xs" disabled>Create Workspace</button>
			{/if}
		</div>
	</div>
</div>
