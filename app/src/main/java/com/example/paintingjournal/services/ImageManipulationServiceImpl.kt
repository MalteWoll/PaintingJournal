package com.example.paintingjournal.services

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class ImageManipulationServiceImpl : ImageManipulationService {
    override fun getMatFromBitmap(bitmap: Bitmap?): Mat {
        val imageMat = Mat()
        Utils.bitmapToMat(bitmap, imageMat)
        return imageMat
    }

    override fun createGrayScaleMat(mat: Mat?): Mat {
        val grayMat = Mat()
        Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)
        return grayMat
    }

    override fun getBitmapFromMat(mat: Mat?): Bitmap? {
        val bitmap = mat?.let { Bitmap.createBitmap(it.cols(), mat.rows(), Bitmap.Config.RGB_565) }
        Utils.matToBitmap(mat, bitmap)
        return bitmap
    }
}