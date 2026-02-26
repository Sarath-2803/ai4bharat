package com.example.ai4bharat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Scheme::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun schemeDao(): SchemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai4bharat_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}