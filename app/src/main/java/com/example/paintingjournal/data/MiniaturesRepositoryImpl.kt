package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import kotlinx.coroutines.flow.Flow

class MiniaturesRepositoryImpl(private val miniatureDao: MiniatureDao) : MiniaturesRepository {
    override fun getAllMiniaturesStream(): Flow<List<Miniature>> = miniatureDao.getAllMiniatures()
    override fun getMiniatureStream(id: Int): Flow<Miniature?> = miniatureDao.getMiniature(id)
    override suspend fun insertMiniature(miniature: Miniature) : Long = miniatureDao.insert(miniature)
    override suspend fun deleteMiniature(miniature: Miniature) = miniatureDao.delete(miniature)
    override suspend fun updateMiniature(miniature: Miniature) = miniatureDao.update(miniature)
    override suspend fun getImagesForMiniature(id: Int): Flow<List<Image>> = miniatureDao.getImagesForMiniature(id)
    override suspend fun addImageForMiniature(miniatureImageMappingTable: MiniatureImageMappingTable) = miniatureDao.addMiniatureImageMap(miniatureImageMappingTable)
    override suspend fun deleteImageForMiniature(miniatureImageMappingTable: MiniatureImageMappingTable) = miniatureDao.deleteMiniatureImageMap(miniatureImageMappingTable)
    override suspend fun getPaintsForMiniature(id: Long): Flow<List<MiniaturePaint>> = miniatureDao.getPaintsForMiniature(id)
    override suspend fun addPaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable) = miniatureDao.addPaintToMiniature(miniaturePaintMappingTable)
    override suspend fun deletePaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable) = miniatureDao.deletePaintForMiniature(miniaturePaintMappingTable)
}