<script lang="ts">
	import type { MainPageInputData, MainPageInputUI } from '$lib';

	let { ui, data }: { ui: MainPageInputUI; data: MainPageInputData } = $props();
</script>

<div class="flex w-full flex-col gap-3">
	<div class="flex flex-row items-center justify-between">
		<div class="flex flex-col gap-[2px]">
			<h4 class="text-sm font-bold capitalize">{data.name}</h4>
			<p class="text-base-content/60 max-w-[700px] text-xs font-medium text-wrap capitalize">
				{data.description.replaceAll(';', ',')}
			</p>
		</div>
	</div>
	<div>
		<input
			type="text"
			placeholder={ui.placeholder}
			class="input w-full bg-transparent text-xs font-medium"
			value={ui.value as string}
		/>
	</div>
</div>
