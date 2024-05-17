package com.example.paintingjournal.data

import com.example.paintingjournal.model.ColorHex
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.ColorSchemeColorHexMappingTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ColorSchemeRepositoryImpl(private val colorSchemeDao: ColorSchemeDao) : ColorSchemeRepository {
    override suspend fun insertColorScheme(colorScheme: ColorScheme): Long = withContext(Dispatchers.IO) { colorSchemeDao.insert(colorScheme) }
    override suspend fun updateColorScheme(colorScheme: ColorScheme) = withContext(Dispatchers.IO) { colorSchemeDao.update(colorScheme) }
    override suspend fun deleteColorScheme(colorScheme: ColorScheme) = withContext(Dispatchers.IO) { colorSchemeDao.delete(colorScheme) }
    override suspend fun getColorScheme(id: Long): Flow<ColorScheme> = withContext(Dispatchers.IO) { colorSchemeDao.getColorScheme(id) }
    override suspend fun getAllColorSchemesStream(): Flow<List<ColorScheme>> = withContext(Dispatchers.IO) { colorSchemeDao.getAllColorSchemes() }
    override suspend fun getColorHexesForColorScheme(id: Long): Flow<List<ColorHex>> = withContext(Dispatchers.IO) { colorSchemeDao.getColorHexesForColorScheme(id) }
    override suspend fun addColorSchemeColorHexMap(colorSchemeColorHexMappingTable: ColorSchemeColorHexMappingTable) = withContext(Dispatchers.IO) { colorSchemeDao.addColorSchemeColorHexMap(colorSchemeColorHexMappingTable) }
    override suspend fun deleteColorSchemeColorHexMap(colorSchemeColorHexMappingTable: ColorSchemeColorHexMappingTable) = withContext(Dispatchers.IO) { colorSchemeDao.deleteColorSchemeColorHexMap(colorSchemeColorHexMappingTable) }
    override suspend fun insertColorHex(colorHex: ColorHex): Long = withContext(Dispatchers.IO) { colorSchemeDao.insertColorHex(colorHex) }
    override suspend fun deleteColorHex(colorHex: ColorHex) = withContext(Dispatchers.IO) { colorSchemeDao.deleteColorHex(colorHex) }
    override suspend fun updateColorHex(colorHex: ColorHex) = withContext(Dispatchers.IO) { colorSchemeDao.updateColorHex(colorHex) }
}