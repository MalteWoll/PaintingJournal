package com.example.paintingjournal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "colorSchemeColorHexMappingTable",
    primaryKeys = ["colorSchemeIdRef", "colorHexIdRef"],
    foreignKeys = [
        ForeignKey(
            entity = ColorScheme::class,
            parentColumns = ["colorSchemeId"],
            childColumns = ["colorSchemeIdRef"],
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
data class ColorSchemeColorHexMappingTable (
    @ColumnInfo(name = "colorSchemeIdRef")
    val colorSchemeIdRef: Long,
    @ColumnInfo(name = "colorHexIdRef")
    val colorHexIdRef: Long
)