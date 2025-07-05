<script lang="ts">
	import { Sidebar } from '$lib';
	import type { Snippet } from 'svelte';

	let { children }: { children: Snippet } = $props();
</script>

<div class="grid min-h-screen w-full grid-cols-12">
	<section class="bg-base-200/50 col-span-3 min-h-screen" id="sidebar">
		<Sidebar />
	</section>
	<section class="col-span-9">
		{@render children()}
	</section>
</div>
