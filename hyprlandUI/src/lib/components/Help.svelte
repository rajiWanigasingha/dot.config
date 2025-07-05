<script lang="ts">
	import { helpState, mainPageState, sidebarState, type MainPageActionInputData } from '$lib';
	import { marked } from 'marked';
	import { tick } from 'svelte';
	import { open } from '@tauri-apps/plugin-shell';
	import { toast } from 'svelte-sonner';

	let markdown = $state(null as null | HTMLDivElement);

	function scrollIntoView(target: EventTarget & HTMLAnchorElement, data: MainPageActionInputData) {
		const targetLink = target.getAttribute('href');

		if (targetLink === null) {
			toast.warning("Sorry, Couldn't Find This Settings");
			return;
		}

		const el = document.querySelector(targetLink) as HTMLDivElement;

		if (!el) {
			toast.warning("Sorry, Couldn't Find This Settings");
			return;
		}

		el.scrollIntoView({
			behavior: 'smooth',
			block: 'center',
			inline: 'nearest'
		});

		helpState.setActiveHelp(data.settingsName, data);
		setTimeout(() => helpState.setActiveHelp(null, data), 1600);
	}

	function openLink(link: string) {
		open(link);
		toast.success('This Link Open In Your Browser');
	}

	$effect(() => {
		if (markdown && helpState.helpstate.helpMd !== null) {
			tick();
			Array.from(markdown.children).forEach((item) => {
				switch (item.tagName) {
					case 'H4': {
						item.className = 'text-sm';
						break;
					}

					case 'P': {
						item.className = 'text-xs font-light';

						Array.from(item.children).forEach((a) => {
							if (a.tagName === 'A') {
								a.className = 'text-blue-200 hover:text-blue-300';
								const link = (a as HTMLAnchorElement).href;
								a.addEventListener('click', (e) => {
									e.preventDefault();
									openLink(link);
								});
							}
						});

						break;
					}

					case 'PRE': {
						item.className = '';
						item.children[0].className = 'text-xs font-light';
						break;
					}

					case 'UL': {
						Array.from(item.children).forEach((li) => {
							li.className = 'text-xs text-light';
						});
					}
				}
			});
		}
	});
</script>

<div>
	<div class="flex flex-row items-center justify-between px-2 py-1">
		<div class="flex flex-row items-center gap-1">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle">
				<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
					><path
						fill="currentColor"
						d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10s10-4.48 10-10S17.52 2 12 2m1 17h-2v-2h2zm2.07-7.75l-.9.92C13.45 12.9 13 13.5 13 15h-2v-.5c0-1.1.45-2.1 1.17-2.83l1.24-1.26c.37-.36.59-.86.59-1.41c0-1.1-.9-2-2-2s-2 .9-2 2H8c0-2.21 1.79-4 4-4s4 1.79 4 4c0 .88-.36 1.68-.93 2.25"
					/></svg
				>
			</button>
			<p class="text-xs font-semibold capitalize">
				{sidebarState.sidebarState.sidebarActive.toLocaleLowerCase().replaceAll('_', ' ')} Help
			</p>
		</div>
		<div>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-sm btn-circle btn-soft">
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
					><path fill="currentColor" d="M6 13v-2h12v2z" /></svg
				>
			</button>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-sm btn-circle btn-soft">
				<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24"
					><path
						fill="currentColor"
						d="M20 15v-5q0-1.25-.875-2.125T17 7H6V4q0-.825.588-1.412T8 2h12q.825 0 1.413.588T22 4v9q0 .825-.587 1.413T20 15M4 22q-.825 0-1.412-.587T2 20v-9q0-.825.588-1.412T4 9h12q.825 0 1.413.588T18 11v9q0 .825-.587 1.413T16 22z"
					/></svg
				>
			</button>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-sm btn-circle btn-soft">
				<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
					><path
						fill="currentColor"
						d="m8.382 17.025l-1.407-1.4L10.593 12L6.975 8.4L8.382 7L12 10.615L15.593 7L17 8.4L13.382 12L17 15.625l-1.407 1.4L12 13.41z"
					/></svg
				>
			</button>
		</div>
	</div>
	<div class="divider m-0"></div>
	<div class="max-h-[94vh] overflow-y-auto">
		{#if !helpState.helpstate.show}
			<ul class="menu rounded-box w-full">
				{#each mainPageState.mainInputState.mainPageData as helpTags}
					<li>
						<h2 class="menu-title capitalize">{helpTags.tab}</h2>
						<ul>
							{#each helpTags.settings as setting}
								<!-- svelte-ignore a11y_invalid_attribute -->
								<li>
									<a
										href="#{setting.data.settingsName}"
										onclick={(e) => {
											e.preventDefault();
											if (helpTags.tab !== mainPageState.get().tab) {
												mainPageState.setTabs(helpTags.tab);
											}
											const target = e.currentTarget;
											setTimeout(() => {
												scrollIntoView(target, setting.data);
											}, 200);
										}}
									>
										<p class="text-xs">{setting.data.name}</p>
									</a>
								</li>
							{/each}
						</ul>
					</li>
				{/each}
			</ul>
		{:else}
			<div class="flex flex-col gap-2">
				<div class="flex flex-row items-start gap-3 px-4 pt-2">
					<!-- svelte-ignore a11y_consider_explicit_label -->
					<button class="cursor-pointer" onclick={() => helpState.setShow(false)}>
						<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
							><path fill="currentColor" d="m9 18l-6-6l6-6l1.4 1.4L6.8 11H21v2H6.8l3.6 3.6z" /></svg
						>
					</button>
					<div class="flex flex-col gap-1">
						<p class="text-sm font-semibold">{helpState.getActiveHelp()?.name}</p>
						<p class="text-base-content/60 text-xs font-medium">
							Hyprland Settings <span class="font-bold"
								>{helpState.getActiveHelp()?.category}:{helpState.getActiveHelp()
									?.settingsName}</span
							>
						</p>
					</div>
				</div>
				<div class="divider m-0"></div>
				<div class="prose prose-invert max-w-none px-4" bind:this={markdown}>
					{@html marked(helpState.helpstate?.helpMd ?? '')}
				</div>
			</div>
		{/if}
	</div>
</div>
