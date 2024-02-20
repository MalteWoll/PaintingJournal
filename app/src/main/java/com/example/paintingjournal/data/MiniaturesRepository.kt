package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import kotlinx.coroutines.flow.Flow

interface MiniaturesRepository {
    fun getAllMiniaturesStream(): Flow<List<Miniature>>
    fun getMiniatureStream(id: Int): Flow<Miniature?>
    suspend fun insertMiniature(miniature: Miniature) : Long
    suspend fun deleteMiniature(miniature: Miniature)
    suspend fun updateMiniature(miniature: Miniature)
    suspend fun getImagesForMiniature(id: Int): Flow<List<Image>>
    suspend fun addImageForMiniature(miniatureImageMappingTable: MiniatureImageMappingTable)
    suspend fun deleteImageForMiniature(miniatureImageMappingTable: MiniatureImageMappingTable)
}