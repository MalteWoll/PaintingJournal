package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintColorHexMappingTable
import com.example.paintingjournal.model.PaintImageMappingTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PaintsRepositoryImpl(private val paintDao: PaintDao) : PaintsRepository {
    override suspend fun getAllPaintsStream(): Flow<List<MiniaturePaint>> = withContext(Dispatchers.IO) { paintDao.getAllPaints() }
    override suspend fun getPaintStream(id: Int): Flow<MiniaturePaint?> = withContext(Dispatchers.IO) { paintDao.getPaint(id) }
    override suspend fun insertPaint(paint: MiniaturePaint) : Long = withContext(Dispatchers.IO) { paintDao.insert(paint) }
    override suspend fun deletePaint(paint: MiniaturePaint) = withContext(Dispatchers.IO) { paintDao.delete(paint) }
    override suspend fun updatePaint(paint: MiniaturePaint) = withContext(Dispatchers.IO) { paintDao.update(paint) }
    override suspend fun getImagesForPaint(id: Int): Flow<List<Image>> = withContext(Dispatchers.IO) { paintDao.getImagesForPaint(id) }
    override suspend fun getPaintForImage(id: Int): Flow<MiniaturePaint> = withContext(Dispatchers.IO) { paintDao.getPaintForImage(id) }
    override suspend fun addImageForPaint(paintImageMappingTable: PaintImageMappingTable) = withContext(Dispatchers.IO) { paintDao.addPaintImageMap(paintImageMappingTable) }
    override suspend fun deleteImageForPaint(paintImageMappingTable: PaintImageMappingTable) = withContext(Dispatchers.IO) { paintDao.deletePaintImageMap(paintImageMappingTable) }
    override suspend fun getAllManufacturers(): Flow<List<String>> = withContext(Dispatchers.IO) { paintDao.getAllPaintManufacturers() }
    override suspend fun getAllPaintTypes(): Flow<List<String>> = withContext(Dispatchers.IO) { paintDao.getAllPaintTypes() }
    override suspend fun addPaintForColor(paintColorHexMappingTable: PaintColorHexMappingTable) = withContext(Dispatchers.IO) { paintDao.addPaintColorHexMappingTable(paintColorHexMappingTable) }
    override suspend fun getPaintForColor(id: Long): Flow<MiniaturePaint> = withContext(Dispatchers.IO) { paintDao.getPaintForColorHex(id) }
    override suspend fun removePaintColorHexMapping(paintColorHexMappingTable: PaintColorHexMappingTable) = withContext(Dispatchers.IO) { paintDao.removePaintColorHexMappingTable(paintColorHexMappingTable) }
}