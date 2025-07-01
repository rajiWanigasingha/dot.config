<script lang="ts">
	import { Sidebar, Main, uiStore, websocketConnection, updateChange } from '$lib';
	import Help from '$lib/components/Help.svelte';
	import { Toaster } from 'svelte-sonner';

	$effect(() => {
		if (uiStore.getSidebar().length === 0) {
			websocketConnection.connectToPage();
			websocketConnection.connectToMain();
			websocketConnection.connectToHelp();
		}
	});
</script>

<Toaster position="bottom-center" richColors/>

<div class="grid min-h-screen w-full grid-cols-12">
	<section class="bg-base-200/50 col-span-3 min-h-screen" id="sidebar">
		<Sidebar />
	</section>

	<section
		class="bg-base-200
		{updateChange.update !== null && updateChange.update !== false ? 'border-sweep-left-right' : ''}  
		{updateChange.update !== null && updateChange.update === false ? 'border-sweep-left-right-err' : ''}
		col-span-6 min-h-screen"
		id="sidebar"
	>
		<Main />
	</section>

	<section class="bg-base-300 col-span-3 min-h-screen" id="sidebar">
		<Help />
	</section>
</div>

<style>
	@keyframes smoothBorderSweep {
		0% {
			background-position: 0% -100%;
			opacity: 0;
		}
		10% {
			opacity: 1;
		}
		90% {
			opacity: 0.7;
		}
		100% {
			background-position: 0% 100%;
			opacity: 0;
		}
	}

	.border-sweep-left-right {
		position: relative;
	}

	.border-sweep-left-right::before,
	.border-sweep-left-right::after {
		content: '';
		position: absolute;
		top: 0;
		width: 2px;
		height: 100%;
		background-image: linear-gradient(to bottom, transparent, var(--color-success), transparent);
		background-repeat: no-repeat;
		background-size: 100% 300%;
		background-position: 0% -100%;
		opacity: 0;
		animation: smoothBorderSweep 2s cubic-bezier(0.22, 1.27, 1, 1.13) infinite;
	}

	.border-sweep-left-right::before {
		left: 0;
	}

	.border-sweep-left-right::after {
		right: 0;
	}

	.border-sweep-left-right-err {
		position: relative;
	}

	.border-sweep-left-right-err::before,
	.border-sweep-left-right-err::after {
		content: '';
		position: absolute;
		top: 0;
		width: 2px;
		height: 100%;
		background-image: linear-gradient(to bottom, transparent, var(--color-error), transparent);
		background-repeat: no-repeat;
		background-size: 100% 300%;
		background-position: 0% -100%;
		opacity: 0;
		animation: smoothBorderSweep 2s cubic-bezier(0.22, 1.27, 1, 1.13) infinite;
	}

	.border-sweep-left-right-err::before {
		left: 0;
	}

	.border-sweep-left-right-err::after {
		right: 0;
	}
</style>
