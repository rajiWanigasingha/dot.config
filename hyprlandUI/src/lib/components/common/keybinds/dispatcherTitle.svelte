<script lang="ts">
	import { GetIcons, keybindConn, keybindState } from '$lib';

	let { name } = $props();
</script>

<div class="flex flex-row items-center justify-between px-4 py-[13.5px]">
	<div class="flex flex-row items-center gap-4">
		<button
			class="h-fit cursor-pointer bg-transparent hover:bg-transparent"
			onclick={() => {
				keybindState.setLoadDispatcher('');
				keybindState.holdKeybinds.args = '';
				keybindState.holdKeybinds.dispatcher = ''
				keybindConn.getDispatchers()
			}}
		>
			{@html GetIcons('arrow_left', 16)}
		</button>
		<p class="text-xs font-semibold">{name}</p>
	</div>
</div>

<div class="divider m-0"></div>
