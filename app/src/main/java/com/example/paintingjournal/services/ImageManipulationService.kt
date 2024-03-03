package com.example.paintingjournal.services

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import org.opencv.core.Mat

interface ImageManipulationService {
    fun getMatFromBitmap(bitmap: Bitmap?) : Mat
    fun createGrayScaleMat(mat: Mat?) : Mat
    fun getBitmapFromMat(mat: Mat?) : Bitmap?
    fun getScreenToBitmapPixelConversion(bitmap: Bitmap?, screenSize: IntSize): FloatArray?
    fun createBitmapAroundPosition(position: Offset, originalBitmap: Bitmap, rectSize: IntSize, screenToBitmapConversion: FloatArray) : Bitmap
    fun calculateAveragePixelValue(mat: Mat?)
    fun calculateAveragePixelValue(bitmap: Bitmap?)
}