<script lang="ts">
	import { GetIcons, TitleBar, variableConn, variableState } from '$lib';
	import { tick } from 'svelte';
	import { slide } from 'svelte/transition';

	const icons = [GetIcons('add'), GetIcons('search')];

	const addNewVariable = () => {
		open = true;
	};
	const search = () => {};

	const actions = [addNewVariable, search];

	let open = $state(false);
	let edit = $state(false);

	let oldName = $state('');
	let name = $state('');
	let value = $state('');

	let errorValidation = $state(false);
	let errorMessage = $state('');

	function createNewVariable() {
		errorValidation = false;
		errorMessage = '';

		if (name === '' || value === '') {
			errorValidation = true;
			errorMessage = 'Validation Error! Variable Name Or Value Is Empty.';
			return;
		}

		if (name[0] !== '$') {
			name = '$' + name;
		}

		const exist = variableState.checkVariables(name);

		if (oldName === '') {
			console.log('old name is empty');
			return;
		}

		if (edit) {
			variableConn.addUpdate(oldName, name, value);
		} else {
			if (!exist) {
				errorValidation = true;
				errorMessage = 'Validation Error! This Variable All ready Exist.';
				return;
			}

			variableConn.addVariable(name, value);
		}

		name = value = oldName = '';
		open = false;
		edit = false;
	}

	function editNewVariable(nameEdit: string, valueEdit: string) {
		open = true;
		edit = true;

		tick().then(() => {
			const div = document.querySelector('#editOrAddDiv') as HTMLDivElement;
			if (div) {
				div.scrollIntoView({ behavior: 'smooth' });
			}
		});

		oldName = name = nameEdit;
		value = valueEdit;
	}
</script>

<div class="bg-base-200 max-h-screen min-h-screen w-full overflow-y-auto">
	<TitleBar name={'variables'} svg={icons} action={actions} />

	{#if open}
		<div
			in:slide={{ duration: 300 }}
			out:slide={{ duration: 300 }}
			class="w-full px-8 py-4"
			id="editOrAddDiv"
		>
			<div class="flex w-full justify-end">
				<button class="btn btn-error btn-circle btn-xs" onclick={() => (open = false)}>
					{@html GetIcons('close', 16)}
				</button>
			</div>
			<fieldset class="fieldset bg-base-300 border-base-300 rounded-box w-full border p-4">
				<legend class="fieldset-legend">Create And Update Variable</legend>
				<input
					type="text"
					class="input bg-base-100/50 join-item w-full text-xs focus:outline-0"
					placeholder="New_Variable"
					bind:value={name}
					oninput={(e) => {
						if (e.currentTarget.value[e.currentTarget.value.length - 1] !== ' ') {
							name = e.currentTarget.value;
						} else {
							name = name.replace(' ', '_');
						}
					}}
				/>
				<p class="label text-xs">Variable Can't Have Spaces In Between Words And Must Be Unque</p>
				<input
					type="text"
					class="input bg-base-100/50 join-item w-full text-xs focus:outline-0"
					placeholder="New_Variable"
					bind:value
				/>
				<p class="label text-xs">Enter Variable value</p>
				<button
					class="btn btn-success w-full text-xs font-medium"
					onclick={() => createNewVariable()}
					>{!edit ? 'Create New Variable' : 'Edit Variable'}</button
				>

				{#if errorValidation}
					<div role="alert" class="alert alert-error my-2">
						<svg
							xmlns="http://www.w3.org/2000/svg"
							class="h-6 w-6 shrink-0 stroke-current"
							fill="none"
							viewBox="0 0 24 24"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								stroke-width="2"
								d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
							/>
						</svg>
						<span class="font-semibold">{errorMessage}</span>
						<div>
							<button class="btn btn-sm" onclick={() => (errorValidation = false)}>Close</button>
						</div>
					</div>
				{/if}
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
					{#each variableState.state.variables as variables, index}
						<tr>
							<th>{index + 1}</th>
							<td class="text-xs font-medium">{variables.name}</td>
							<td>
								<p class="text-xs font-semibold">{variables.value}</p>
							</td>
							<td>
								<div class="flex flex-row justify-end gap-3">
									<button
										class="btn btn-xs btn-warning btn-circle"
										onclick={() => editNewVariable(variables.name, variables.value)}
									>
										{@html GetIcons('edit', 16)}
									</button>
									<button class="btn btn-xs btn-error btn-circle">
										{@html GetIcons('delete', 16)}
									</button>
								</div>
							</td>
						</tr>
					{/each}
				</tbody>
			</table>
		</div>
	</div>
</div>
