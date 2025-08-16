package model

import kotlinx.serialization.Serializable
import model.tables.StandedKeywordParseModel

@Serializable
data class SuccessRateOfParse(
    val name: String,
    var success: Boolean?
)

@Serializable
data class HyprlandKeywords(
    var general: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var misc: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var group: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var debug: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var decoration: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var dwindle: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var master: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var animations: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var inputs: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var binds: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var gestures: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var xwayland: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var openGL: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var cursor: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var render: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var ecosystem: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var experimental: MutableList<StandedKeywordParseModel> = mutableListOf(),
    var device: MutableList<StandedKeywordParseModel> = mutableListOf()
)