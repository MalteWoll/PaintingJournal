package com.example.paintingjournal.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "miniatures")
data class Miniature (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "miniatureId")
    val id: Long = 0,
    val name: String,
    val manufacturer: String,
    val faction: String,
    val createdAt: Long,
    var previewImageUri: Uri?,
    val saveState: SaveStateEnum
)