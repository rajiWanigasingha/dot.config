<script lang="ts">
	import { animationConn, animationState } from '$lib';

	let name = $state('');
	let status = $state(false);
	let speed = $state(0);
	let curve = $state('');
	let style = $state('');

	$effect(() => {
		if (animationState.ui.edit !== null) {
			name = animationState.ui.edit.name;
			status = animationState.ui.edit.onOff === 1 ? true : false;
			speed = Number(animationState.ui.edit.speed) * 100;
			curve = animationState.ui.edit.curve;
			style = animationState.ui.edit.style;
		}
	});

	function edit() {
		const animationResult = {
			name: name,
			onOff: status ? 1 : 0,
			speed: speed ? (speed / 100).toString() : '1',
			curve: curve,
			style: style
		};

		animationConn.edit(animationResult);

		animationState.ui.edit = null;
		animationState.ui.openEdit = false;
	}
</script>

<dialog id="my_modal_2" class="modal" open={animationState.ui.openEdit}>
	<div class="modal-box min-w-2xl">
		<div class="flex flex-col gap-1">
			<h3 class="text-sm font-bold">Edit Animation</h3>
			<p class="text-base-content/60 text-xs font-medium">
				Edit Aniamtion For {name}
			</p>
		</div>
		<div class="divider m-0"></div>
		<div class="flex flex-col gap-1">
			<fieldset class="fieldset">
				<p class="fieldset-legend">Name Of Animation</p>
				<input type="text" class="input w-full text-xs" value={name} disabled />
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
				<details class="dropdown dropdown-top">
					<summary class="btn w-full text-xs" id="summery">{curve}</summary>
					<ul
						class="menu dropdown-content bg-base-300 rounded-box z-1 mb-1 max-h-100 w-full flex-nowrap overflow-y-auto shadow-sm"
					>
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
					value={style}
				></textarea>
				<label for="name" class="fieldset-label"
					>Styles Of Animation. {animationState.ui.styles.join(' ,')} are supported.</label
				>
			</fieldset>
			<div class="divider m-0"></div>
			<button class="btn btn-warning w-full text-xs" onclick={() => edit()}>Edit Animation</button>
		</div>
	</div>
	<form method="dialog" class="modal-backdrop">
		<button onclick={() => (animationState.ui.openEdit = false)}>close</button>
	</form>
</dialog>
