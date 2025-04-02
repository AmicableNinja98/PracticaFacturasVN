package com.example.data_retrofit.services

import com.example.domain.factura_response.FacturaResponse
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CustomKotlinSerializationConverter : Converter.Factory() {

    private val json = Json { ignoreUnknownKeys = true } // Configura el JSON para permitir claves desconocidas

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        // Verificamos si el tipo es FacturaResponse
        if (type == FacturaResponse::class.java) {
            return Converter { body ->
                // Convertimos el ResponseBody a un objeto Factura usando Kotlin Serialization
                val jsonString = body.string() ?: ""
                json.decodeFromString<FacturaResponse>(jsonString)
            }
        }
        return null
    }
}
