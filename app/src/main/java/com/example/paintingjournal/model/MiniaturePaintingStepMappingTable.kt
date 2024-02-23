package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "miniaturePaintingStepMappingTable",
    primaryKeys = ["miniatureIdRef", "paintingStepIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = Miniature::class,
            parentColumns = ["miniatureId"],
            childColumns = ["miniatureIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PaintingStep::class,
            parentColumns = ["stepId"],
            childColumns = ["paintingStepIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MiniaturePaintingStepMappingTable (
    @ColumnInfo(name = "miniatureIdRef")
    val miniatureIdRef: Long,
    @ColumnInfo(name = "paintingStepIdRef")
    val paintingStepIdRef: Long
)