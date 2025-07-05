<script lang="ts">
	import {
		GetIcons,
		TitleBar
	} from '$lib';
	import { slide } from 'svelte/transition';

	const icons = [GetIcons('add'), GetIcons('search')];

	const addNewVariable = () => {
		open = true;
	};
	const search = () => {};

	const actions = [addNewVariable, search];

	let open = $state(false);
</script>

<div class="bg-base-200 max-h-screen min-h-screen w-full overflow-y-auto">
	<TitleBar name={'variables'} svg={icons} action={actions} />

	{#if open}
		<div in:slide={{ duration: 300 }} out:slide={{ duration: 300 }} class="w-full px-8 py-4">
			<div class="flex w-full justify-end">
				<button class="btn btn-error btn-circle btn-xs" onclick={() => (open = false)}>
					{@html GetIcons('close', 16)}
				</button>
			</div>
			<fieldset class="fieldset bg-base-300 border-base-300 rounded-box w-full border p-4">
				<legend class="fieldset-legend">Create Variable</legend>
				<input
					type="text"
					class="input bg-base-100/50 join-item w-full text-xs focus:outline-0"
					placeholder="New_Variable"
				/>
				<p class="label">Variable Can't Have Spaces In Between Words And Must Be Unque</p>
				<input
					type="text"
					class="input bg-base-100/50 join-item w-full text-xs focus:outline-0"
					placeholder="New_Variable"
				/>
				<p class="label">Enter Variable value</p>
				<button class="btn btn-success w-full text-xs font-medium">Create New Variable</button>
			</fieldset>
		</div>
	{/if}

	<div class="min-h-[94vh] overflow-y-auto px-8 py-4">
		<div class="rounded-box border-base-content/5 bg-base-100/10 border">
			<table class="table">
				<!-- head -->
				<thead>
					<tr>
						<th></th>
						<th>Name</th>
						<th>Value</th>
						<th class="text-end">Action</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>1</th>
						<td class="text-xs font-medium">Variable_Name</td>
						<td>
							<p class="text-xs font-semibold">Variable Value</p>
						</td>
						<td>
							<div class="flex flex-row justify-end gap-3">
								<button class="btn btn-xs btn-warning btn-circle">
									{@html GetIcons('edit', 16)}
								</button>
								<button class="btn btn-xs btn-error btn-circle">
									{@html GetIcons('delete', 16)}
								</button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>