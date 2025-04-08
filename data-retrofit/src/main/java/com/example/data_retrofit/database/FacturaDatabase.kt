package com.example.data_retrofit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.domain.factura.Factura

@Database(
    entities = [Factura::class],
    version = 1,
    exportSchema = false
)
abstract class FacturaDatabase : RoomDatabase(){
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}