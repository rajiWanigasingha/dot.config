<script lang="ts">
	import { keybindState } from '$lib';
	import { marked } from 'marked';
	import { tick } from 'svelte';

	let markdown = $state(null as null | HTMLDivElement);

	$effect(() => {
		if (markdown && keybindState.help.page !== '') {
			tick();
			Array.from(markdown.children).forEach((item) => {
				switch (item.tagName) {
					case 'H3': {
						item.className = 'text-sm font-semibold';
						break;
					}

					case 'H4': {
						item.className = 'text-sm font-medium border-t-[1px] pt-4 border-t-base-content/10';
						break;
					}

					case 'H5': {
						item.className = 'text-xs font-medium text-base-content/60 border-t-[1px] pt-4';
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
								});
							} else if (a.tagName === 'CODE') {
								a.className = 'badge badge-sm';
							}
						});

						break;
					}

					case 'PRE': {
						item.className = 'bg-base-200 p-4 rounded-md text-wrap';
						item.children[0].className = 'text-xs font-light';
						break;
					}

					case 'TABLE': {
						item.className = 'table table-sm table-zebra';
						Array.from(item.children[0].children[0].children).forEach(
							(elem) => (elem.className = 'text-xs')
						);
						break;
					}

					case 'UL': {
						item.className = 'flex flex-col gap-2';
						Array.from(item.children).forEach((li) => {
							li.className = 'text-xs text-light';

							Array.from(li.children).forEach((elem) => {
								if (elem.tagName === 'CODE') {
									elem.className = 'badge badge-sm';
								}

								if (elem.tagName === 'P') {
									item.className = 'text-xs font-light';
								}

								if (elem.tagName === 'UL') {
									elem.className = 'flex flex-col gap-1 ml-3';
									Array.from(elem.children).forEach((li) => {
										li.className = 'text-xs text-light';

										Array.from(li.children).forEach((elem) => {
											if (elem.tagName === 'UL') {
												elem.className = `flex flex-col gap-1 ml-3`;
											}

											if (elem.tagName === 'CODE') {
												elem.className = 'badge badge-sm';
											}

											if (elem.tagName === 'P') {
												item.className = 'text-xs font-light';
											}
										});
									});
								}
							});
						});
						break;
					}
				}
			});
		}
	});
</script>

<div class="flex flex-row items-center justify-between px-4 py-[13.5px]">
	<p class="text-xs font-semibold">Help Create Keybind</p>
</div>

<div class="divider m-0"></div>

<div class="flex max-h-[94vh] min-h-[94vh] flex-col gap-3 overflow-y-auto p-4" bind:this={markdown}>
	{@html marked(keybindState.help.page)}
</div>
