package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintImageMappingTable
import kotlinx.coroutines.flow.Flow

interface PaintsRepository {
    fun getAllPaintsStream(): Flow<List<MiniaturePaint>>
    fun getPaintStream(id: Int): Flow<MiniaturePaint?>
    suspend fun insertPaint(paint: MiniaturePaint) : Long
    suspend fun deletePaint(paint: MiniaturePaint)
    suspend fun updatePaint(paint: MiniaturePaint)
    fun getImagesForPaint(id: Int): Flow<List<Image>>
    suspend fun addImageForPaint(paintImageMappingTable: PaintImageMappingTable)
    suspend fun deleteImageForPaint(paintImageMappingTable: PaintImageMappingTable)
}