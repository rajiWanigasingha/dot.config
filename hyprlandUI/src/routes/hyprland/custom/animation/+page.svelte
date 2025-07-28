<script lang="ts">
	import {
		animationConn,
		animationState,
		AnimationTreeAddNew,
		AnimationTreeCurve,
		AnimationTreeEdit,
		GetIcons,
		SidebarIcon
	} from '$lib';
	import { onDestroy } from 'svelte';

	let showAnimation = $state('windows');
	let animationStyle = $state(['slide', 'popin', 'gnomed'] as string[]);

	type AnimationNode = {
		name: string;
		des: string;
		icon?: string;
		styles?: string[];
		child?: AnimationNode[];
	};

	const animationTree: AnimationNode[] = [
		{
			name: 'Global',
			des: 'Global Animation Settings',
			icon: '',
			child: [
				{
					name: 'Windows',
					des: 'Animate all window actions',
					icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M20 15v-5q0-1.25-.875-2.125T17 7H6V4q0-.825.588-1.412T8 2h12q.825 0 1.413.588T22 4v9q0 .825-.587 1.413T20 15M4 22q-.825 0-1.412-.587T2 20v-9q0-.825.588-1.412T4 9h12q.825 0 1.413.588T18 11v9q0 .825-.587 1.413T16 22z"/></svg>`,
					styles: ['slide', 'popin', 'gnomed'],
					child: [
						{
							name: 'Windows In',
							des: 'Window open animation',
							icon: '',
							styles: ['slide', 'popin', 'gnomed']
						},
						{
							name: 'Windows Out',
							des: 'Window close animation',
							icon: '',
							styles: ['slide', 'popin', 'gnomed']
						},
						{
							name: 'Windows Move',
							des: 'Move/drag/resize animations',
							icon: ''
						}
					]
				},
				{
					name: 'Layers',
					des: 'Animate layers',
					icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="m12 21.05l-9-7l1.65-1.25L12 18.5l7.35-5.7L21 14.05zM12 16L3 9l9-7l9 7z"/></svg>`,
					styles: ['slide', 'popin', 'fade'],
					child: [
						{
							name: 'Layers In',
							des: 'Layer open animation',
							icon: ''
						},
						{
							name: 'Layers Out',
							des: 'Layer close animation',
							icon: ''
						}
					]
				},
				{
					name: 'Fade',
					des: 'Fade-related animations',
					icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M5 21q-.825 0-1.412-.587T3 19V5q0-.825.588-1.412T5 3h5v18zm7 0q-.425 0-.712-.288T11 20t.288-.712T12 19t.713.288T13 20t-.288.713T12 21m0-4q-.425 0-.712-.288T11 16t.288-.712T12 15t.713.288T13 16t-.288.713T12 17m0-4q-.425 0-.712-.288T11 12t.288-.712T12 11t.713.288T13 12t-.288.713T12 13m0-4q-.425 0-.712-.288T11 8t.288-.712T12 7t.713.288T13 8t-.288.713T12 9m0-4q-.425 0-.712-.288T11 4t.288-.712T12 3t.713.288T13 4t-.288.713T12 5m2 14q-.425 0-.712-.288T13 18t.288-.712T14 17t.713.288T15 18t-.288.713T14 19m0-4q-.425 0-.712-.288T13 14t.288-.712T14 13t.713.288T15 14t-.288.713T14 15m0-4q-.425 0-.712-.288T13 10t.288-.712T14 9t.713.288T15 10t-.288.713T14 11m0-4q-.425 0-.712-.288T13 6t.288-.712T14 5t.713.288T15 6t-.288.713T14 7m2 14q-.425 0-.712-.288T15 20t.288-.712T16 19t.713.288T17 20t-.288.713T16 21m0-4q-.425 0-.712-.288T15 16t.288-.712T16 15t.713.288T17 16t-.288.713T16 17m0-4q-.425 0-.712-.288T15 12t.288-.712T16 11t.713.288T17 12t-.288.713T16 13m0-4q-.425 0-.712-.288T15 8t.288-.712T16 7t.713.288T17 8t-.288.713T16 9m0-4q-.425 0-.712-.288T15 4t.288-.712T16 3t.713.288T17 4t-.288.713T16 5m2 14q-.425 0-.712-.288T17 18t.288-.712T18 17t.713.288T19 18t-.288.713T18 19m0-4q-.425 0-.712-.288T17 14t.288-.712T18 13t.713.288T19 14t-.288.713T18 15m0-4q-.425 0-.712-.288T17 10t.288-.712T18 9t.713.288T19 10t-.288.713T18 11m0-4q-.425 0-.712-.288T17 6t.288-.712T18 5t.713.288T19 6t-.288.713T18 7m2 14q-.425 0-.712-.288T19 20t.288-.712T20 19t.713.288T21 20t-.288.713T20 21m0-4q-.425 0-.712-.288T19 16t.288-.712T20 15t.713.288T21 16t-.288.713T20 17m0-4q-.425 0-.712-.288T19 12t.288-.712T20 11t.713.288T21 12t-.288.713T20 13m0-4q-.425 0-.712-.288T19 8t.288-.712T20 7t.713.288T21 8t-.288.713T20 9m0-4q-.425 0-.712-.288T19 4t.288-.712T20 3t.713.288T21 4t-.288.713T20 5"/></svg>`,
					child: [
						{
							name: 'Fade In',
							des: 'Fade in for window open',
							icon: ''
						},
						{
							name: 'Fade Out',
							des: 'Fade out for window close',
							icon: ''
						},
						{
							name: 'Fade Switch',
							des: 'Fade on active window change',
							icon: ''
						},
						{
							name: 'Fade Shadow',
							des: 'Fade shadow effect on window switch',
							icon: ''
						},
						{
							name: 'Fade Dim',
							des: 'Dim inactive windows',
							icon: ''
						},
						{
							name: 'Fade Layers',
							des: 'Fade animation for layers',
							icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="m12 21.05l-9-7l1.65-1.25L12 18.5l7.35-5.7L21 14.05zM12 16L3 9l9-7l9 7z"/></svg>`,
							child: [
								{
									name: 'Fade Layers In',
									des: 'Fade in when layer opens',
									icon: ''
								},
								{
									name: 'Fade Layers Out',
									des: 'Fade out when layer closes',
									icon: ''
								}
							]
						}
					]
				},
				{
					name: 'Border',
					des: 'Animate border color transitions',
					icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M5 19h14V5H5zm-2 2V3h18v18zm4-8v-2h2v2zm4 4v-2h2v2zm0-4v-2h2v2zm0-4V7h2v2zm4 4v-2h2v2z"/></svg>`
				},
				{
					name: 'Border Angle',
					des: 'Animate border gradient angle',
					icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M5 21q-.825 0-1.412-.587T3 19V5q0-.825.588-1.412T5 3h14q.825 0 1.413.588T21 5v14q0 .825-.587 1.413T19 21zm6-10v2h2v-2zm-4 0v2h2v-2zm2 2v2h2v-2zm4 0v2h2v-2zm-8 0v2h2v-2zm10-2v2h2v2h2v-2h-2v-2zm-8 4v2H5v2h2v-2h2v2h2v-2h2v2h2v-2h2v2h2v-2h-2v-2h-2v2h-2v-2h-2v2H9v-2zm12-4v2zm0 4v2z"/></svg>`,
					styles: ['once', 'loop']
				},
				{
					name: 'Workspaces',
					des: 'Animate workspace switching',
					icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M8 18q-.825 0-1.412-.587T6 16v-2h2v2h12V6H8v2H6V4q0-.825.588-1.412T8 2h12q.825 0 1.413.588T22 4v12q0 .825-.587 1.413T20 18zm-4 4q-.825 0-1.412-.587T2 20V6h2v14h14v2zm9-7l-1.4-1.4l1.575-1.6H6v-2h7.175L11.6 8.4L13 7l4 4z"/></svg>`,
					styles: ['slide', 'slidevert', 'fade', 'slidefade', 'slidefadevert'],
					child: [
						{
							name: 'Workspaces In',
							des: 'Workspace enter animation',
							icon: '',
							styles: ['slide', 'slidevert', 'fade', 'slidefade', 'slidefadevert']
						},
						{
							name: 'Workspaces Out',
							des: 'Workspace exit animation',
							icon: '',
							styles: ['slide', 'slidevert', 'fade', 'slidefade', 'slidefadevert']
						},
						{
							name: 'Special Workspace',
							des: 'Animation for special workspace',
							icon: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"><path fill="currentColor" d="M8 18q-.825 0-1.412-.587T6 16V4q0-.825.588-1.412T8 2h12q.825 0 1.413.588T22 4v12q0 .825-.587 1.413T20 18zm-4 4q-.825 0-1.412-.587T2 20V6h2v14h14v2zm9-14h7V4h-7z"/></svg>`,
							styles: ['slide', 'slidevert', 'fade', 'slidefade', 'slidefadevert'],
							child: [
								{
									name: 'Special Workspace In',
									des: 'Special workspace enter animation',
									icon: '',
									styles: ['slide', 'slidevert', 'fade', 'slidefade', 'slidefadevert']
								},
								{
									name: 'Special Workspace Out',
									des: 'Special workspace exit animation',
									icon: '',
									styles: ['slide', 'slidevert', 'fade', 'slidefade', 'slidefadevert']
								}
							]
						}
					]
				}
			]
		}
	];

	function breakAndCapitalize(word: string): string {
		return word.replace(/([a-z])([A-Z])/g, '$1 $2');
	}

	$effect(() => {
		if (animationConn.wsAnimation === null) {
			animationConn.connect();
		}
	});

	onDestroy(() => {
		if (animationConn.wsAnimation !== null) {
			animationConn.disconnect();
		}
	});
</script>

<AnimationTreeAddNew />
<AnimationTreeEdit />
<AnimationTreeCurve />

<div class="grid w-full grid-cols-12">
	<div class="bg-base-200 col-span-8 max-h-screen min-h-screen overflow-y-auto">
		<div class="flex flex-row items-center justify-between px-4 py-1">
			<p class="text-xs font-semibold">Animation Settings</p>
			<div class="flex flex-row gap-4">
				<div class="tooltip tooltip-bottom">
					<div class="tooltip-content">
						<p class="text-xs">Edit Or Add Bezier Curve</p>
					</div>
					<button
						class="btn btn-circle btn-soft"
						onclick={() => (animationState.ui.openCurve = true)}
					>
						{@html GetIcons('curve')}
					</button>
				</div>
				<button
					class="btn btn-circle btn-soft"
					onclick={() => {
						animationState.ui.openNew = true;
						animationState.ui.animation = breakAndCapitalize(showAnimation);
						animationState.ui.styles = animationStyle;
					}}
				>
					{@html GetIcons('add')}
				</button>
			</div>
		</div>
		<div class="divider m-0"></div>
		<div class="p-8">
			{#if animationState.getAnimation().some((anim) => anim.name.toLowerCase() === showAnimation)}
				{#each animationState.getAnimation() as animation}
					{#if animation.name.toLowerCase() === showAnimation}
						<div class="bg-base-300/60 flex w-full flex-col gap-4 p-4">
							<div class="flex flex-row items-center justify-between">
								<p class="text-xs font-semibold capitalize">{breakAndCapitalize(animation.name)}</p>
								<label class="toggle toggle-md text-base-conten">
									<input
										type="checkbox"
										class="hidden"
										checked={animation.onOff === 1 ? true : false}
										onclick={(e) =>
											animationConn.enableOrDisable({
												...animation,
												onOff: e.currentTarget.checked ? 1 : 0
											})}
									/>
									<svg
										aria-label="enabled"
										xmlns="http://www.w3.org/2000/svg"
										viewBox="0 0 24 24"
										fill="none"
										stroke="currentColor"
										stroke-width="4"
										stroke-linecap="round"
										stroke-linejoin="round"
									>
										<path d="M18 6 6 18" />
										<path d="m6 6 12 12" />
									</svg>
									<svg aria-label="disabled" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
										<g
											stroke-linejoin="round"
											stroke-linecap="round"
											stroke-width="4"
											fill="none"
											stroke="currentColor"
										>
											<path d="M20 6 9 17l-5-5"></path>
										</g>
									</svg>
								</label>
							</div>
							<div>
								<table class="table">
									<thead>
										<tr class="bg-base-100">
											<th class="text-xs font-medium">Speed</th>
											<th class="text-xs font-medium">Curve</th>
											<th class="text-xs font-medium">Styles</th>
										</tr>
									</thead>
									<tbody>
										<tr class="bg-base-200">
											<td class="text-xs">{Number(animation.speed) * 100} Ms</td>
											<td class="text-xs">{animation.curve}</td>
											<td class="text-xs">{animation.style}</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="flex flex-row justify-end gap-4">
								<button
									class="btn btn-circle btn-sm btn-warning"
									onclick={() => {
										animationState.ui.edit = animation;
										animationState.ui.openEdit = true;
									}}>{@html GetIcons('edit', 16)}</button
								>
								<button
									class="btn btn-circle btn-sm btn-error"
									onclick={() => {
										animationConn.delete(animation);
									}}>{@html GetIcons('delete', 16)}</button
								>
							</div>
						</div>
					{/if}
				{/each}
			{:else}
				<div class="flex w-full flex-col items-center justify-center gap-3">
					{@html SidebarIcon('animationTree', 64)}
					<p class="text-sm font-medium">
						No Animation Found In This Part Of The Animation Tree. Try To Add New One
					</p>
				</div>
			{/if}
		</div>
	</div>
	<div class="bg-base-300 col-span-4 max-h-screen min-h-screen overflow-y-auto">
		<div class="flex flex-row items-center justify-between px-4 py-[13.5px]">
			<p class="text-xs font-semibold">Animation Tree</p>
		</div>
		<div class="divider m-0"></div>
		<div>
			<ul class="menu w-full py-4">
				{#each animationTree as tree}
					<li>
						<p class="text-base-content/60 text-xs font-semibold">{tree.name}</p>
						{#if tree.child !== undefined}
							<ul>
								{#each tree.child as titles}
									<li>
										<button
											onclick={() => {
												showAnimation = titles.name.replaceAll(' ', '').toLowerCase();
												animationStyle = titles.styles ?? [];
												animationState.ui.animationName =
													titles.name.replaceAll(' ', '').charAt(0).toLowerCase() +
													titles.name.replaceAll(' ', '').slice(1);
											}}
											class={titles.name.replaceAll(' ', '').toLowerCase() === showAnimation
												? 'menu-active'
												: ''}
										>
											{@html titles.icon}
											<p class="text-xs font-semibold">{titles.name}</p>
										</button>
										{#if titles.child !== undefined}
											<ul>
												{#each titles.child as subtitle}
													<li>
														<button
															onclick={() => {
																showAnimation = subtitle.name.replaceAll(' ', '').toLowerCase();
																animationStyle = subtitle.styles ?? [];
																animationState.ui.animationName =
																	subtitle.name.replaceAll(' ', '').charAt(0).toLowerCase() +
																	subtitle.name.replaceAll(' ', '').slice(1);
															}}
															class={subtitle.name.replaceAll(' ', '').toLowerCase() ===
															showAnimation
																? 'menu-active'
																: ''}
														>
															{@html subtitle.icon}
															<div class="flex flex-col">
																<p class="text-xs font-medium">{subtitle.name}</p>
																<p class="text-base-content/60 text-xs font-light">
																	{subtitle.des}
																</p>
															</div>
														</button>
													</li>
													{#if subtitle.child !== undefined}
														<ul>
															{#each subtitle.child as last}
																<li>
																	<button
																		onclick={() => {
																			showAnimation = last.name.replaceAll(' ', '').toLowerCase();
																			animationStyle = last.styles ?? [];
																			animationState.ui.animationName =
																				last.name.replaceAll(' ', '').charAt(0).toLowerCase() +
																				last.name.replaceAll(' ', '').slice(1);
																		}}
																		class={last.name.replaceAll(' ', '').toLowerCase() ===
																		showAnimation
																			? 'menu-active'
																			: ''}
																	>
																		{@html last.icon}
																		<div class="flex flex-col">
																			<p class="text-xs font-medium">{last.name}</p>
																			<p class="text-base-content/60 text-xs font-light">
																				{last.des}
																			</p>
																		</div>
																	</button>
																</li>
															{/each}
														</ul>
													{/if}
												{/each}
											</ul>
										{/if}
									</li>
								{/each}
							</ul>
						{/if}
					</li>
				{/each}
			</ul>
		</div>
	</div>
</div>
