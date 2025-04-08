package com.example.data_retrofit.services

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CustomKotlinSerializationConverter : Converter.Factory() {

    private val json = Json { ignoreUnknownKeys = true }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return Converter { body ->
            val jsonString = body.string()
            json.decodeFromString(
                json.serializersModule.serializer(type),
                jsonString
            )
        }
    }
}

