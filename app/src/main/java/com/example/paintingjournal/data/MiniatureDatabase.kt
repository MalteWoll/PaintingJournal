package com.example.paintingjournal.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paintingjournal.converters.DateConverters
import com.example.paintingjournal.converters.SaveStateEnumConverter
import com.example.paintingjournal.converters.UriConverters
import com.example.paintingjournal.model.ColorHex
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.ColorSchemeColorHexMappingTable
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import com.example.paintingjournal.model.MiniaturePaintingStepMappingTable
import com.example.paintingjournal.model.PaintImageMappingTable
import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.model.PaintingStepImageMappingTable

@Database(entities = [
    ColorScheme::class,
    ColorSchemeColorHexMappingTable::class,
    ColorHex::class,
    Image::class,
    Miniature::class,
    MiniaturePaint::class,
    PaintImageMappingTable::class,
    MiniatureImageMappingTable::class,
    MiniaturePaintMappingTable::class,
    PaintingStep::class,
    MiniaturePaintingStepMappingTable::class,
    PaintingStepImageMappingTable::class
    ],
    version = 28,
    autoMigrations = [AutoMigration(from = 27, to = 28)],
    exportSchema = true)
@TypeConverters(
    DateConverters::class,
    UriConverters::class,
    SaveStateEnumConverter::class
)
abstract class MiniatureDatabase : RoomDatabase() {
    abstract fun colorSchemeDao(): ColorSchemeDao
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