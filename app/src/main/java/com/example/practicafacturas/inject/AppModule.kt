package com.example.practicafacturas.inject

import android.content.Context
import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.database.FacturaDatabase
import com.example.data_retrofit.repository.SmartSolarLocalService
import com.example.data_retrofit.services.FacturaApiService
import com.example.data_retrofit.services.SmartSolarService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Objeto que se encargará de realizar la inyección de dependencias en la app
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    const val BASE_URL = "https://viewnextandroid.wiremockapi.cloud/"
    const val BASE_POSTMAN_URL = "https://adf8ae8e-69de-4fc2-b42b-fdb5ff4cbe3b.mock.pstmn.io/"

    /**
     * Inyecta una instancia de retrofit para usar en la aplicación.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_POSTMAN_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    /**
     * Prepara a retrofit para conectarse a la api.
     * @param retrofit -> Instancia de retrofit que usará la aplicación.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): FacturaApiService = retrofit.create(
        FacturaApiService::class.java
    )

    @Provides
    @Singleton
    fun provideSmartSolarService(@ApplicationContext context: Context): SmartSolarService = SmartSolarLocalService(context)

    /**
     * Inyecta la base de datos para usar en la aplicación.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FacturaDatabase =
        FacturaDatabase.getDatabase(context)

    /**
     * Inyecta el dao para que el repositorio pueda usarlo.
     * @param facturaDatabase -> Instancia de la base de datos de la aplicación.
     */
    @Provides
    @Singleton
    fun provideFacturaDao(facturaDatabase: FacturaDatabase): FacturaDao =
        facturaDatabase.getFacturaDao()
}