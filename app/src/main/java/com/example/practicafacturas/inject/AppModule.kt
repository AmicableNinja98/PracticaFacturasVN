package com.example.practicafacturas.inject

import android.content.Context
import co.infinum.retromock.Retromock
import com.example.data_retrofit.database.FacturaDao
import com.example.data_retrofit.database.FacturaDatabase
import com.example.data_retrofit.services.CustomKotlinSerializationConverter
import com.example.data_retrofit.services.FacturaApiService
import com.example.data_retrofit.services.RetromockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    /*val json = Json {
        ignoreUnknownKeys = true
    }*/

    /**
     * Inyecta una instancia de retrofit para usar en la aplicación.
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_POSTMAN_URL)
        .addConverterFactory(CustomKotlinSerializationConverter())
        .build()

    @Provides
    @Singleton
    fun provideRetroMock(retrofit: Retrofit): Retromock = Retromock.Builder()
        .retrofit(retrofit)
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
    fun provideRetromockService(retromock: Retromock): RetromockService = retromock.create(
        RetromockService::class.java
    )

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