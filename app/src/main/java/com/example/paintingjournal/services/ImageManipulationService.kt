package com.example.paintingjournal.services

import android.graphics.Bitmap
import org.opencv.core.Mat

interface ImageManipulationService {
    fun getMatFromBitmap(bitmap: Bitmap?) : Mat
    fun createGrayScaleMat(mat: Mat?) : Mat
    fun getBitmapFromMat(mat: Mat?) : Bitmap?
}