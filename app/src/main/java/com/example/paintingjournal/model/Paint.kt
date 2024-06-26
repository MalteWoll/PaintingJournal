package com.example.paintingjournal.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "paints")
data class MiniaturePaint (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "paintId")
    val id: Long = 0,
    val name: String,
    val manufacturer: String,
    val description: String,
    val type: String,
    val createdAt: Long,
    var previewImageUri: Uri?,
    val hexColor: String,
    @ColumnInfo(name = "saveState", defaultValue = "SAVED")
    val saveState: SaveStateEnum
)