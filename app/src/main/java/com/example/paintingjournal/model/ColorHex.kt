package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colorHex")
data class ColorHex (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "colorHexId")
    val id: Long = 0,
    val hex: String,
    val createdAt: Long
)