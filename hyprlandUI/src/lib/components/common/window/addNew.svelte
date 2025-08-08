<script lang="ts">
	import { GetIcons, windowConn, windowState, type WindowRulesPayload } from '$lib';
	import { toast } from 'svelte-sonner';
	import { slide } from 'svelte/transition';

	$effect(() => {
		if (windowState.getEdit() !== null) {
			const window = windowState.getEdit();

			name = window!!.params.join(' ');

			oldName = name;

			addRules = [];

			window?.rules.forEach((item) => {
				addRules.push({ name: item.name, value: item.value ?? '' });
				oldRules.push({ name: item.name, value: item.value ?? '' });
			});

			edit = true;

			windowState.setEdit(null);
		}
	});

	let edit = $state(false);
	let addArgs = $state('');
	let rules = $state({ rule: '', args: '' });
	let addRules = $state([] as { name: string; value: string }[]);
	let oldRules = $state([] as { name: string; value: string }[]);
	let oldName = $state('');
	let name = $state('');

	function addNew() {
		if (name === '') {
			toast.warning("Params Can't Be Empty");
			return;
		}

		if (addRules.length === 0) {
			toast.warning('Must Add New Rules For Matching Window');
			return;
		}

		const window = {
			rules: addRules,
			params: name.split(',')
		} as WindowRulesPayload;

		windowConn.addNewWindow(window);
	}

	function editWindow() {
		if (name === '') {
			toast.warning("Params Can't Be Empty");
			return;
		}

		if (addRules.length === 0) {
			toast.warning('Must Add New Rules For Matching Window');
			return;
		}

		const old = {
			rules: oldRules,
			params: oldName.split(',')
		} as WindowRulesPayload;

		const window = {
			rules: addRules,
			params: name.split(',')
		} as WindowRulesPayload;

		windowConn.editWindow(old, window);
	}
</script>

<dialog id="my_modal_2" class="modal" open={windowState.ui.open}>
	<div class="modal-box bg-base-300 max-h-6xl max-w-6xl">
		<div class="flex w-full flex-row">
			<div class={addRules.length === 0 ? 'w-full' : 'w-8/12'}>
				<div class="flex flex-col gap-1">
					<h3 class="text-xs font-bold">Add New Window</h3>
					<p class="text-base-content/60 text-xs font-medium">Create New Window Rules</p>
				</div>
				<div class="divider m-0"></div>
				<div class="flex flex-col gap-3">
					<fieldset class="fieldset">
						<legend class="fieldset-legend">Window Params</legend>
						<textarea
							class="textarea bg-base-200/60 w-full resize-none text-xs outline-0 hover:outline-0"
							placeholder="class:dot.config"
							bind:value={name}
						></textarea>
						<p class="label text-xs">Add Parameters That Will Match Your Window</p>
					</fieldset>
					<fieldset class="fieldset">
						<legend class="fieldset-legend">Select Rules</legend>
						<select
							class="select bg-base-200/60 w-full text-xs outline-0 hover:ring-0 hover:outline-0"
							oninput={(e) =>
								windowConn.getWindowData(e.currentTarget.value as 'STATIC' | 'DYNAMIC' | 'PARAMS')}
						>
							<option disabled selected>Select Window Rules</option>
							<option value="STATIC">Static</option>
							<option value="DYNAMIC">Dynamic</option>
							<option value="PARAMS">Params</option>
						</select>
						<p class="label text-xs">Add Rules Type For Window That Match</p>
					</fieldset>
					{#if windowState.getWindowGetData().length !== 0}
						<div transition:slide={{ duration: 300 }} class="flex flex-col gap-3">
							{#if windowState.store.rulesType === 'STATIC'}
								<fieldset class="fieldset">
									<legend class="fieldset-legend">Select Static Rules</legend>
									<select
										class="select bg-base-200/60 w-full text-xs outline-0 hover:ring-0 hover:outline-0"
										oninput={(e) => {
											if (windowState.findWindowRule(e.currentTarget.value)) {
												addArgs = e.currentTarget.value;
											} else {
												addArgs = 'null';
											}

											rules.rule = e.currentTarget.value;
										}}
									>
										<option disabled selected>Select Static Rules For Add To Window</option>
										{#each windowState.getWindowGetData() as data}
											<option value={data.name} class="text-xs">{data.actionName}</option>
										{/each}
									</select>
									<p class="label text-xs text-wrap">
										Static rules are evaluated once when the window is opened and never again. This
										essentially means that it is always the initialTitle and initialClass which will
										be found when matching on title and class, respectively.
									</p>
								</fieldset>
							{:else if windowState.store.rulesType === 'DYNAMIC'}
								<fieldset class="fieldset">
									<legend class="fieldset-legend">Select Dynamic Rules</legend>
									<select
										class="select bg-base-200/60 w-full text-xs outline-0 hover:ring-0 hover:outline-0"
										oninput={(e) => {
											if (windowState.findWindowRule(e.currentTarget.value)) {
												addArgs = e.currentTarget.value;
											} else {
												addArgs = 'null';
											}

											rules.rule = e.currentTarget.value;
										}}
									>
										<option disabled selected>Select Dynamic Rules For Add To Window</option>
										{#each windowState.getWindowGetData() as data}
											<option value={data.name} class="text-xs">{data.actionName}</option>
										{/each}
									</select>
									<p class="label text-xs text-wrap">
										Dynamic rules are re-evaluated every time a property changes.
									</p>
								</fieldset>
							{:else}
								<fieldset class="fieldset">
									<legend class="fieldset-legend">Select Props Rules</legend>
									<select
										class="select bg-base-200/60 w-full text-xs outline-0 hover:ring-0 hover:outline-0"
										oninput={(e) => {
											if (windowState.findWindowRule(e.currentTarget.value)) {
												addArgs = e.currentTarget.value;
											} else {
												addArgs = 'null';
											}

											rules.rule = e.currentTarget.value;
										}}
									>
										<option disabled selected>Select Props Rules For Add To Window</option>
										{#each windowState.getWindowGetData() as data}
											<option value={data.name} class="text-xs">{data.actionName}</option>
										{/each}
									</select>
									<p class="label text-xs text-wrap">Can Add Only One For One Window</p>
								</fieldset>
							{/if}
							{#if addArgs !== ''}
								<div transition:slide={{ duration: 300 }} class="flex flex-col gap-3">
									{#if addArgs === 'null'}
										<p class="bg-base-100 flex w-full flex-row gap-2 p-4 text-xs font-medium">
											{@html GetIcons('error', 16)} No Argument Needed For This Rule
										</p>
									{:else}
										<fieldset class="fieldset">
											<legend class="fieldset-legend">Add Args For Rules</legend>
											<textarea
												class="textarea bg-base-200/60 w-full resize-none text-xs outline-0 hover:outline-0"
												placeholder="20"
												bind:value={rules.args}
											></textarea>
											<p class="label text-xs text-wrap">Add Args For Rules For Window</p>
										</fieldset>
									{/if}

									{#if windowState.ui.addActionArgHelp !== ''}
										<div class="bg-base-200/60 border-l-info border-l-2 p-4">
											<p class="text-base-content/90 text-xs font-medium">
												{windowState.ui.addActionArgHelp}
											</p>
										</div>
									{/if}

									<div class="mt-2">
										<button
											class="btn btn-info w-full text-xs"
											onclick={() => {
												if (rules.rule !== '') {
													if (addArgs !== 'null') {
														if (rules.rule === '') {
															toast.warning('Argument Is Needed For This Rule');
															return;
														}
														addRules.push({
															name: rules.rule,
															value: rules.args
														});
													} else {
														addRules.push({
															name: rules.rule,
															value: rules.args
														});
													}

													rules = { rule: '', args: '' };
													windowState.setWindowGetData([]);
												}
											}}>Add New Rules</button
										>
									</div>
								</div>
							{/if}
						</div>
					{/if}
					<div class="mt-2">
						{#if edit}
							<button class="btn btn-warning w-full text-xs" onclick={() => editWindow()}
								>Edit Window Rule</button
							>
						{:else if addRules.length === 0 || name === ''}
							<button class="btn btn-success w-full text-xs" disabled>Add New Window Rule</button>
						{:else}
							<button class="btn btn-success w-full text-xs" onclick={() => addNew()}
								>Add New Window Rule</button
							>
						{/if}
					</div>
				</div>
			</div>
			{#if addRules.length > 0}
				<div class="divider divider-horizontal m-2"></div>
			{/if}
			{#if addRules.length > 0}
				<div class="flex w-4/12 flex-col p-1" transition:slide={{ duration: 300, delay: 100 }}>
					<div>
						<p class="text-xs font-medium">Rules For Window</p>
						<p class="text-base-content/60 text-xs font-medium">
							These Are Rules That Will Apply In Window
						</p>
					</div>
					<div class="divider m-0"></div>
					<div class="max-h-6xl flex flex-col gap-2 overflow-y-auto">
						{#each addRules as rule}
							<div class="bg-base-100 flex flex-row items-center justify-between rounded-md p-4">
								<p class="text-xs font-medium">{rule.name}</p>
								<div class="flex flex-row items-center gap-3">
									<button
										class="btn btn-ghost btn-sm btn-circle text-warning hover:text-warning-content hover:bg-warning"
										onclick={() => {
											addRules = addRules.filter((item) => item.name !== rule.name);
											rules = { rule: rule.name, args: rule.value };
										}}>{@html GetIcons('edit', 16)}</button
									>
									<button
										class="btn btn-ghost btn-sm btn-circle text-error hover:text-error-content hover:bg-error"
										onclick={() => {
											addRules = addRules.filter((item) => item.name !== rule.name);
										}}>{@html GetIcons('delete', 16)}</button
									>
								</div>
							</div>
						{/each}
					</div>
				</div>
			{/if}
		</div>
	</div>
	<form method="dialog" class="modal-backdrop">
		<button
			onclick={() => {
				addRules = [];
				addArgs = '';
				name = '';
				rules = { args: '', rule: '' };
				windowState.ui.open = false;
				windowState.setEdit(null);
			}}>close</button
		>
	</form>
</dialog>
