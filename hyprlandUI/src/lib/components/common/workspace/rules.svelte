<script lang="ts">
	import { GetIcons, workspaceConn, workspaceState, type WorkspaceData } from '$lib';
	import { toast } from 'svelte-sonner';

	let { rule, name }: { rule: WorkspaceData; name: string } = $props();

	function typedEntries<T extends object>(obj: T): [keyof T, T[keyof T]][] {
		return Object.entries(obj) as [keyof T, T[keyof T]][];
	}

	let monitor = $state(rule.monitor);
	let isDefault = $state(rule.default);
	let gapsIn = $state(rule.gapsIn);
	let gapsOut = $state(rule.gapsOut);
	let borderSize = $state(rule.borderSize);
	let border = $state(rule.border);
	let shadow = $state(rule.shadow);
	let rounding = $state(rule.rounding);
	let decorate = $state(rule.decorate);
	let persistent = $state(rule.persistent);
	let onCreatedEmpty = $state(rule.onCreatedEmpty);
	let defaultName = $state(rule.defaultName);

	$effect(() => {
		if (workspaceState.ui.action) {
			const newWorkspace = {
				name: name,
				rules: {
					monitor: monitor,
					default: isDefault,
					gapsIn: gapsIn,
					gapsOut: gapsOut,
					borderSize: borderSize,
					border: border,
					shadow: shadow,
					rounding: rounding,
					decorate: decorate,
					persistent: persistent,
					onCreatedEmpty: onCreatedEmpty,
					defaultName: defaultName
				}
			};

			if (newWorkspace.name === '') {
				toast.warning('Name Must Not Be Empty');
				return;
			}

			if (workspaceState.ui.newUIData === null) {
				toast.warning('Need To Add Workspace Rules');
				return;
			}

			workspaceConn.add(newWorkspace);
		}
	});
</script>

{#each typedEntries(rule) as [rules, value]}
	{#if rules === 'gapsIn'}
		<!-- svelte-ignore a11y_no_static_element_interactions -->
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Gaps In</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Set the gaps between windows for this workspace
					</p>
				</div>
				<div class="join">
					<button
						class="btn btn-neutral join-item"
						onclick={() => {
							if (gapsIn) {
								gapsIn = gapsIn - 1;
							} else {
								gapsIn = 1;
							}
						}}>{@html GetIcons('minus', 16)}</button
					>
					<input
						type="text"
						class="input join-item text-center text-xs"
						placeholder="1"
						bind:value={gapsIn}
					/>
					<button
						class="btn btn-neutral join-item"
						onclick={() => {
							if (gapsIn) {
								gapsIn = gapsIn + 1;
							} else {
								gapsIn = 1;
							}
						}}>{@html GetIcons('add', 16)}</button
					>
					{#if !workspaceState.ui.pageChange}
						<button
							class="btn btn-warning text-xs"
							onclick={() => workspaceConn.edit({ name: name, rules: { ...rule, gapsIn: gapsIn } })}
							>Apply</button
						>
					{/if}
				</div>
			</div>
		</div>
	{:else if rules === 'gapsOut'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Gaps Out</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Set the gaps between windows and monitor edges for this workspace
					</p>
				</div>
				<div class="join">
					<button
						class="btn btn-neutral join-item"
						onclick={() => {
							if (gapsOut) {
								gapsOut = gapsOut - 1;
							} else {
								gapsOut = 1;
							}
						}}>{@html GetIcons('minus', 16)}</button
					>
					<input
						type="text"
						class="input join-item text-center text-xs"
						placeholder="1"
						bind:value={gapsOut}
					/>
					<button
						class="btn btn-neutral join-item"
						onclick={() => {
							if (gapsOut) {
								gapsOut = gapsOut + 1;
							} else {
								gapsOut = 1;
							}
						}}>{@html GetIcons('add', 16)}</button
					>
					{#if !workspaceState.ui.pageChange}
						<button
							class="btn btn-warning text-xs"
							onclick={() =>
								workspaceConn.edit({ name: name, rules: { ...rule, gapsOut: gapsOut } })}
							>Apply</button
						>
					{/if}
				</div>
			</div>
		</div>
	{:else if rules === 'borderSize'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Border Size</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Set the border size around windows for this workspace
					</p>
				</div>
				<div class="join">
					<button
						class="btn btn-neutral join-item"
						onclick={() => {
							if (borderSize) {
								borderSize = borderSize - 1;
							} else {
								borderSize = 1;
							}
						}}>{@html GetIcons('minus', 16)}</button
					>
					<input
						type="text"
						class="input join-item text-center text-xs"
						placeholder="1"
						bind:value={borderSize}
						oninput={(e) => (borderSize = Number(e.currentTarget.value))}
					/>
					<button
						class="btn btn-neutral join-item"
						onclick={() => {
							if (borderSize) {
								borderSize = borderSize + 1;
							} else {
								borderSize = 1;
							}
						}}>{@html GetIcons('add', 16)}</button
					>
					{#if !workspaceState.ui.pageChange}
						<button
							class="btn btn-warning join-item text-xs"
							onclick={() =>
								workspaceConn.edit({ name: name, rules: { ...rule, borderSize: borderSize } })}
							>Apply</button
						>
					{/if}
				</div>
			</div>
		</div>
	{:else if rules === 'border'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Border</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Whether to draw borders or not, for this workspace
					</p>
				</div>
				<label class="toggle toggle-xl text-base-content">
					<input
						type="checkbox"
						class="hidden"
						checked={border}
						oninput={(e) => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.edit({
									name: name,
									rules: { ...rule, border: e.currentTarget.checked }
								});
							}
						}}
					/>
					<svg
						aria-label="enabled"
						xmlns="http://www.w3.org/2000/svg"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="4"
						stroke-linecap="round"
						stroke-linejoin="round"
					>
						<path d="M18 6 6 18" />
						<path d="m6 6 12 12" />
					</svg>
					<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
						<g
							stroke-linejoin="round"
							stroke-linecap="round"
							stroke-width="4"
							fill="none"
							stroke="currentColor"
						>
							<path d="M20 6 9 17l-5-5"></path>
						</g>
					</svg>
				</label>
			</div>
		</div>
	{:else if rules === 'shadow'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Shadow</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Whether to draw shadows or not, for this workspace
					</p>
				</div>
				<label class="toggle toggle-xl text-base-content">
					<input
						type="checkbox"
						class="hidden"
						checked={shadow}
						oninput={(e) => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.edit({
									name: name,
									rules: { ...rule, shadow: e.currentTarget.checked }
								});
							}
						}}
					/>
					<svg
						aria-label="enabled"
						xmlns="http://www.w3.org/2000/svg"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="4"
						stroke-linecap="round"
						stroke-linejoin="round"
					>
						<path d="M18 6 6 18" />
						<path d="m6 6 12 12" />
					</svg>
					<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
						<g
							stroke-linejoin="round"
							stroke-linecap="round"
							stroke-width="4"
							fill="none"
							stroke="currentColor"
						>
							<path d="M20 6 9 17l-5-5"></path>
						</g>
					</svg>
				</label>
			</div>
		</div>
	{:else if rules === 'rounding'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Rounding</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Whether to draw rounded windows or not, for this workspace
					</p>
				</div>
				<label class="toggle toggle-xl text-base-content">
					<input
						type="checkbox"
						class="hidden"
						checked={rounding}
						oninput={(e) => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.edit({
									name: name,
									rules: { ...rule, rounding: e.currentTarget.checked }
								});
							}
						}}
					/>
					<svg
						aria-label="enabled"
						xmlns="http://www.w3.org/2000/svg"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="4"
						stroke-linecap="round"
						stroke-linejoin="round"
					>
						<path d="M18 6 6 18" />
						<path d="m6 6 12 12" />
					</svg>
					<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
						<g
							stroke-linejoin="round"
							stroke-linecap="round"
							stroke-width="4"
							fill="none"
							stroke="currentColor"
						>
							<path d="M20 6 9 17l-5-5"></path>
						</g>
					</svg>
				</label>
			</div>
		</div>
	{:else if rules === 'decorate'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Decorate</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Whether to draw window decorations or not, for this workspace
					</p>
				</div>
				<label class="toggle toggle-xl text-base-content">
					<input
						type="checkbox"
						class="hidden"
						checked={decorate}
						oninput={(e) => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.edit({
									name: name,
									rules: { ...rule, decorate: e.currentTarget.checked }
								});
							}
						}}
					/>
					<svg
						aria-label="enabled"
						xmlns="http://www.w3.org/2000/svg"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="4"
						stroke-linecap="round"
						stroke-linejoin="round"
					>
						<path d="M18 6 6 18" />
						<path d="m6 6 12 12" />
					</svg>
					<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
						<g
							stroke-linejoin="round"
							stroke-linecap="round"
							stroke-width="4"
							fill="none"
							stroke="currentColor"
						>
							<path d="M20 6 9 17l-5-5"></path>
						</g>
					</svg>
				</label>
			</div>
		</div>
	{:else if rules === 'persistent'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Persistent</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Keep this workspace alive even if empty and inactive, for this workspace
					</p>
				</div>
				<label class="toggle toggle-xl text-base-content">
					<input
						type="checkbox"
						class="hidden"
						checked={persistent}
						oninput={(e) => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.edit({
									name: name,
									rules: { ...rule, persistent: e.currentTarget.checked }
								});
							}
						}}
					/>
					<svg
						aria-label="enabled"
						xmlns="http://www.w3.org/2000/svg"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="4"
						stroke-linecap="round"
						stroke-linejoin="round"
					>
						<path d="M18 6 6 18" />
						<path d="m6 6 12 12" />
					</svg>
					<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
						<g
							stroke-linejoin="round"
							stroke-linecap="round"
							stroke-width="4"
							fill="none"
							stroke="currentColor"
						>
							<path d="M20 6 9 17l-5-5"></path>
						</g>
					</svg>
				</label>
			</div>
		</div>
	{:else if rules === 'default'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-row justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Default</p>
					<p class="text-base-content/60 text-xs font-semibold">
						Whether this workspace should be the default workspace for the given monitor
					</p>
				</div>
				<label class="toggle toggle-xl text-base-content">
					<input
						type="checkbox"
						class="hidden"
						checked={isDefault}
						oninput={(e) => {
							if (!workspaceState.ui.pageChange) {
								workspaceConn.edit({
									name: name,
									rules: { ...rule, default: e.currentTarget.checked }
								});
							}
						}}
					/>
					<svg
						aria-label="enabled"
						xmlns="http://www.w3.org/2000/svg"
						viewBox="0 0 24 24"
						fill="none"
						stroke="currentColor"
						stroke-width="4"
						stroke-linecap="round"
						stroke-linejoin="round"
					>
						<path d="M18 6 6 18" />
						<path d="m6 6 12 12" />
					</svg>
					<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
						<g
							stroke-linejoin="round"
							stroke-linecap="round"
							stroke-width="4"
							fill="none"
							stroke="currentColor"
						>
							<path d="M20 6 9 17l-5-5"></path>
						</g>
					</svg>
				</label>
			</div>
		</div>
	{:else if rules === 'monitor'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-col gap-2 rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Monitor</p>
					<p class="text-base-content/60 text-xs font-semibold">Binds a workspace to a monitor</p>
				</div>
				<div class="join">
					<input
						type="text"
						class="input join-item w-full p-2 text-xs"
						placeholder="eDp-1"
						bind:value={monitor}
					/>
					{#if !workspaceState.ui.pageChange}
						<button
							class="btn btn-warning join-item text-xs"
							onclick={() =>
								workspaceConn.edit({
									name: name,
									rules: { ...rule, monitor: monitor }
								})}>Apply</button
						>
					{/if}
				</div>
			</div>
		</div>
	{:else if rules === 'onCreatedEmpty'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-col gap-2 rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">On Created Empty</p>
					<p class="text-base-content/60 text-xs font-semibold">
						A command to be executed once a workspace is created empty (i.e. not created by moving a
						window to it).
					</p>
				</div>
				<div class="join">
					<input
						type="text"
						class="input join-item w-full p-2 text-xs"
						placeholder="float; move: 100 100"
						bind:value={onCreatedEmpty}
					/>
					{#if !workspaceState.ui.pageChange}
						<button
							class="btn btn-warning join-item text-xs"
							onclick={() =>
								workspaceConn.edit({
									name: name,
									rules: { ...rule, onCreatedEmpty: onCreatedEmpty }
								})}>Apply</button
						>
					{/if}
				</div>
			</div>
		</div>
	{:else if rules === 'defaultName'}
		<div class="flex flex-col gap-3">
			<div class="bg-base-300 flex flex-col gap-2 rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-semibold">Default Name</p>
					<p class="text-base-content/60 text-xs font-semibold">
						A default name for the workspace.
					</p>
				</div>
				<div class="join">
					<input
						type="text"
						class="input join-item w-full p-2 text-xs"
						placeholder="This_is_new_workspace_name"
						bind:value={defaultName}
					/>
					{#if !workspaceState.ui.pageChange}
						<button
							class="btn btn-warning join-item text-xs"
							onclick={() =>
								workspaceConn.edit({
									name: name,
									rules: { ...rule, defaultName: defaultName }
								})}>Apply</button
						>
					{/if}
				</div>
			</div>
		</div>
	{/if}
{/each}
