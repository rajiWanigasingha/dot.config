export let updateChange = $state({
    update: null as boolean | null,
    error: null as { name: string, category: string, error: string } | null,

    setUpdate(data: boolean) {
        this.update = data
    },

    clearUpdates() {
        this.update = null
    },

    setError(err: { name: string, category: string, error: string }) {
        this.error = err
    },

    changeError(error: string) {
        if (this.error !== null) {
            this.error.error = error
        } else {
            this.clearError()
        }
    },

    clearError() {
        this.error = null
    }
})