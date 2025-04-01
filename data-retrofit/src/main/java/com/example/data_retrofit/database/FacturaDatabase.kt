package com.example.data_retrofit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data_retrofit.repository.FacturaRepository
import com.example.domain.factura.Factura
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import javax.inject.Inject

@Database(
    entities = [Factura::class],
    version = 1,
    exportSchema = false
)
abstract class FacturaDatabase @Inject constructor(private val facturaRepository: FacturaRepository) : RoomDatabase(){
    abstract fun getFacturaDao() : FacturaDao

    companion object{
        @Volatile
        private var INSTANCE : FacturaDatabase? = null

        fun getDatabase(context: Context) : FacturaDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FacturaDatabase::class.java,
                    "factura_database.db"
                )
                    .addCallback(object : RoomDatabase.Callback(){
                        override fun onCreate(db : SupportSQLiteDatabase){
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                INSTANCE?.let {
                                    database -> prepoulateDatabase(database)
                                }
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }

        private fun prepoulateDatabase(facturaDatabase: FacturaDatabase){
            val facturaDao = facturaDatabase.getFacturaDao()
            runBlocking {
                val response = facturaDatabase.facturaRepository.getFacturasFromApi()
                var id = 0
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null && body.numFacturas > 0 && body.facturas.isNotEmpty())
                        body.facturas.forEach { factura ->
                            factura.id = id++
                            facturaDao.insertFactura(factura)
                        }
                }
            }
        }
    }
}