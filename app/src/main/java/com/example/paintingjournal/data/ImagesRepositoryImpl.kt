package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import kotlinx.coroutines.flow.Flow

class ImagesRepositoryImpl(private val imageDao: ImageDao) : ImagesRepository {
    override fun getAllImagesStream(): Flow<List<Image>> = imageDao.getAllImages()

    override fun getImageStream(id: Int): Flow<Image> = imageDao.getImage(id)

    override suspend fun insertImage(image: Image) = imageDao.insert(image)

    override suspend fun deleteImage(image: Image) = imageDao.delete(image)

    override suspend fun updateImage(image: Image) = imageDao.update(image)
}