package com.example.paintingjournal.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "paints")
data class MiniaturePaint (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val manufacturer: String,
    val description: String,
    val type: String,
    val imageUri: String,
    val createdAt: Date?,
    val image: Uri?
)