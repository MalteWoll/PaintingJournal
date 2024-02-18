package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "paints")
data class MiniaturePaint (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "paintId")
    val id: Int = 0,
    val name: String,
    val manufacturer: String,
    val description: String,
    val type: String,
    val createdAt: Date?
)