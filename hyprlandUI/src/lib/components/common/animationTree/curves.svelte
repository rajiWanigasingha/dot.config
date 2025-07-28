<script lang="ts">
	import { animationConn, animationState, GetIcons, type Curve } from '$lib';
	import { toast } from 'svelte-sonner';
	import { fade, fly } from 'svelte/transition';

	let action = $state('');

	let name = $state('');
	let x0 = $state(null as null | number);
	let y0 = $state(null as null | number);
	let x1 = $state(null as null | number);
	let y1 = $state(null as null | number);
	let oldCurve = $state(null as null | Curve);

	let deleteName = $state('');
	let deleteCurve = $state(null as null | Curve);

	function addNewCurve() {
		let curve = false;

		animationState.getCurves().forEach((item) => {
			if (item.name === name) {
				curve = true;
			}
		});

		if (curve) {
			toast.error('Curve With This Name Already Exist');
			return;
		}

		if (name === '') {
			toast.error('Name Must Not Be Empty.');
			return;
		}

		if (x0 === null || x1 === null || y0 === null || y1 === null) {
			toast.error('Input Field Is Empty');
			return;
		}

		const curveNew: Curve = {
			name: name,
			x0: x0!!.toString(),
			y0: y0!!.toString(),
			x1: x1!!.toString(),
			y1: y1!!.toString()
		};

		animationConn.addNewCurve(curveNew);

		action = '';
		animationState.ui.openCurve = false;
	}

	function editCurve() {
		let curve = false;

		animationState.getCurves().forEach((item) => {
			if (item.name === name) {
				curve = true;
			}
		});

		if (curve) {
			toast.error('Curve With This Name Already Exist');
			return;
		}

		if (name === '') {
			toast.error('Name Must Not Be Empty.');
			return;
		}

		if (x0 === null || x1 === null || y0 === null || y1 === null) {
			toast.error('Input Field Is Empty');
			return;
		}

		const curveNew: Curve = {
			name: name,
			x0: x0!!.toString(),
			y0: y0!!.toString(),
			x1: x1!!.toString(),
			y1: y1!!.toString()
		};

		if (oldCurve === null) {
			return;
		}

		animationConn.editCurve(curveNew, oldCurve);

		action = '';
		animationState.ui.openCurve = false;
	}
</script>

{#if animationState.ui.openCurve}
	<div class="bg-base-200/60 fixed inset-0 z-30" transition:fade={{ duration: 200 }}>
		<!-- svelte-ignore a11y_click_events_have_key_events -->
		<!-- svelte-ignore a11y_no_static_element_interactions -->
		<div
			class="min-h-screen w-full"
			onclick={() => {
				animationState.ui.openCurve = false;
				action = '';
			}}
		></div>

		{#if action === 'add'}
			<div
				transition:fly={{ x: 500, duration: 200 }}
				class="bg-base-100 border-r-base-content/10 fixed top-0 right-[600px] min-h-screen w-[500px] border-r-[0.5px]"
			>
				<div class="p-8">
					<div>
						<p class="text-xs font-medium">Add New Curve</p>
						<p class="text-base-content/60 text-xs font-light">Create New Bezier Curve</p>
						<div class="divider m-2"></div>
					</div>
					<div class="flex flex-col gap-3">
						<label class="input w-full text-xs">
							<span class="label">Curve Name</span>
							<input
								type="text"
								class="text-xs focus:ring-0"
								placeholder="liner"
								bind:value={name}
								oninput={(e) => (name = e.currentTarget.value)}
							/>
						</label>
						<div class="flex flex-row gap-2">
							<label class="input w-full text-xs">
								<span class="label">X0</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={x0}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											x0 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											x0 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
							<label class="input w-full text-xs">
								<span class="label">Y0</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={y0}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											y0 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											y0 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
						</div>
						<div class="flex flex-row gap-2">
							<label class="input w-full text-xs">
								<span class="label">X1</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={x1}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											x1 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											x1 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
							<label class="input w-full text-xs">
								<span class="label">Y1</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={y1}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											y1 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											y1 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
						</div>
						<div class="flex flex-col gap-2">
							<button class="btn btn-success w-full text-xs" onclick={() => addNewCurve()}
								>Create New Curve</button
							>
							<button class="btn btn-primary text-xs" onclick={() => (action = '')}>Close</button>
						</div>
					</div>
				</div>
			</div>
		{:else if action === 'edit'}
			<div
				transition:fly={{ x: 500, duration: 200 }}
				class="bg-base-100 border-r-base-content/10 fixed top-0 right-[600px] min-h-screen w-[500px] border-r-[0.5px]"
			>
				<div class="p-8">
					<div>
						<p class="text-xs font-medium">Edit Curve</p>
						<p class="text-base-content/60 text-xs font-light">Edit Bezier Curve</p>
						<div class="divider m-2"></div>
					</div>
					<div class="flex flex-col gap-3">
						<label class="input w-full text-xs">
							<span class="label">Curve Name</span>
							<input
								type="text"
								class="text-xs focus:ring-0"
								placeholder="liner"
								bind:value={name}
								oninput={(e) => (name = e.currentTarget.value)}
							/>
						</label>
						<div class="flex flex-row gap-2">
							<label class="input w-full text-xs">
								<span class="label">X0</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={x0}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											x0 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											x0 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
							<label class="input w-full text-xs">
								<span class="label">Y0</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={y0}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											y0 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											y0 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
						</div>
						<div class="flex flex-row gap-2">
							<label class="input w-full text-xs">
								<span class="label">X1</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={x1}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											x1 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											x1 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
							<label class="input w-full text-xs">
								<span class="label">Y1</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={y1}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											y1 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											y1 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
						</div>
						<div class="flex flex-col gap-2">
							<button class="btn btn-warning w-full text-xs" onclick={() => editCurve()}
								>Edit Curve</button
							>
							<button class="btn btn-primary text-xs" onclick={() => (action = '')}>Close</button>
						</div>
					</div>
				</div>
			</div>
		{:else if action === 'delete'}
			<div
				transition:fly={{ x: 500, duration: 200 }}
				class="bg-base-100 border-r-base-content/10 fixed top-0 right-[600px] min-h-screen w-[500px] border-r-[0.5px]"
			>
				<div class="p-8">
					<div>
						<p class="text-xs font-medium">Delete Curve</p>
						<p class="text-base-content/60 text-xs font-light">Delete Bezier Curve</p>
						<div class="divider m-2"></div>
					</div>
					<div class="flex flex-col gap-2">
						<p class="text-center text-xs font-semibold">
							This Will Delete Any Animation Using This Curve Too
						</p>
						<button
							class="btn btn-error text-xs"
							onclick={() => {
								animationConn.deleteCurve(deleteCurve!!);
								action = '';
								animationState.ui.openCurve = false;
							}}>Delete Anyway</button
						>
					</div>
					<div class="my-2">
						<p class="text-xs font-semibold">Animation That Will Be Deleted</p>
						<div class="divider my-2"></div>
						<div class="flex flex-col gap-3">
							{#each animationState.getAnimation() as animations}
								{#if animations.curve === deleteName}
									<div class="bg-base-300 flex w-full flex-col gap-4 p-2">
										<p class="text-xs font-semibold">{animations.name}</p>
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
														<td class="text-xs">{Number(animations.speed) * 100} Ms</td>
														<td class="text-xs">{animations.curve}</td>
														<td class="text-xs">{animations.style}</td>
													</tr>
												</tbody>
											</table>
										</div>
									</div>
								{/if}
							{/each}
						</div>
					</div>
				</div>
			</div>
		{/if}
		<div
			class="bg-base-100 fixed top-0 right-0 z-40 min-h-screen w-[600px] shadow-xl"
			transition:fly={{ x: 500, duration: 200 }}
		>
			<div class="p-8">
				<div class="flex flex-row items-center justify-between">
					<div class="flex flex-col">
						<p class="text-sm font-semibold">Bezier Curves</p>
						<p class="text-base-content/60 text-xs font-medium">Add Edit Delete Bezier Curves</p>
					</div>
					<button class="btn btn-sm btn-circle btn-success" onclick={() => (action = 'add')}>
						{@html GetIcons('add', 16)}
					</button>
				</div>
				<div class="divider m-0 my-2"></div>
				<div class="flex max-h-[90vh] flex-col gap-3 overflow-y-auto p-1">
					{#each animationState.getCurves() as curves}
						<div class="bg-base-300 flex w-full flex-row items-center justify-between p-4">
							<div>
								<p class="text-xs font-semibold">{curves.name}</p>
								<p class="text-base-content/70 text-xs font-light">
									Codinations ({curves.x0} ,{curves.y0}) ,({curves.x1} ,{curves.y1})
								</p>
							</div>
							<div class="flex flex-row justify-end gap-3">
								<button
									class="btn btn-sm btn-circle btn-warning"
									onclick={() => {
										action = 'edit';
										name = curves.name;
										x0 = Number(curves.x0);
										x1 = Number(curves.x1);
										y0 = Number(curves.y0);
										y1 = Number(curves.y1);
										oldCurve = curves;
									}}
								>
									{@html GetIcons('edit', 16)}
								</button>
								<button
									class="btn btn-sm btn-circle btn-error"
									onclick={() => {
										action = 'delete';
										deleteName = curves.name;
										deleteCurve = curves;
									}}
								>
									{@html GetIcons('delete', 16)}
								</button>
							</div>
						</div>
					{/each}
				</div>
			</div>
		</div>
	</div>
{/if}
