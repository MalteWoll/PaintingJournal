package com.example.paintingjournal.data

import com.example.paintingjournal.model.Image
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    suspend fun getAllImagesStream(): Flow<List<Image>>
    suspend fun getImageStream(id: Int): Flow<Image>
    suspend fun insertImage(image: Image) : Long
    suspend fun deleteImage(image: Image)
    suspend fun updateImage(image: Image)
}