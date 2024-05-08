package com.example.paintingjournal.converters

import androidx.room.TypeConverter
import com.example.paintingjournal.model.SaveStateEnum

class SaveStateEnumConverter {
    @TypeConverter
    fun toSaveState(value: String) = enumValueOf<SaveStateEnum>(value)

    @TypeConverter
    fun fromSaveState(value: SaveStateEnum) = value.ordinal
}