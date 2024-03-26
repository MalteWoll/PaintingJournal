package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintImageMappingTable
import kotlinx.coroutines.flow.Flow

interface PaintsRepository {
    suspend fun getAllPaintsStream(): Flow<List<MiniaturePaint>>
    suspend fun getPaintStream(id: Int): Flow<MiniaturePaint?>
    suspend fun insertPaint(paint: MiniaturePaint) : Long
    suspend fun deletePaint(paint: MiniaturePaint)
    suspend fun updatePaint(paint: MiniaturePaint)
    suspend fun getImagesForPaint(id: Int): Flow<List<Image>>
    suspend fun getPaintForImage(id: Int): Flow<MiniaturePaint>
    suspend fun addImageForPaint(paintImageMappingTable: PaintImageMappingTable)
    suspend fun deleteImageForPaint(paintImageMappingTable: PaintImageMappingTable)
    suspend fun getAllManufacturers(): Flow<List<String>>
    suspend fun getAllPaintTypes(): Flow<List<String>>
}