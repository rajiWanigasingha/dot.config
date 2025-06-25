@file:OptIn(ExperimentalSerializationApi::class)

package org.hyprconfig.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class AddReserved(
    val top: Int,
    val bottom: Int,
    val left: Int,
    val right: Int
)

@Serializable
data class MonitorModel(
    @EncodeDefault
    var name: String = "",
    @EncodeDefault
    var disable: Boolean = false,
    @EncodeDefault
    var addreserved: Boolean = false,
    @EncodeDefault
    var addreservedValue: AddReserved? = null,
    @EncodeDefault
    var resolution: String = "preferred",
    @EncodeDefault
    var position: String = "auto",
    @EncodeDefault
    var scale: String = "auto",
    @EncodeDefault
    var mirror: String? = null,
    @EncodeDefault
    var bitDepth: Int? = null,
    @EncodeDefault
    var transform: Int? = null,
    @EncodeDefault
    var cm: String? = null,
    @EncodeDefault
    var sdrsaturation: Float? = null,
    @EncodeDefault
    var sdrbrightness: Float? = null,
    @EncodeDefault
    var vvr: Int? = null,
    @EncodeDefault
    var workspace: String? = null
)
