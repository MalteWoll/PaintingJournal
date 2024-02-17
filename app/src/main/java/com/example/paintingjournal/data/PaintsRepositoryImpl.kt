package com.example.paintingjournal.data

import com.example.paintingjournal.model.MiniaturePaint
import kotlinx.coroutines.flow.Flow

class PaintsRepositoryImpl(private val paintDao: PaintDao) : PaintsRepository {
    override fun getAllPaintsStream(): Flow<List<MiniaturePaint>> = paintDao.getAllPaints()

    override fun getPaintStream(id: Int): Flow<MiniaturePaint?> = paintDao.getPaint(id)

    override suspend fun insertPaint(paint: MiniaturePaint) = paintDao.insert(paint)

    override suspend fun deletePaint(paint: MiniaturePaint) = paintDao.delete(paint)

    override suspend fun updatePaint(paint: MiniaturePaint) = paintDao.update(paint)
}