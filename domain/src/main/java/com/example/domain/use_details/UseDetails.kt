package com.example.domain.use_details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UseDetails(
    @SerialName(value = "cau")
    val codigo : String,
    @SerialName(value = "estado")
    val estado : String,
    @SerialName(value = "tipo")
    val tipo : String,
    @SerialName(value = "compensacion")
    val compensacion : String,
    @SerialName(value = "potencia")
    val potencia : String
)