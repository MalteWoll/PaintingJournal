package com.example.paintingjournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.paintingjournal.model.Miniature

@Database(entities = [Miniature::class], version = 1, exportSchema = false)
abstract class MiniatureDatabase : RoomDatabase() {
    abstract fun miniatureDao(): MiniatureDao
    companion object {
        @Volatile
        private var Instance: MiniatureDatabase? = null

        fun getDatabase(context: Context): MiniatureDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MiniatureDatabase::class.java, "miniature_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}