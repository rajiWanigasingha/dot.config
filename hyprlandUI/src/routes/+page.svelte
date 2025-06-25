<script lang="ts">
	import { Sidebar, Main, uiStore, websocketConnection } from '$lib';
	import Help from '$lib/components/Help.svelte';

	$effect(() => {
		if(uiStore.getSidebar().length === 0) {
			websocketConnection.connectToPage()
			websocketConnection.connectToMain()
		}
	})
</script>

<div class="grid min-h-screen w-full grid-cols-12">
	<section class="bg-base-100 col-span-3 min-h-screen" id="sidebar">
        <Sidebar />
    </section>

	<section class="bg-base-200 col-span-6 min-h-screen" id="sidebar">
        <Main />
    </section>

	<section class="bg-base-300 col-span-3 min-h-screen" id="sidebar">
        <Help />
    </section>
</div>
