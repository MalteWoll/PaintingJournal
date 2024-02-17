package com.example.paintingjournal.data

import com.example.paintingjournal.model.MiniaturePaint
import kotlinx.coroutines.flow.Flow

interface PaintsRepository {
    fun getAllPaintsStream(): Flow<List<MiniaturePaint>>
    fun getPaintStream(id: Int): Flow<MiniaturePaint?>
    suspend fun insertPaint(paint: MiniaturePaint)
    suspend fun deletePaint(paint: MiniaturePaint)
    suspend fun updatePaint(paint: MiniaturePaint)
}