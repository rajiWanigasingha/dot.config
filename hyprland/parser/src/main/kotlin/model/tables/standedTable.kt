package model.tables

import kotlinx.serialization.Serializable
import model.HyprlandTypes

@Serializable
data class StandedKeywordModel(
    val name: String,
    var value: String,
    val type: HyprlandTypes
)

@Serializable
data class StandedKeywordParseModel(
    val name: String,
    val value: String,
    val fileName: String
)

