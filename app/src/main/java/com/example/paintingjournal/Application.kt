package com.example.paintingjournal

import android.app.Application
import com.example.paintingjournal.data.AppContainer
import com.example.paintingjournal.data.AppDataContainer
import org.opencv.android.OpenCVLoader

class PaintingJournal : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        OpenCVLoader.initLocal()
        container = AppDataContainer(this)
    }
}