package model

import kotlinx.serialization.Serializable

@Serializable
data class ParsedModels(
    val name: String,
    val success: Boolean,
    val description: String,
    val found: Int
)

@Serializable
data class BackupModel(
    val file: String? = null,
    val success: Boolean,
)

@Serializable
enum class ParsingStep {
    NEED,
    PARSE,
    CREATE_HYPRLAND,
    CREATE_BACKUP
}

@Serializable
data class ParsingSteps(
    val step: ParsingStep,
    val need: Boolean? = null,
    val parse: ParsedModels? = null,
    val createHyprland: Boolean? = null,
    val createBackup: BackupModel? = null
)