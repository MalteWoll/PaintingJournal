package com.example.paintingjournal.services

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import okhttp3.internal.toHexString
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


        return Bitmap.createBitmap(
            originalBitmap,
            startPositionRectX,
            startPositionRectY,
            rectSize.width,
            rectSize.height,
        )
    }

    override fun calculateAveragePixelValue(mat: Mat?) {
        if (mat != null) {
            val value = mat[0,0]
            println(value.toString())
        }
    }

    override fun calculateAveragePixelValue(bitmap: Bitmap?) : String {
        var sumOfA = 0
        var sumOfR = 0
        var sumOfG = 0
        var sumOfB = 0
        if (bitmap != null) {
            for(x in 0..<bitmap.width) {
                for(y in 0..<bitmap.height) {
                    val intColors = getArgbFromInt(bitmap.getPixel(x,y))
                    sumOfA += intColors[0]
                    sumOfR += intColors[1]
                    sumOfG += intColors[2]
                    sumOfB += intColors[3]
                }
            }
            val pixelAmount = bitmap.width * bitmap.height
            println("Average color: A:${sumOfA/pixelAmount}, R:${sumOfR/pixelAmount}, G:${sumOfG/pixelAmount}, B:${sumOfB/pixelAmount}")
            val avgA = sumOfA/pixelAmount
            val avgR = sumOfR/pixelAmount
            val avgG = sumOfG/pixelAmount
            val avgB = sumOfB/pixelAmount
            val hexValue = getHexFromRgb(intArrayOf(avgR,avgG,avgB))
            val intValue = Color.rgb(avgR,avgG,avgB)
            val hexColor = intValue.toHexString()
            println("hex value: $hexValue")
            println("int value: $intValue")
            println("hex value2: $hexColor")
            return hexValue
        } else {
            return ""
        }
    }

    override fun getArgbFromInt(intColor: Int): IntArray {
        val a = intColor shr 24 and 0xff
        val r = intColor shr 16 and 0xff
        val g = intColor shr 8 and 0xff
        val b = intColor and 0xff
        return intArrayOf(a,r,g,b)
    }

    override fun getHexFromRgb(rgbArray: IntArray): String {
        return if(rgbArray.size == 3) {
            String.format("#%02x%02x%02x", rgbArray[0], rgbArray[1], rgbArray[2])
        } else {
            ""
        }
    }
}