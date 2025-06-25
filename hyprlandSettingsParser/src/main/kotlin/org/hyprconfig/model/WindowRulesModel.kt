package org.hyprconfig.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class RulesForWindow(
    val keyword: String,
    @EncodeDefault
    val value: String? = null
)

@Serializable
data class WindowRulesModel(
    val rules: List<RulesForWindow>,
    val params: List<String>
)