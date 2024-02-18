package com.example.paintingjournal.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class Image (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageId")
    val id: Long = 0,
    @ColumnInfo(name = "imageUri")
    val imageUri: Uri?,
    @ColumnInfo(name = "saveState")
    var saveState: SaveStateEnum = SaveStateEnum.NEW
)