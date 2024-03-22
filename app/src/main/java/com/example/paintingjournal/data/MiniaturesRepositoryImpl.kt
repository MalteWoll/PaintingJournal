package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import com.example.paintingjournal.model.MiniaturePaintingStepMappingTable
import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.model.PaintingStepImageMappingTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MiniaturesRepositoryImpl(private val miniatureDao: MiniatureDao) : MiniaturesRepository {
    override suspend fun getAllMiniaturesStream(): Flow<List<Miniature>> = withContext(Dispatchers.IO) { miniatureDao.getAllMiniatures() }
    override suspend fun getMiniatureStream(id: Int): Flow<Miniature?> = withContext(Dispatchers.IO) { miniatureDao.getMiniature(id) }
    override suspend fun insertMiniature(miniature: Miniature) : Long = withContext(Dispatchers.IO) { miniatureDao.insert(miniature) }
    override suspend fun deleteMiniature(miniature: Miniature) = withContext(Dispatchers.IO) { miniatureDao.delete(miniature) }
    override suspend fun updateMiniature(miniature: Miniature) = withContext(Dispatchers.IO) { miniatureDao.update(miniature) }
    override suspend fun getImagesForMiniature(id: Int): Flow<List<Image>> = withContext(Dispatchers.IO) { miniatureDao.getImagesForMiniature(id) }
    override suspend fun addImageForMiniature(miniatureImageMappingTable: MiniatureImageMappingTable) = withContext(Dispatchers.IO) { miniatureDao.addMiniatureImageMap(miniatureImageMappingTable) }
    override suspend fun deleteImageForMiniature(miniatureImageMappingTable: MiniatureImageMappingTable) = withContext(Dispatchers.IO) { miniatureDao.deleteMiniatureImageMap(miniatureImageMappingTable) }
    override suspend fun getPaintsForMiniature(id: Long): Flow<List<MiniaturePaint>> = withContext(Dispatchers.IO) { miniatureDao.getPaintsForMiniature(id) }
    override suspend fun addPaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable) = withContext(Dispatchers.IO) { miniatureDao.addPaintToMiniature(miniaturePaintMappingTable) }
    override suspend fun deletePaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable) = withContext(Dispatchers.IO) { miniatureDao.deletePaintForMiniature(miniaturePaintMappingTable) }
    override suspend fun insertPaintingStep(paintingStep: PaintingStep): Long = withContext(Dispatchers.IO) { miniatureDao.insertPaintingStep(paintingStep) }
    override suspend fun updatePaintingStep(paintingStep: PaintingStep) = withContext(Dispatchers.IO) { miniatureDao.updatePaintingStep(paintingStep) }
    override suspend fun deletePaintingStep(id: Long) = withContext(Dispatchers.IO) {miniatureDao.deletePaintingStep(id) }
    override suspend fun getPaintingStep(id: Long): Flow<PaintingStep> = withContext(Dispatchers.IO) { miniatureDao.getPaintingStep(id) }
    override suspend fun getAllPaintingSteps(): Flow<List<PaintingStep>> = withContext(Dispatchers.IO) { miniatureDao.getAllPaintingSteps() }
    override suspend fun getPaintingStepsForMiniature(id: Long): Flow<List<PaintingStep>> = withContext(Dispatchers.IO) { miniatureDao.getPaintingStepsForMiniature(id) }
    override suspend fun addPaintingStepForMiniature(miniaturePaintingStepMappingTable: MiniaturePaintingStepMappingTable) = withContext(Dispatchers.IO) { miniatureDao.addPaintingStepForMiniature(miniaturePaintingStepMappingTable) }
    override suspend fun deletePaintingStepForMiniature(miniaturePaintingStepMappingTable: MiniaturePaintingStepMappingTable) = withContext(Dispatchers.IO) { miniatureDao.deletePaintingStepForMiniature(miniaturePaintingStepMappingTable) }
    override suspend fun getImagesForPaintingStep(id: Long): Flow<List<Image>> = withContext(Dispatchers.IO) { miniatureDao.getImagesForPaintingStep(id) }
    override suspend fun insertImageForPaintingStep(paintingStepImageMappingTable: PaintingStepImageMappingTable): Long = withContext(Dispatchers.IO) { miniatureDao.insertImageForPaintingStep(paintingStepImageMappingTable) }
    override suspend fun deleteImageForPaintingStep(paintingStepImageMappingTable: PaintingStepImageMappingTable) = withContext(Dispatchers.IO) { miniatureDao.deleteImageForPaintingStep(paintingStepImageMappingTable) }
}