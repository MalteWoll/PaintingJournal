package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "miniatureImageMapping",
    primaryKeys = ["miniatureIdRef", "imageIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = Miniature::class,
            parentColumns = ["miniatureId"],
            childColumns = ["miniatureIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Image::class,
            parentColumns = ["imageId"],
            childColumns = ["imageIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MiniatureImageMappingTable (
    @ColumnInfo(name = "miniatureIdRef")
    val miniatureIdRef: Long,
    @ColumnInfo(index = true, name = "imageIdRef")
    val imageIdRef: Long
)