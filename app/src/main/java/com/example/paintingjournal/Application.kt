package com.example.paintingjournal

import android.app.Application
import com.example.paintingjournal.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.opencv.android.OpenCVLoader

class PaintingJournal : Application() {
    override fun onCreate() {
        super.onCreate()

        OpenCVLoader.initLocal()

        startKoin {
            androidLogger()
            androidContext(this@PaintingJournal)
            modules(appModule)
        }
    }
}