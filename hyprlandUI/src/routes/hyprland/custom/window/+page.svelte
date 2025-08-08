<script lang="ts">
	import { GetIcons, WindowAddNew, windowConn, WindowDelete, windowState } from '$lib';
	import { onDestroy } from 'svelte';
	import { toast } from 'svelte-sonner';

	$effect(() => {
		if (windowConn.wsWindow === null) {
			windowConn.connect();
		}
	});

	onDestroy(() => {
		windowConn.desconnect();
	});
</script>

<WindowAddNew />

<div class="bg-base-200 max-h-screen min-h-screen w-full">
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<p class="text-xs font-semibold">Window Rules</p>
		<div class="flex flex-row gap-4">
			<button class="btn btn-circle btn-soft" onclick={() => (windowState.ui.open = true)}>
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
	<div class="flex max-h-[94vh] flex-col gap-6 overflow-y-auto p-8">
		{#each windowState.getWindow() as window}
			<div>
				<div class="bg-base-100 flex flex-row items-center justify-end gap-3 p-2">
					<button
						class="btn btn-sm btn-warning btn-circle text-xs font-semibold"
						onclick={() => {
							windowState.setEdit(window);
							windowState.ui.open = true;
						}}>{@html GetIcons('edit', 16)}</button
					>
					<button
						class="btn btn-sm btn-error btn-circle text-xs font-semibold"
						onclick={() => {
							toast.error('Delete This Entire Window Rule', {
								action: {
									label: 'Delete',
									onClick: () => {
										windowConn.deleteWindow(window);
									}
								},
								cancel: {
									label: 'Close',
									onClick: () => {
										toast.dismiss();
									}
								}
							});
						}}>{@html GetIcons('delete', 16)}</button
					>
				</div>
				<div class="bg-base-300 flex w-full flex-col gap-4 p-4">
					<div>
						<p class="text-xs font-semibold">{window.params.join(',')}</p>
					</div>
					<div>
						<table class="table">
							<thead>
								<tr>
									<th class="bg-base-100 border-base-content/5 border text-xs font-medium"
										>Rule Name</th
									>
									<th class="bg-base-100 border-base-content/5 border text-xs font-medium"
										>Rules Value</th
									>
									<th class="bg-base-100 border-base-content/5 border text-xs font-medium"
										>Actions</th
									>
								</tr>
							</thead>
							<tbody>
								{#each window.rules as rule, index}
									<tr class="bg-base-200/60">
										<td class="border-base-content/5 w-1/2 border text-xs">{rule.name}</td>
										<td class="border-base-content/5 w-1/2 border text-xs"
											>{rule.value ? rule.value : '-'}</td
										>
										<td class="border-base-content/5 flex justify-center border text-xs">
											<button
												class="btn btn-sm btn-circle btn-ghost text-error hover:text-error-content hover:bg-error"
												onclick={() => {
													if (window.rules.length !== 1) {
														toast.custom(WindowDelete);

														const newRule = [...window.rules];

														const newWindow = {
															params: window.params,
															rules: newRule.filter((_, i) => i !== index)
														};

														windowState.ui.deleteWindow = { old: window, new: newWindow };
													} else {
														toast.error('Delete This Entire Window Rule', {
															action: {
																label: 'Delete',
																onClick: () => {
																	windowConn.deleteWindow(window);
																	toast.dismiss()
																}
															},
															cancel: {
																label: 'Close',
																onClick: () => {
																	toast.dismiss();
																}
															}
														});
													}
												}}>{@html GetIcons('delete', 18)}</button
											>
										</td>
									</tr>
								{/each}
							</tbody>
						</table>
					</div>
				</div>
			</div>
		{/each}
	</div>
</div>
