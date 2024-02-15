package com.example.paintingjournal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miniatures")
data class Miniature (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val manufacturer: String
)