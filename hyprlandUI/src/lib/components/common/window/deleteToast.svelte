<script lang="ts">
	import { GetIcons, windowConn, windowState } from '$lib';
	import { toast } from 'svelte-sonner';
</script>

<div class="flex min-w-sm flex-row items-center justify-between rounded-md bg-[#1B0404] p-3">
	<div class="flex flex-row items-center gap-2">
		<span class="rounded-full p-1 text-xs text-red-200">{@html GetIcons('error', 18)}</span>
		<p class="text-xs font-semibold text-red-300">Delete Window Rule</p>
	</div>
	<div class="flex flex-row gap-3">
		<button
			class="btn btn-sm btn-primary rounded-md text-xs font-medium"
			onclick={() => toast.dismiss()}>Close</button
		>
		<button
			class="btn btn-sm bg-base-content text-base-100 rounded-md text-xs font-medium"
			onclick={() => {
				if (windowState.ui.deleteWindow !== null) {
					windowConn.editWindow(windowState.ui.deleteWindow.old, windowState.ui.deleteWindow.new);
					toast.dismiss();
				}
			}}>Delete</button
		>
	</div>
</div>
