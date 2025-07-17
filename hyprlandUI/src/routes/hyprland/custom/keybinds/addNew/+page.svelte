<script lang="ts">
	import { goto } from '$app/navigation';
	import {
		Dispatcher,
		DispatcherCloseWindow,
		DispatcherExecute,
		DispatcherExecuteRaw,
		DispatcherFallback,
		DispatcherKillActive,
		DispatcherKillWindow,
		DispatcherMoveToWorkspace,
		DispatcherMoveToWorkspaceSilent,
		DispatcherPass,
		DispatcherSendKeyState,
		DispatcherSendShortcuts,
		DispatcherSetFloating,
		DispatcherSetTitled,
		DispatcherSignal,
		DispatcherSignalWindow,
		DispatcherToggleFloating,
		DispatcherWorkspace,
		HelpPage,
		keybindConn,
		keybindState,
		DispatcherTitle,
		type KeybindsLoad
	} from '$lib';
	import { Shortcut } from '$lib';
	import { onDestroy, onMount } from 'svelte';
	import { fade, slide } from 'svelte/transition';

	onMount(() => {
		if (keybindConn.wsKeybind === null) {
			console.log('Connect to websocket /keybinds');
			keybindConn.connection();
		}

		if (
			keybindState.help.page === '' &&
			keybindState.help.state !== null &&
			keybindConn.wsKeybind !== null
		) {
			keybindConn.sendHelp(keybindState.help.state);
		}
	});

	onDestroy(() => {
		console.log('Disconnect Keybinds');
		keybindConn.sendDisconnect();
	});

	function createNewBind() {
		if (keybindState.holdKeybinds.dispatcher !== null) {
			const newBind: KeybindsLoad = {
				flags: keybindState.holdKeybinds.flags,
				mod: keybindState.holdKeybinds.mod,
				keys: keybindState.holdKeybinds.key,
				description: '',
				dispatcher: keybindState.holdKeybinds.dispatcher,
				args: keybindState.holdKeybinds.args ?? ''
			};

			keybindConn.createNew(newBind);
		}
	}

</script>

<div class="grid max-h-screen min-h-screen w-full grid-cols-12 overflow-y-hidden">
	<div class="bg-base-200 col-span-7 min-h-screen">
		{#if keybindState.getLoadDispatcher() === ''}
			{#if keybindState.store.dispatchers.length === 0}
				<div transition:slide={{ duration: 300, delay: 50 }}>
					<Shortcut />
				</div>
			{:else}
				<div transition:slide={{ duration: 300, delay: 100 }}>
					<Dispatcher />
				</div>
			{/if}
		{:else if keybindState.getLoadDispatcher() === 'exec'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Add Args For Execute" />
				<DispatcherExecute />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'execr'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Add Args For Execute Raw" />
				<DispatcherExecuteRaw />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'pass'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Add Args For Pass Keys" />
				<DispatcherPass />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'sendshortcut'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Add Args For Send Shortcuts" />
				<DispatcherSendShortcuts />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'sendkeystate'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Add Args For Send Key State" />
				<DispatcherSendKeyState />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'killactive'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="No Args For kill Active" />
				<DispatcherKillActive />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'forcekillactive'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="No Args For Force kill Active" />
				<DispatcherKillActive />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'closewindow'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Close Given Window" />
				<DispatcherCloseWindow />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'killwindow'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Kill Given Window" />
				<DispatcherKillWindow />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'signal'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Send A Signal To Active Window" />
				<DispatcherSignal />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'signalwindow'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Send A Signal To Active Window" />
				<DispatcherSignalWindow />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'workspace'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Changing Workspaces" />
				<DispatcherWorkspace />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'movetoworkspace'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Moving Focus Window Into A Workspace" />
				<DispatcherMoveToWorkspace />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'movetoworkspacesilent'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle
					name="Args For Moving Focus Window Into A Workspace But Silent (Doesn't show it change)"
				/>
				<DispatcherMoveToWorkspaceSilent />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'togglefloating'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Toggle Floating Mode" />
				<DispatcherToggleFloating />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'setfloating'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Set Floating State" />
				<DispatcherSetFloating />
			</div>
		{:else if keybindState.getLoadDispatcher() === 'settiled'}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Args For Set Titled" />
				<DispatcherSetTitled />
			</div>
		{:else}
			<div transition:slide={{ duration: 300, delay: 100 }}>
				<DispatcherTitle name="Add Argument for dispatchers" />
				<DispatcherFallback />
			</div>
		{/if}
	</div>

	<div class="bg-base-300 col-span-5 min-h-screen">
		<HelpPage />
	</div>
</div>

{#if keybindState.getSummery()}
	<dialog class="modal" open={true}>
		<div class="modal-box bg-base-300 max-w-5xl rounded-md">
			<h3 class="text-sm font-bold">Keybind Summery</h3>
			<p class="text-base-content/60 py-1 text-xs font-medium">
				This is a summery of the keybind that will create
			</p>
			<div class="p-2">
				<div class="overflow-x-auto">
					<table class="table-zebra border-base-content/10 table border text-xs">
						<tbody>
							<tr>
								<th class="w-32">Mod Key</th>
								<td>
									<div>
										{#each keybindState.getHoldKeybinds().mod as mod}
											<kbd class="kbd kbd-sm">{mod}</kbd>
										{/each}
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-32">Keys</th>
								<td>
									<div>
										{#each keybindState.getHoldKeybinds().key as key}
											<kbd class="kbd kbd-sm">{key}</kbd>
										{/each}
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-32">Flags</th>
								<td>
									<div>
										{#each keybindState.getHoldKeybinds().flags as flag}
											<div class="badge badge-sm text-xs">{flag}</div>
										{/each}
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-32">Dispatcher</th>
								<td>
									<div>
										<div class="badge badge-sm text-xs">
											{keybindState.getHoldKeybinds().dispatcher}
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<th class="w-32">Arguments</th>
								<td>
									<div>
										<div class="text-xs">{keybindState.getHoldKeybinds().args}</div>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-action flex flex-row justify-between">
				<form method="dialog">
					<button
						class="btn btn-error text-error-content text-xs"
						onclick={() => keybindState.setSummery(false)}>Discard This</button
					>
				</form>
				<button class="btn btn-success text-success-content text-xs" onclick={() => createNewBind()}
					>Create Bind</button
				>
			</div>
		</div>
	</dialog>
{/if}
