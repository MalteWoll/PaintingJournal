package com.example.paintingjournal.data

import com.example.paintingjournal.model.ColorHex
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.ColorSchemeColorHexMappingTable
import kotlinx.coroutines.flow.Flow

interface ColorSchemeRepository {
    suspend fun insertColorScheme(colorScheme: ColorScheme) : Long
    suspend fun updateColorScheme(colorScheme: ColorScheme)
    suspend fun deleteColorScheme(colorScheme: ColorScheme)
    suspend fun getColorScheme(id: Long) : Flow<ColorScheme>
    suspend fun getAllColorSchemesStream(): Flow<List<ColorScheme>>
    suspend fun getColorHexesForColorScheme(id: Long): Flow<List<ColorHex>>
    suspend fun addColorSchemeColorHexMap(colorSchemeColorHexMappingTable: ColorSchemeColorHexMappingTable)
    suspend fun deleteColorSchemeColorHexMap(colorSchemeColorHexMappingTable: ColorSchemeColorHexMappingTable)
}