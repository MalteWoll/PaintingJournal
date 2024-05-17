package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colorSchemes")
data class ColorScheme (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "colorSchemeId")
    val id: Long = 0,
    val name: String,
    val originalColor: String,
    val saveState: SaveStateEnum,
    val createdAt: Long
)