package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ImagesRepositoryImpl(private val imageDao: ImageDao) : ImagesRepository {
    override suspend fun getAllImagesStream(): Flow<List<Image>> = withContext(Dispatchers.IO) { imageDao.getAllImages() }
    override suspend fun getImageStream(id: Int): Flow<Image> = withContext(Dispatchers.IO) { imageDao.getImage(id) }
    override suspend fun insertImage(image: Image): Long = withContext(Dispatchers.IO) { imageDao.insert(image) }
    override suspend fun deleteImage(image: Image) = withContext(Dispatchers.IO) { imageDao.delete(image) }
    override suspend fun updateImage(image: Image) = withContext(Dispatchers.IO) { imageDao.update(image) }
}