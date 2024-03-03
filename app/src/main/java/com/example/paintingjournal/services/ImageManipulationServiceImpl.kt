package com.example.paintingjournal.services

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
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

    // Calculates the ratio to determine which point on the screen refers to which pixel on a bitmap image
    override fun getScreenToBitmapPixelConversion(
        bitmap: Bitmap?,
        screenSize: IntSize
    ): FloatArray? {
        return if(bitmap != null) {
            val width = (bitmap.width.toFloat() / screenSize.width.toFloat())
            val height = (bitmap.height.toFloat() / screenSize.height.toFloat())
            floatArrayOf(width, height)
        } else {
            null
        }
    }

    override fun createBitmapAroundPosition(
        position: Offset,
        originalBitmap: Bitmap,
        rectSize: IntSize,
        screenToBitmapConversion: FloatArray
    ): Bitmap {
        val posXConverted = (position.x * screenToBitmapConversion[0]).toInt()
        val posYConverted = (position.y * screenToBitmapConversion[1]).toInt()
        println("position: ${position.x}, ${position.y}")
        println("converted: ${posXConverted}, $posYConverted")

        var startPositionRectX: Int
        var startPositionRectY: Int
        val halfRectSizeX = rectSize.width / 2
        val halfRectSizeY = rectSize.height / 2

        startPositionRectX = posXConverted - halfRectSizeX
        if(posXConverted + halfRectSizeX > originalBitmap.width) {
            startPositionRectX = originalBitmap.width - rectSize.width
        }
        if(posXConverted - halfRectSizeX < 0) {
            startPositionRectX = 0
        }

        startPositionRectY = posYConverted - halfRectSizeY
        if(posYConverted + halfRectSizeY > originalBitmap.height) {
            startPositionRectY = originalBitmap.height - rectSize.height
        }
        if(posYConverted - halfRectSizeY < 0) {
            startPositionRectY = 0
        }

        println("start position: ${startPositionRectX}, $startPositionRectY")

        return Bitmap.createBitmap(
            originalBitmap,
            startPositionRectX,
            startPositionRectY,
            rectSize.width,
            rectSize.height
        )
    }
}