package org.hyprconfig.helpers

import org.hyprconfig.model.VariableModel

internal object StoreVariables {
    val variableStore = mutableListOf<VariableModel>()

    fun add(variables: List<VariableModel>) {
        variableStore.addAll(variables)
    }
}

internal fun String.insertVariables(): String {
    var result = this
    for (v in StoreVariables.variableStore.sortedByDescending { it.value.length }) {
        val escapedValue = Regex.escape(v.value)
        val pattern = Regex("(?<![\\w\$])$escapedValue(?![\\w])")
        result = pattern.replace(result) { v.name }
    }
    return result
}