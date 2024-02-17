package com.example.paintingjournal.data

import android.content.Context

interface AppContainer {
    val miniaturesRepository: MiniaturesRepository
    val paintsRepository: PaintsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val miniaturesRepository: MiniaturesRepository by lazy {
        MiniaturesRepositoryImpl(MiniatureDatabase.getDatabase(context).miniatureDao())
    }
    override val paintsRepository: PaintsRepository by lazy {
        PaintsRepositoryImpl(MiniatureDatabase.getDatabase(context).paintDao())
    }
}
