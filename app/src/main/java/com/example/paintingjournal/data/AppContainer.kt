package com.example.paintingjournal.data

import android.content.Context
import com.example.paintingjournal.services.MiniaturesService
import com.example.paintingjournal.services.MiniaturesServiceImpl

interface AppContainer {
    val imagesRepository: ImagesRepository
    val miniaturesRepository: MiniaturesRepository
    val miniaturesService: MiniaturesService
    val paintsRepository: PaintsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val imagesRepository: ImagesRepository by lazy {
        ImagesRepositoryImpl(MiniatureDatabase.getDatabase(context).imageDao())
    }
    override val miniaturesRepository: MiniaturesRepository by lazy {
        MiniaturesRepositoryImpl(MiniatureDatabase.getDatabase(context).miniatureDao())
    }
    override val miniaturesService: MiniaturesService by lazy {
        MiniaturesServiceImpl()
    }
    override val paintsRepository: PaintsRepository by lazy {
        PaintsRepositoryImpl(MiniatureDatabase.getDatabase(context).paintDao())
    }
}
