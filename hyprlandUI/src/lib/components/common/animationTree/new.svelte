<script lang="ts">
	import { animationState, type Curve, type Animation, animationConn } from '$lib';
	import { toast } from 'svelte-sonner';

	let status = $state(true);
	let speed = $state(null as null | number);
	let curve = $state('');
	let newCurve = $state({
		name: '',
		x0: null as null | number,
		y0: null as null | number,
		x1: null as null | number,
		y1: null as null | number
	});
	let style = $state('');

	function addNew() {
		let curveResult: Curve;
		let animationResult: Animation;

		if (curve === 'custom') {
			let exist = false;

			animationState.getCurves().forEach((item) => {
				if (item.name === newCurve.name) {
					exist = true;
				}
			});

			if (exist) {
				toast.error('This Name Is Already Exist');
				return;
			}

			curveResult = {
				name: newCurve.name,
				x0: newCurve.x0?.toString() ?? '0',
				y0: newCurve.y0?.toString() ?? '0',
				x1: newCurve.x1?.toString() ?? '0',
				y1: newCurve.y1?.toString() ?? '0'
			};
		} else {
			curveResult = animationState.getCurves().filter((item) => item.name === curve)[0];
		}

		animationResult = {
			name: animationState.ui.animationName,
			onOff: status ? 1 : 0,
			speed: speed ? (speed / 100).toString() : '1',
			curve: curveResult.name,
			style: style
		};

		if (curve !== 'custom') {
			animationConn.addNew(animationResult, null);
		} else {
			animationConn.addNew(animationResult, curveResult);
		}

		animationState.ui.openNew = false;
	}
</script>

<dialog id="my_modal_2" class="modal" open={animationState.ui.openNew}>
	<div class="modal-box min-w-2xl">
		<div class="flex flex-col gap-1">
			<h3 class="text-sm font-bold">Add New Animation</h3>
			<p class="text-base-content/60 text-xs font-medium">
				Add New Aniamtion For {animationState.ui.animation}
			</p>
		</div>
		<div class="divider m-0"></div>
		<div class="flex flex-col gap-1">
			<fieldset class="fieldset">
				<p class="fieldset-legend">Name Of Animation</p>
				<input
					type="text"
					class="input w-full text-xs"
					value={animationState.ui.animationName}
					disabled
				/>
				<label for="name" class="fieldset-label"
					>Name Of The Animation. Can't Be Change. Need To Chooes Add New In Part Of The Tree</label
				>
			</fieldset>
			<div class="divider m-0"></div>
			<fieldset class="fieldset">
				<p class="fieldset-legend">Enable Or Disable</p>
				<div class="flex flex-row items-center justify-between">
					<p class="text-xs font-medium">Enable Or Disable This Animation</p>
					<label class="toggle toggle-md text-base-conten">
						<input
							type="checkbox"
							class="hidden"
							onclick={(e) => (status = e.currentTarget.checked)}
							checked={status}
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
			</fieldset>
			<div class="divider m-0"></div>
			<fieldset class="fieldset">
				<p class="fieldset-legend">Speed Of Animation</p>
				<label class="input w-full text-xs">
					<input
						type="text"
						class="text-xs focus:ring-0"
						placeholder="300"
						bind:value={speed}
						oninput={(e) => {
							if (isNaN(Number(e.currentTarget.value))) {
								speed = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
							} else {
								speed = Number(e.currentTarget.value);
							}
						}}
					/>
					<span class="label">ms</span>
				</label>
				<label for="name" class="fieldset-label">Speed Of The Animation In Miliseconds</label>
			</fieldset>
			<div class="divider m-0"></div>
			<fieldset class="fieldset">
				<p class="fieldset-legend">Animation Cubic Bezier Curve</p>
				{#if curve !== 'custom'}
					<details class="dropdown dropdown-top">
						<summary class="btn w-full text-xs" id="summery"
							>{curve === '' ? 'Choose Or Create New Bezier Curve' : curve}</summary
						>
						<ul
							class="menu dropdown-content bg-base-300 rounded-box z-1 mb-1 max-h-100 w-full flex-nowrap overflow-y-auto shadow-sm"
						>
							<li>
								<button
									class="flex flex-col items-start justify-start gap-1 text-xs"
									onclick={() => (curve = 'custom')}
								>
									<p class="text-start font-medium">Custom Curve</p>
									<p class="text-base-content/60 text-start font-light">
										Create cubic bezier new custom curve
									</p>
								</button>
							</li>
							{#each animationState.getCurves() as curves}
								<li>
									<button
										class="flex flex-col items-start justify-start gap-1 text-xs"
										onclick={() => {
											curve = curves.name;
											const summery = document.getElementById('summery') as HTMLElement;

											summery.click();
										}}
									>
										<p class="text-start font-medium">{curves.name}</p>
										<p class="text-base-content/60 text-start font-light">
											{curves.x0} ,{curves.y0} and {curves.x1} ,{curves.y1}
										</p>
									</button>
								</li>
							{/each}
						</ul>
					</details>
				{:else}
					<div class="flex flex-col gap-1">
						<label class="input w-full text-xs">
							<span class="label">Curve Name</span>
							<input
								type="text"
								class="text-xs focus:ring-0"
								placeholder="liner"
								oninput={(e) => {
									newCurve.name = e.currentTarget.value;
								}}
							/>
						</label>
						<div class="flex flex-row gap-2">
							<label class="input w-full text-xs">
								<span class="label">X0</span>
								<input
									type="text"
									class="text-xs focus:ring-0"
									placeholder="0"
									bind:value={newCurve.x0}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											newCurve.x0 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											newCurve.x0 = Number(e.currentTarget.value);
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
									bind:value={newCurve.y0}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											newCurve.y0 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											newCurve.y0 = Number(e.currentTarget.value);
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
									bind:value={newCurve.x1}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											newCurve.x1 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											newCurve.x1 = Number(e.currentTarget.value);
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
									bind:value={newCurve.y1}
									oninput={(e) => {
										if (isNaN(Number(e.currentTarget.value))) {
											newCurve.y1 = Number(e.currentTarget.value.replace(/[^\d]/g, ''));
										} else {
											newCurve.y1 = Number(e.currentTarget.value);
										}
									}}
								/>
							</label>
						</div>
						<button
							class="btn btn-primary text-xs"
							onclick={() => {
								curve = '';
								newCurve = { name: '', x0: null, y0: null, x1: null, y1: null };
							}}>Close</button
						>
					</div>
				{/if}

				<label for="name" class="fieldset-label"
					>Curve Of The Animation. Choose Custom For Custom Curve</label
				>
			</fieldset>
			<div class="divider m-0"></div>
			<fieldset class="fieldset">
				<p class="fieldset-legend">Styles For Animation</p>
				<textarea
					class="textarea w-full text-xs"
					placeholder="popin 60%"
					oninput={(e) => (style = e.currentTarget.value)}
				></textarea>
				<label for="name" class="fieldset-label"
					>Styles Of Animation. {animationState.ui.styles.join(' ,')} are supported.</label
				>
			</fieldset>
			<div class="divider m-0"></div>
			<button class="btn btn-success w-full text-xs" onclick={() => addNew()}
				>Create New Animation</button
			>
		</div>
	</div>
	<form method="dialog" class="modal-backdrop">
		<button onclick={() => (animationState.ui.openNew = false)}>close</button>
	</form>
</dialog>
