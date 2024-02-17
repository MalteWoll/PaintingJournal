package com.example.paintingjournal.data

import com.example.paintingjournal.model.Miniature
import kotlinx.coroutines.flow.Flow

class MiniaturesRepositoryImpl(private val miniatureDao: MiniatureDao) : MiniaturesRepository {
    override fun getAllMiniaturesStream(): Flow<List<Miniature>> = miniatureDao.getAllMiniatures()

    override fun getMiniatureStream(id: Int): Flow<Miniature?> = miniatureDao.getMiniature(id)

    override suspend fun insertMiniature(miniature: Miniature) = miniatureDao.insert(miniature)

    override suspend fun deleteMiniature(miniature: Miniature) = miniatureDao.delete(miniature)

    override suspend fun updateMiniature(miniature: Miniature) = miniatureDao.update(miniature)

}