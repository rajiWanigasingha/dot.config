import type { Animation, Curve } from "$lib";

function AnimationState() {

    let store = $state({
        animation: [] as Animation[],
        bezier: [] as Curve[]
    })

    let ui = $state({
        openNew: false,
        openEdit: false,
        openCurve: false,
        animation: '',
        animationName: '',
        styles: [] as string[],
        edit: null as null | Animation
    });

    return {
        store,

        ui,

        setAnimation(animations: Animation[]) {
            this.store.animation = animations
        },

        getAnimation() {
            return this.store.animation
        },

        setCurves(curves: Curve[]) {
            this.store.bezier = curves
        },

        getCurves() {
            return this.store.bezier
        }
    }
}

export const animationState = AnimationState()