<script lang="ts">
	import { workspaceState } from '$lib';
</script>

<dialog id="my_modal_2" class="modal" open={workspaceState.getUiAddNew()}>
	<div class="modal-box bg-base-300 min-w-5xl">
		<div>
			<h3 class="text-xs font-semibold">Add Workspace Rules</h3>
			{#if workspaceState.ui.addTo === 'edit'}
				<p class="text-base-content/60 text-xs font-light">
					In Edit Mode When Adding A New Rule Doesn't Effect Until You Change It Values
				</p>
			{/if}
		</div>
		<div class="divider m-2"></div>
		<div class="flex flex-col gap-2">
			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, monitor: '' }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, monitor: '' };
						} else {
							workspaceState.ui.newUIData = { monitor: '' };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Monitor</p>
				<p class="text-base-content/60 text-xs">Binds a workspace to a monitor.</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, default: false }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, default: false };
						} else {
							workspaceState.ui.newUIData = { default: false };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Default</p>
				<p class="text-base-content/60 text-xs">
					Whether this workspace should be the default workspace for the given monitor.
				</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, gapsIn: 0 }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, gapsIn: 0 };
						} else {
							workspaceState.ui.newUIData = { gapsIn: 0 };
						}
					}
					workspaceState.ui.addnewRules = false;
				}}
			>
				<p class="text-xs font-medium">Gaps In</p>
				<p class="text-base-content/60 text-xs">
					Set the gaps between windows (equivalent to General → gaps_in).
				</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, gapsOut: 0 }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, gapsOut: 0 };
						} else {
							workspaceState.ui.newUIData = { gapsOut: 0 };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Gaps Out</p>
				<p class="text-base-content/60 text-xs">
					Set the gaps between windows and monitor edges (equivalent to General → gaps_out).
				</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, borderSize: 0 }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, borderSize: 0 };
						} else {
							workspaceState.ui.newUIData = { borderSize: 0 };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Border Size</p>
				<p class="text-base-content/60 text-xs">
					Set the border size around windows (equivalent to General → border_size).
				</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, border: true }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, border: true };
						} else {
							workspaceState.ui.newUIData = { border: true };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Border</p>
				<p class="text-base-content/60 text-xs">Whether to draw borders or not.</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, shadow: true }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, shadow: true };
						} else {
							workspaceState.ui.newUIData = { shadow: true };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Shadow</p>
				<p class="text-base-content/60 text-xs">Whether to draw shadows or not.</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, rounding: true }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, rounding: true };
						} else {
							workspaceState.ui.newUIData = { rounding: true };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Rounding</p>
				<p class="text-base-content/60 text-xs">Whether to draw rounded windows or not.</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, decorate: true }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, decorate: true };
						} else {
							workspaceState.ui.newUIData = { decorate: true };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Decorate</p>
				<p class="text-base-content/60 text-xs">Whether to draw window decorations or not.</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, persistent: true }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, persistent: true };
						} else {
							workspaceState.ui.newUIData = { persistent: true };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Persistent</p>
				<p class="text-base-content/60 text-xs">
					Keep this workspace alive even if empty and inactive.
				</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, onCreatedEmpty: '' }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, onCreatedEmpty: '' };
						} else {
							workspaceState.ui.newUIData = { onCreatedEmpty: '' };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">On Create Empty</p>
				<p class="text-base-content/60 text-xs">
					A command to be executed once a workspace is created empty.
				</p>
			</button>

			<button
				class="hover:bg-base-100 bg-base-200 cursor-pointer p-4 text-start"
				onclick={() => {
					if (workspaceState.ui.addTo === 'edit') {
						workspaceState.setNewEdit({
							name: workspaceState.ui.editName!!.name,
							rules: { ...workspaceState.ui.editName?.rules, defaultName: '' }
						});
					} else {
						if (workspaceState.ui.newUIData !== null) {
							workspaceState.ui.newUIData = { ...workspaceState.ui.newUIData, defaultName: '' };
						} else {
							workspaceState.ui.newUIData = { defaultName: '' };
						}
						workspaceState.ui.addnewRules = false;
					}
				}}
			>
				<p class="text-xs font-medium">Default Name</p>
				<p class="text-base-content/60 text-xs">A default name for the workspace.</p>
			</button>
		</div>
	</div>
	<form method="dialog" class="modal-backdrop">
		<button onclick={() => workspaceState.setUiAddNew(false, null)}>close</button>
	</form>
</dialog>
