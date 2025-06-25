<script lang="ts">
	import type { MainPageInputData, MainPageInputUI } from '$lib';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();

	let value = $state(ui.value as number);
</script>

<div class="flex w-full flex-col gap-3">
	<div class="flex flex-row items-center justify-between">
		<div class="flex flex-col gap-[2px]">
			<h4 class="text-sm font-bold capitalize">{data.name}</h4>
			<p class="text-base-content/60 max-w-[700px] text-xs font-medium text-wrap capitalize">
				{data.description.replaceAll(';', ',')}
			</p>
		</div>
		<div>
			<input
				type="text"
				class="input input-xl border-0 border-b-1 bg-transparent text-center text-sm shadow-none focus:border-b-1 focus:outline-0"
				bind:value
			/>
		</div>
	</div>
	<div class="join w-full">
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button
			class="btn btn-outline join-item hover:bg-secondary hover:text-secondary-content hover:border-base-content w-1/2 hover:border-1"
			onclick={() => value--}
		>
			<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
				><path fill="currentColor" d="M19 12.998H5v-2h14z" /></svg
			>
		</button>
		<!-- svelte-ignore a11y_consider_explicit_label -->
		<button
			class="btn btn-outline join-item hover:bg-secondary hover:text-secondary-content hover:border-base-content w-1/2 hover:border-1"
			onclick={() => value++}
		>
			<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
				><path fill="currentColor" d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6z" /></svg
			>
		</button>
	</div>
</div>
