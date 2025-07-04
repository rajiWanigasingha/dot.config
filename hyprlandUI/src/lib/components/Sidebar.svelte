<script lang="ts">
	import {
		helpState,
		sidebarConn,
		SidebarIcon,
		sidebarState,
	} from '$lib';
</script>

<div>
	<div class="flex flex-row items-center justify-between px-2 py-1">
		<div class="flex flex-row items-center gap-2">
			<p class="text-xs font-semibold">Dot.Config</p>
		</div>
		<div class="flex flex-row gap-3">
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle">
				<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
					><path
						fill="currentColor"
						d="M19.14 12.94c.04-.3.06-.61.06-.94c0-.32-.02-.64-.07-.94l2.03-1.58a.49.49 0 0 0 .12-.61l-1.92-3.32a.49.49 0 0 0-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54a.484.484 0 0 0-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58a.49.49 0 0 0-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6s3.6 1.62 3.6 3.6s-1.62 3.6-3.6 3.6"
					/></svg
				>
			</button>
			<!-- svelte-ignore a11y_consider_explicit_label -->
			<button class="btn btn-circle">
				<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24"
					><path
						fill="currentColor"
						d="M15.5 14h-.79l-.28-.27A6.47 6.47 0 0 0 16 9.5A6.5 6.5 0 1 0 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5S14 7.01 14 9.5S11.99 14 9.5 14"
					/></svg
				>
			</button>
		</div>
	</div>
	<div class="divider m-0"></div>
	<div class="max-h-[94vh] overflow-y-auto">
		<ul class="menu rounded-box w-full">
			{#each sidebarState.sidebarState.sidebarItems as item}
				<li>
					<h2 class="menu-title">{item.componentTitle}</h2>
					<ul>
						{#each item.navigationSettings as actions}
							<!-- svelte-ignore a11y_invalid_attribute -->
							<li>
								<a
									href="#"
									class={actions.actionLink === sidebarState.sidebarState.sidebarActive
										? 'menu-active'
										: ''}
									onclick={() => {
										sidebarConn.loadMainPage(actions.actionLink);
										helpState.setShow(false);
									}}
								>
									<div>
										{@html SidebarIcon(actions.icon)}
									</div>
									{actions.name}
								</a>
							</li>
						{/each}
					</ul>
				</li>
			{/each}
		</ul>
	</div>
</div>
