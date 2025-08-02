<script lang="ts">
	import { GetIcons, workspaceConn, workspaceState, type WorkspaceData } from '$lib';

	let { index, data, name }: { index: number; data: WorkspaceData; name: string } = $props();
</script>

<dialog id="my_modal_2" class="modal" open={workspaceState.ui.delete === index ? true : false}>
	<div class="modal-box bg-base-300 min-w-5xl">
		<div class="flex flex-row items-center justify-between">
			<h3 class="text-xs font-semibold">Delete Workspace Rules</h3>
			<button
				class="btn btn-error btn-sm rounded-md text-xs"
				onclick={() => workspaceConn.delete({ name: name, rules: data }, 'ALL')}>Delete All</button
			>
		</div>
		<div class="divider m-2"></div>
		<div class="flex flex-col gap-2">
			{#if data.monitor != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Monitor</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							Workspace That Bound To This Monitor Is: {data.monitor}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, monitor: undefined } },
									'SINGLE'
								);
							} else {
								delete data.monitor;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}

			{#if data.default != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Default</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							Whether this workspace should be the default workspace for the given monitor: {data.default}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, default: undefined } },
									'SINGLE'
								);
							} else {
								delete data.default;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}

			{#if data.gapsIn != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Gaps In</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							Value Of Gaps In In This Workspace: {data.gapsIn}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, gapsIn: undefined } },
									'SINGLE'
								);
							} else {
								delete data.gapsIn;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.gapsOut != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Gaps Out</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							gaps between windows and monitor edges are: {data.gapsOut}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, gapsOut: undefined } },
									'SINGLE'
								);
							} else {
								delete data.gapsOut;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.borderSize != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Border Size</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							the border size around windows is: {data.borderSize}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, borderSize: undefined } },
									'SINGLE'
								);
							} else {
								delete data.borderSize;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.border != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Border</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							If there is a border: {data.border}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, border: undefined } },
									'SINGLE'
								);
							} else {
								delete data.border;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.shadow != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Shadow</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							If there is a shadow: {data.shadow}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, shadow: undefined } },
									'SINGLE'
								);
							} else {
								delete data.shadow;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.rounding != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Rouding</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							If there is a rounding in windows: {data.rounding}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, rounding: undefined } },
									'SINGLE'
								);
							} else {
								delete data.rounding;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.decorate != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Decoration</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							If there is a decoration in windows: {data.decorate}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, decorate: undefined } },
									'SINGLE'
								);
							} else {
								delete data.decorate;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.persistent != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Persistent</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							Keep this workspace alive even if empty and inactive: {data.persistent}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, persistent: undefined } },
									'SINGLE'
								);
							} else {
								delete data.persistent;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.onCreatedEmpty != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">On Create Empty</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							A command to be executed once a workspace is created empty is: {data.onCreatedEmpty}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, onCreatedEmpty: undefined } },
									'SINGLE'
								);
							} else {
								delete data.onCreatedEmpty;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
			{#if data.defaultName != null}
				<div class="bg-base-100 flex flex-row items-center justify-between p-4">
					<div class="flex flex-col gap-1">
						<p class="text-xs font-medium">Default Name</p>
						<p class="text-base-content/60 text-xs font-light capitalize">
							A default name for the workspace is: {data.defaultName}
						</p>
					</div>
					<button
						class="btn btn-error btn-ghost btn-circle hover:text-error-content text-error"
						onclick={() => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.delete(
									{ name: name, rules: { ...data, defaultName: undefined } },
									'SINGLE'
								);
							} else {
								delete data.defaultName;
								workspaceState.deleteNewUI(data);
								workspaceState.setUiDelete(null);
							}
						}}>{@html GetIcons('delete')}</button
					>
				</div>
			{/if}
		</div>
	</div>
	<form method="dialog" class="modal-backdrop">
		<button onclick={() => workspaceState.setUiDelete(null)}>close</button>
	</form>
</dialog>
