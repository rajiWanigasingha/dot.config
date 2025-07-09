<script lang="ts">
	import { envConn, envState, GetIcons } from '$lib';
	import { onDestroy, tick } from 'svelte';
	import { toast } from 'svelte-sonner';

	let keyword = $state('env');
	let value = $state('');
	let oldValue = $state('');
	let edit = $state(false);

	$effect(() => {
		if (envConn.wsEnv === null) {
			console.log('Connection Is Closed Reconnection To Env WebSocket');
			envConn.connect();
		}
	});

	onDestroy(() => {
		envConn.close();
	});

	function newEnv() {
		if (keyword === '' || value === '') {
			return;
		}

		envConn.createNew({ actions: 'ADD', keyword: keyword, value: value, oldValue: null });

		keyword = 'env';
		oldValue = value = '';
	}

	function update() {
		if (keyword === '' || value === '' || oldValue === '') {
			return;
		}

		envConn.updateEnv({ actions: 'UPDATE', keyword: keyword, value: value, oldValue: oldValue });

		edit = false;
		keyword = 'env';
		oldValue = value = '';
	}

	function editScroll() {
		edit = true;
		tick().then(() => {
			const div = document.querySelector('#editEnv') as HTMLDivElement;
			if (div) {
				div.scrollIntoView({ behavior: 'smooth' });
			}
		});
	}
</script>

<div class="bg-base-200 max-h-screen min-h-screen w-full overflow-y-auto">
	<div class="flex flex-row items-center justify-between px-4 py-1">
		<p class="text-xs font-semibold">Enviroment Variables</p>
		<div class="flex flex-row gap-3">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle btn-soft" onclick={() => {}}>
				{@html GetIcons('search')}
			</button>
		</div>
	</div>
	<div class="divider m-0"></div>

	<div class="join w-full p-8" id="editEnv">
		<label class="select text-xs">
			<span class="label">Type</span>
			<select class="text-xs focus:ring-0" bind:value={keyword}>
				<option value="env" selected>env</option>
				<option value="envd">envd</option>
			</select>
		</label>
		<label class="input join-item w-full text-xs">
			<span class="label text-xs">Env</span>
			<input
				type="text"
				placeholder="Envirenment Variable"
				class="text-xs focus:ring-0"
				bind:value
			/>
		</label>
		{#if !edit}
			<button class="join-item btn btn-success w-[100px] text-xs" onclick={() => newEnv()}>
				Add
			</button>
		{:else}
			<button class="join-item btn btn-success w-[100px] text-xs" onclick={() => update()}>
				Edit
			</button>
			<button
				class="join-item btn btn-neutral w-[100px] text-xs"
				onclick={() => {
					edit = false;
					keyword = 'env';
					value = '';
				}}
			>
				Close
			</button>
		{/if}
	</div>

	<div class="flex flex-col gap-3 px-8 py-4">
		{#each envState.getEnv() as env}
			<div class="bg-base-300 flex w-full flex-row items-center justify-between rounded-md p-4">
				<div class="flex flex-col gap-1">
					<p class="text-base-content/60 text-xs">{env.keyword}</p>
					<p class="text-xs font-medium">{env.value}</p>
				</div>
				<div class="flex flex-row justify-end gap-3">
					<button
						class="btn btn-xs btn-warning btn-circle"
						onclick={() => {
							(keyword = env.keyword), (value = oldValue = env.value);
							editScroll();
						}}
					>
						{@html GetIcons('edit', 16)}
					</button>
					<button
						class="btn btn-xs btn-error btn-circle"
						onclick={() =>
							toast.warning('This Action Will Delete This Env. Variable.', {
								action: {
									label: 'Confirm',
									onClick: () =>
										envConn.deleteEnv({
											actions: 'DELETE',
											keyword: env.keyword,
											value: env.value,
											oldValue: null
										})
								}
							})}
					>
						{@html GetIcons('delete', 16)}
					</button>
				</div>
			</div>
		{/each}
	</div>
</div>
