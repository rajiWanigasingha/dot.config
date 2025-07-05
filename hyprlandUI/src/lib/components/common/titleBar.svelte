<script lang="ts">
	let { name, svg, action }: { name: string; svg: string[]; action: (() => void)[] } = $props();
</script>

<div class="flex flex-row items-center justify-between px-4 py-1">
	<div class="flex flex-row items-center gap-2">
		<p class="text-xs font-semibold capitalize">{name}</p>
	</div>
	<div class="flex flex-row gap-3">
		{#each svg as icon ,index}
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle btn-soft" onclick={() => action[index]()}>
				{@html icon}
			</button>
		{/each}
	</div>
</div>
<div class="divider m-0"></div>
