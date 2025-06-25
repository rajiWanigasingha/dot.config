export let updateChange = $state({
    update: null as { name: string, category: string, sucess: boolean | null } | null,

    setUpdate(updates: { name: string, category: string, sucess: boolean | null }) {
        this.update = updates
    },

    changeUpdate(sucess: boolean) {
        if (this.update !== null) this.update.sucess = sucess
    },

    clearUpdates() {
        this.update = null
    }
})