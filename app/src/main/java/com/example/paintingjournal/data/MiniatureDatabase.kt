package com.example.paintingjournal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paintingjournal.converters.DateConverters
import com.example.paintingjournal.converters.UriConverters
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import com.example.paintingjournal.model.MiniaturePaintingStepMappingTable
import com.example.paintingjournal.model.PaintImageMappingTable
import com.example.paintingjournal.model.PaintingStep

@Database(entities = [
    Image::class,
    Miniature::class,
    MiniaturePaint::class,
    PaintImageMappingTable::class,
    MiniatureImageMappingTable::class,
    MiniaturePaintMappingTable::class,
    PaintingStep::class,
    MiniaturePaintingStepMappingTable::class
    ], version = 16, exportSchema = false)
@TypeConverters(DateConverters::class, UriConverters::class)
abstract class MiniatureDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun miniatureDao(): MiniatureDao
    abstract fun paintDao(): PaintDao
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