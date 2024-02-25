package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import com.example.paintingjournal.model.MiniaturePaintingStepMappingTable
import com.example.paintingjournal.model.PaintingStep
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
    suspend fun getPaintsForMiniature(id: Long): Flow<List<MiniaturePaint>>
    suspend fun addPaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable)
    suspend fun deletePaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable)
    suspend fun insertPaintingStep(paintingStep: PaintingStep) : Long
    suspend fun updatePaintingStep(paintingStep: PaintingStep)
    suspend fun deletePaintingStep(id: Long)
    suspend fun getAllPaintingSteps(): Flow<List<PaintingStep>>
    suspend fun getPaintingStep(id: Long): Flow<PaintingStep>
    suspend fun getPaintingStepsForMiniature(id: Long): Flow<List<PaintingStep>>
    suspend fun addPaintingStepForMiniature(miniaturePaintingStepMappingTable: MiniaturePaintingStepMappingTable)
    suspend fun deletePaintingStepForMiniature(miniaturePaintingStepMappingTable: MiniaturePaintingStepMappingTable)
}