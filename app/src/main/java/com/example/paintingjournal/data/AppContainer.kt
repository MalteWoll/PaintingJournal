package com.example.paintingjournal.data

import android.content.Context

interface AppContainer {
    val miniaturesRepository: MiniaturesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val miniaturesRepository: MiniaturesRepository by lazy {
        OfflineMiniaturesRepository(MiniatureDatabase.getDatabase(context).miniatureDao())
    }
}
