package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName= "paintingStepImageMappingTable",
    primaryKeys = ["paintingStepIdRef", "imageIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = PaintingStep::class,
            parentColumns = ["stepId"],
            childColumns = ["paintingStepIdRef"],
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
data class PaintingStepImageMappingTable (
    @ColumnInfo(name = "paintingStepIdRef")
    val paintingStepIdRef: Long,
    @ColumnInfo(index = true, name = "imageIdRef")
    val imageIdRef: Long
)