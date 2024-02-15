package com.example.paintingjournal.data

import com.example.paintingjournal.model.Miniature
import kotlinx.coroutines.flow.Flow

interface MiniaturesRepository {
    fun getAllItemsStream(): Flow<List<Miniature>>
    fun getMiniatureStream(id: Int): Flow<Miniature?>
    suspend fun insertMiniature(miniature: Miniature)
    suspend fun deleteMiniature(miniature: Miniature)
    suspend fun updateMiniature(miniature: Miniature)
}