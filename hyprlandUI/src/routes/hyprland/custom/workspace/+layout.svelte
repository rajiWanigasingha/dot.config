<script lang="ts">
	import { onDestroy } from 'svelte';
	import { workspaceConn, workspaceState } from '$lib';
	import type { Snippet } from 'svelte';

	let { children }: { children: Snippet } = $props();

	$effect(() => {
		if (workspaceConn.wsWorkspace === null) {
			workspaceConn.connect();
		}
		workspaceState.ui.pageChange = false;
		workspaceState.ui.newUIData = null;
	});

	onDestroy(() => {
		workspaceConn.desconnect();
	});
</script>

{@render children()}
