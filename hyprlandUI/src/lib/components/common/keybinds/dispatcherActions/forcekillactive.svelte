<script>
	import { keybindState } from '$lib';
</script>

<div class="flex flex-row items-center justify-between px-4 py-[13.5px]">
	<p class="text-xs font-semibold">No Args For Force kill Active Window</p>
</div>

<div class="divider m-0"></div>

<div class="p-4">
	<button
		class="btn btn-success w-full text-xs font-medium"
		onclick={() => keybindState.setSummery(true)}>Create Keybind</button
	>
</div>
