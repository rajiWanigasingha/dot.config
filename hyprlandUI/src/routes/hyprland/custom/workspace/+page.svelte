<script lang="ts">
	import { goto } from '$app/navigation';
	import {
		GetIcons,
		WorkspaceRules,
		WorkspaceRulesComponents,
		WorkspaceRulesDelete,
		workspaceState,
		WorkspaceRulesAddNew
	} from '$lib';
</script>

<WorkspaceRulesAddNew />

<div class="bg-base-200 max-h-screen min-h-screen w-full">
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<p class="text-xs font-semibold">Workspace Rules</p>
		<div class="flex flex-row gap-4">
			<button
				class="btn btn-circle btn-soft"
				onclick={() => goto('/hyprland/custom/workspace/action')}
			>
				{@html GetIcons('add')}
			</button>
			<div class="divider divider-horizontal m-0"></div>
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
	<div class="max-h-[94vh] overflow-y-auto p-8 px-16">
		{#if workspaceState.getWorkspaceRules().length !== 0}
			<div class="flex flex-col gap-4">
				{#each workspaceState.getWorkspaceRules() as data, index}
					<div class="flex flex-col gap-7">
						<div class="flex flex-row items-center justify-between">
							<div class="flex flex-col gap-1">
								<p class="text-sm font-semibold">{data.name}</p>
							</div>
							<div class="flex flex-row justify-end gap-3">
								<button
									class="btn btn-sm btn-circle btn-error"
									onclick={() => workspaceState.setUiDelete(index)}
								>
									{@html GetIcons('delete', 16)}
								</button>
								<button
									class="btn btn-sm btn-circle btn-success"
									onclick={() => {
										workspaceState.setUiAddNew(true, 'edit');
										workspaceState.ui.editName = data;
									}}
								>
									{@html GetIcons('add', 16)}
								</button>
							</div>
						</div>
						<div class="flex flex-col gap-3">
							<WorkspaceRulesComponents rule={data.rules} name={data.name} />
						</div>
						<WorkspaceRulesDelete {index} data={data.rules} name={data.name} />
					</div>
					<div class="divider m-0"></div>
				{/each}
			</div>
		{:else}
			<div class="flex flex-col items-center justify-center gap-4">
				<div class="bg-warning text-warning-content w-fit rounded-full">
					{@html GetIcons('error', 64)}
				</div>
				<div>
					<p class="text-xs font-semibold">No Workspace Rules Are Found. Try To Add New One</p>
				</div>
			</div>
		{/if}
	</div>
</div>
