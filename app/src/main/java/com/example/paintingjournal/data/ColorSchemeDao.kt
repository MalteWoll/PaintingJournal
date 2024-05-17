package com.example.paintingjournal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paintingjournal.model.ColorHex
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.ColorSchemeColorHexMappingTable
import kotlinx.coroutines.flow.Flow

@Dao
interface ColorSchemeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(colorScheme: ColorScheme): Long

    @Update
    suspend fun update(colorScheme: ColorScheme)

    @Delete
    suspend fun delete(colorScheme: ColorScheme)

    @Query("SELECT * from colorSchemes WHERE colorSchemeId = :id")
    fun getColorScheme(id: Long): Flow<ColorScheme>

    @Query("SELECT * from colorSchemes ORDER BY name ASC")
    fun getAllColorSchemes(): Flow<List<ColorScheme>>

    @Query("SELECT colorHexId,hex,createdAt from colorHex " +
            "left join colorSchemeColorHexMappingTable map on colorHex.colorHexId = map.colorHexIdRef " +
            "where colorSchemeIdRef = :id")
    fun getColorHexesForColorScheme(id: Long): Flow<List<ColorHex>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addColorSchemeColorHexMap(colorSchemeColorHexMappingTable: ColorSchemeColorHexMappingTable)

    @Delete
    suspend fun deleteColorSchemeColorHexMap(colorSchemeColorHexMappingTable: ColorSchemeColorHexMappingTable)

    @Insert
    suspend fun insertColorHex(colorHex: ColorHex): Long

    @Delete
    suspend fun deleteColorHex(colorHex: ColorHex)

    @Update
    suspend fun updateColorHex(colorHex: ColorHex)
}