package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "paintImageMapping",
    primaryKeys = ["paintIdRef", "imageIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = MiniaturePaint::class,
            parentColumns = ["paintId"],
            childColumns = ["paintIdRef"],
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
data class PaintImageMappingTable (
    @ColumnInfo(name = "paintIdRef")
    val paintIdRef: Int,
    @ColumnInfo(index = true, name = "imageIdRef")
    val imageIdRef: Int
)