<script lang="ts">
	import { GetIcons, keybindConn, keybindState } from '$lib';
</script>

<div class="flex flex-row items-center justify-between px-4 py-[13.5px]">
	<div class="flex flex-row items-center gap-4">
		<button
			class="h-fit cursor-pointer bg-transparent hover:bg-transparent"
			onclick={() => {
				keybindState.setLoadDispatcher('');
				keybindState.setDispatchers([]);
				keybindState.holdKeybinds.dispatcher = '';
				keybindConn.sendHelp('KEYBIND_HELP');
			}}
		>
			{@html GetIcons('arrow_left', 16)}
		</button>
		<p class="text-xs font-semibold">Choose A Dispatcher</p>
	</div>
</div>

<div class="divider m-0"></div>

<div class="flex max-h-[94vh] min-h-[94vh] flex-col gap-2 overflow-y-auto p-4">
	{#each keybindState.getDispatcher() as dispatchers}
		<button
			class="w-full cursor-pointer rounded-md p-4 text-start {keybindState.getEditKeyHold()
				?.dispatcher === dispatchers.command
				? 'bg-base-100 hover:bg-base-300 border'
				: 'bg-base-300/60 hover:bg-base-300'}"
			onclick={() => {
				if (keybindState.getEditKeyHold() !== null) {
					keybindState.setLoadDispatcher('edit');
					keybindConn.sendHelp('DISPATCHER_HELP', dispatchers.command);
					keybindState.editkeyHold!!.dispatcher = dispatchers.command;
				} else {
					keybindState.setLoadDispatcher(dispatchers.command);
					keybindConn.sendHelp('DISPATCHER_HELP', dispatchers.command);
					keybindState.holdKeybinds.dispatcher = dispatchers.command;
				}
			}}
		>
			<p class="text-sm font-semibold">{dispatchers.name}</p>
			<p class="text-base-content/60 text-xs font-medium">{dispatchers.description}</p>
		</button>
	{/each}
</div>
