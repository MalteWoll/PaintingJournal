package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintImageMappingTable
import kotlinx.coroutines.flow.Flow

class PaintsRepositoryImpl(private val paintDao: PaintDao) : PaintsRepository {
    override fun getAllPaintsStream(): Flow<List<MiniaturePaint>> = paintDao.getAllPaints()
    override fun getPaintStream(id: Int): Flow<MiniaturePaint?> = paintDao.getPaint(id)
    override suspend fun insertPaint(paint: MiniaturePaint) = paintDao.insert(paint)
    override suspend fun deletePaint(paint: MiniaturePaint) = paintDao.delete(paint)
    override suspend fun updatePaint(paint: MiniaturePaint) = paintDao.update(paint)
    override fun getImagesForPaint(id: Int): Flow<List<Image>> = paintDao.getImagesForPaint(id)
    override suspend fun addImageForPaint(paintImageMappingTable: PaintImageMappingTable) = paintDao.addPaintImageMap(paintImageMappingTable)
    override suspend fun deleteImageForPaint(paintImageMappingTable: PaintImageMappingTable) = paintDao.deletePaintImageMap(paintImageMappingTable)
}