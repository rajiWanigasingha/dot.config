<script>
	import { goto } from '$app/navigation';
	import { GetIcons, keybindState } from '$lib';
</script>

<div class="bg-base-200 max-h-screen min-h-screen w-full overflow-y-auto">
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<p class="text-xs font-semibold">Globle ShortCuts</p>
		<div class="flex flex-row gap-3">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button
				class="btn btn-circle btn-soft"
				onclick={() => {
					keybindState.setHelp('KEYBIND_HELP');
					goto('/hyprland/custom/keybinds/addNew');
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
	<div class="flex flex-col gap-3 p-8">
		{#each keybindState.getKeybinds() as binds}
			<div class="bg-base-300 w-full rounded-md">
				<div class="bg-base-100 flex w-full flex-row justify-end gap-2 rounded-t-md px-4 py-2">
					<button class="btn btn-sm btn-warning">{@html GetIcons('edit', 12)} Edit</button>
					<button class="btn btn-sm btn-error">{@html GetIcons('delete', 12)} Delete</button>
				</div>
				<div class="overflow-x-auto px-4 py-2">
					<table class="table-zebra table">
						<tbody>
							<tr>
								<th class="w-[100px] text-xs font-medium">Keybind</th>
								<td>
									<div class="flex flex-row gap-2">
										{#each binds.mod as mods}
											<kbd class="kbd text-xs font-semibold">{mods}</kbd>
										{/each}
										<p class="text-sm font-medium">+</p>
										{#each binds.keys as keys}
											<kbd class="kbd text-xs font-semibold">{keys}</kbd>
										{/each}
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-[100px] text-xs font-medium">Dispatcher</th>
								<td>
									<div class="badge badge-accent text-xs">
										{binds.dispatcher}
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-[100px] text-xs font-medium">Argument</th>
								<td class="text-xs font-semibold">{binds.args}</td>
							</tr>
							<tr>
								<th class="w-[100px] text-xs font-medium">Flags</th>
								<td>
									<div class="flex flex-row gap-2">
										{#each binds.flags as flag}
											<kbd class="kbd text-xs font-semibold">{flag}</kbd>
										{/each}
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-[100px] text-xs font-medium">Discription</th>
								<td>{binds.description === 'null' ? '' : binds.description}</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		{/each}
	</div>
</div>
