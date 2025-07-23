<script lang="ts">
	import { monitorConn, monitorState } from '$lib';
</script>

<div class="flex w-full flex-col gap-3 p-4">
	<div>
		<p class="text-sm font-medium">Connected Monitors</p>
	</div>
	<div class="flex flex-col gap-2">
		{#each monitorState.getDisable() as info}
			<div class="bg-base-300 flex flex-row items-center justify-between p-4">
				<div class="flex flex-col gap-1">
					<p class="text-xs font-medium">{info.monitor?.name}</p>
					<p class="text-base-content/60 text-xs">
						{info.monitorInfo?.description} ,id is {info.monitorInfo?.id}
					</p>
				</div>
				<div>
					{#if info.disable}
						<button
							class="btn btn-success text-xs"
							onclick={() =>
								monitorConn.disableMonitor({ ...info.monitor!!, disable: false }, false)}
							>Enable This Monitor</button
						>
					{:else}
						<button
							class="btn btn-error text-xs"
							onclick={() => monitorConn.disableMonitor({ ...info.monitor!!, disable: true }, true)}
							>Disable This Monitor</button
						>
					{/if}
				</div>
			</div>
		{/each}
	</div>
</div>
