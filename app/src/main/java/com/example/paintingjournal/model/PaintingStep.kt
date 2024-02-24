package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "painting_steps")
data class PaintingStep (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stepId")
    val id: Long = 0,
    @ColumnInfo(name = "stepTitle")
    val stepTitle: String,
    @ColumnInfo(name = "stepDescription")
    val stepDescription: String,
    @ColumnInfo(name = "stepOrder")
    val stepOrder: Int,
    val saveState: SaveStateEnum = SaveStateEnum.NEW
)