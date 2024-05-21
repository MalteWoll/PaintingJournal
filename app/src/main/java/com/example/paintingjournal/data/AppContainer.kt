package com.example.paintingjournal.data

import android.content.Context
import com.example.paintingjournal.services.ColorService
import com.example.paintingjournal.services.ColorServiceImpl
import com.example.paintingjournal.services.ImageManipulationService
import com.example.paintingjournal.services.ImageManipulationServiceImpl
import com.example.paintingjournal.services.ImportService
import com.example.paintingjournal.services.ImportServiceImpl
import com.example.paintingjournal.services.MiniaturesService
import com.example.paintingjournal.services.MiniaturesServiceImpl

interface AppContainer {
    val colorService: ColorService
    val colorSchemeRepository: ColorSchemeRepository
    val imagesRepository: ImagesRepository
    val imageManipulationService: ImageManipulationService
    val importService: ImportService
    val miniaturesRepository: MiniaturesRepository
    val miniaturesService: MiniaturesService
    val paintsRepository: PaintsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val colorService: ColorService by lazy {
        ColorServiceImpl(paintsRepository)
    }
    override val colorSchemeRepository: ColorSchemeRepository by lazy {
        ColorSchemeRepositoryImpl(MiniatureDatabase.getDatabase(context).colorSchemeDao())
    }
    override val imagesRepository: ImagesRepository by lazy {
        ImagesRepositoryImpl(MiniatureDatabase.getDatabase(context).imageDao())
    }
    override val imageManipulationService: ImageManipulationService by lazy {
        ImageManipulationServiceImpl(colorService)
    }
    override val importService: ImportService by lazy {
        ImportServiceImpl(paintsRepository)
    }
    override val miniaturesRepository: MiniaturesRepository by lazy {
        MiniaturesRepositoryImpl(MiniatureDatabase.getDatabase(context).miniatureDao())
    }
    override val miniaturesService: MiniaturesService by lazy {
        MiniaturesServiceImpl(paintsRepository)
    }
    override val paintsRepository: PaintsRepository by lazy {
        PaintsRepositoryImpl(MiniatureDatabase.getDatabase(context).paintDao())
    }
}
