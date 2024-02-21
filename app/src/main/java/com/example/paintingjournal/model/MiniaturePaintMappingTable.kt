package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "miniaturePaintMapping",
    primaryKeys = ["miniatureIdRef", "paintIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = Miniature::class,
            parentColumns = ["miniatureId"],
            childColumns = ["miniatureIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MiniaturePaint::class,
            parentColumns = ["paintId"],
            childColumns = ["paintIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MiniaturePaintMappingTable (
    @ColumnInfo(name = "miniatureIdRef")
    val miniatureIdRef: Long,
    @ColumnInfo(name = "paintIdRef")
    val paintIdRef: Long
)