package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "miniatures")
data class Miniature (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "miniatureId")
    val id: Int = 0,
    val name: String,
    val manufacturer: String,
    val faction: String,
    val createdAt: Date?
)