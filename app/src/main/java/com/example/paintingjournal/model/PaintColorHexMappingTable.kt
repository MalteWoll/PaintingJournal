package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "paintColorHexMappingTable",
    primaryKeys = ["paintIdRef", "colorHexIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = MiniaturePaint::class,
            parentColumns = ["paintId"],
            childColumns = ["paintIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ColorHex::class,
            parentColumns = ["colorHexId"],
            childColumns = ["colorHexIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class PaintColorHexMappingTable (
    @ColumnInfo(name = "paintIdRef")
    val paintIdRef: Long,
    @ColumnInfo(name = "colorHexIdRef")
    val colorHexIdRef: Long
)