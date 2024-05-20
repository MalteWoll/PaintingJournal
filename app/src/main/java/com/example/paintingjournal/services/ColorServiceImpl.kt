package com.example.paintingjournal.services

import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.RgbColorWithPaint
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.math.pow
import kotlin.math.sqrt

class ColorServiceImpl(
    private val paintsRepository: PaintsRepository
) : ColorService {
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

    override fun getRgbFromHex(hex: String): IntArray {
        var hexColor = hex
        if(hexColor == "" || hexColor.isEmpty()) {
            return IntArray(3)
        }

        if(hexColor.length == 8) {
            hexColor = hexColor.substring(2)
        } else {
            if (hexColor.length == 7) {
                hexColor = hexColor.substring(1)
            }
        }

        val r = Integer.valueOf(hexColor.substring(0,2), 16)
        val g = Integer.valueOf(hexColor.substring(2,4), 16)
        val b = Integer.valueOf(hexColor.substring(4,6), 16)

        return intArrayOf(r,g,b)
    }

    override fun getHslFromRgb(rgb: IntArray): FloatArray {
        val hslFloatArray = FloatArray(3)
        Color.colorToHSV(Color.rgb(rgb[0], rgb[1], rgb[2]), hslFloatArray)
        return hslFloatArray
    }

    override fun getRgbFromHsl(hsl: FloatArray): IntArray {
        val color = Color.HSVToColor(hsl)
        return intArrayOf(color.red,color.green,color.blue)
    }

    override fun adjustHue(hsl: FloatArray, angle: Float): FloatArray {
        var hueNew = hsl[0] + angle
        if(hueNew >= 360) {
            hueNew -= 360
        } else {
            if(hueNew < 0) {
                hueNew += 360
            }
        }
        return floatArrayOf(hueNew, hsl[1], hsl[2])
    }

    override fun getAnalogousColors(originalColor: FloatArray): List<FloatArray> {
        val analogousColors = mutableListOf<FloatArray>()
        analogousColors.add(adjustHue(originalColor, -30f))
        analogousColors.add(originalColor)
        analogousColors.add(adjustHue(originalColor, 30f))
        return analogousColors
    }

    override fun getTriadicColors(originalColor: FloatArray): List<FloatArray> {
        val triadicColors = mutableListOf<FloatArray>()
        triadicColors.add(adjustHue(originalColor, -120f))
        triadicColors.add(originalColor)
        triadicColors.add(adjustHue(originalColor, 120f))
        return triadicColors
    }

    override fun getTetradicColors(originalColor: FloatArray): List<FloatArray> {
        val tetradicColors = mutableListOf<FloatArray>()
        tetradicColors.add(originalColor)
        tetradicColors.add(adjustHue(originalColor, 60f))
        tetradicColors.add(adjustHue(originalColor, 180f))
        tetradicColors.add(adjustHue(originalColor, 240f))
        return tetradicColors
    }

    override fun getRgbListFromHslList(hslList: List<FloatArray>): List<IntArray> {
        val rgbList: MutableList<IntArray> = mutableListOf()
        hslList.forEach{ hsl ->
            rgbList.add(getRgbFromHsl(hsl))
        }
        return rgbList.toList()
    }

    override fun getRgbColorsWithPaintFromRgb(rgbColors: List<IntArray>): List<RgbColorWithPaint> {
        val rgbColorWithPaintList: MutableList<RgbColorWithPaint> = mutableListOf()
        rgbColors.forEach { color ->
            rgbColorWithPaintList.add(RgbColorWithPaint(rgbColor = color))
        }
        return rgbColorWithPaintList
    }

    override suspend fun getClosestPaint(rgbColor: IntArray): MiniaturePaintDetails {
        val paints = paintsRepository.getAllPaintsStream()
            .filterNotNull()
            .first()
            .toList()
            .filter { it.hexColor != "" }

        var closestPaint = MiniaturePaintDetails()
        var lowestDistance = 0.0

        if(paints.isNotEmpty()) {
            lowestDistance = getDistanceBetweenTwoRgbColors(getRgbFromHex(paints[0].hexColor), rgbColor)
            closestPaint = paints[0].toMiniaturePaintDetails()
        }

        paints.forEach { paint ->
            val paintColor = getRgbFromHex(paint.hexColor)
            val distance = getDistanceBetweenTwoRgbColors(paintColor, rgbColor)
            if(distance < lowestDistance) {
                lowestDistance = distance
                closestPaint = paint.toMiniaturePaintDetails()
            }
        }

        return closestPaint
    }

    private fun getDistanceBetweenTwoRgbColors(rgbColor1: IntArray, rgbColor2: IntArray) : Double {
        return sqrt( (rgbColor1[0]-rgbColor2[0]).toDouble().pow(2) +
                (rgbColor1[1]-rgbColor2[1]).toDouble().pow(2) +
                (rgbColor1[2]-rgbColor2[2]).toDouble().pow(2) )
    }

    private fun getDistanceBetweenTwoRgbColorsWeighted(rgbColor1: IntArray, rgbColor2: IntArray) : Double {
        return sqrt( ( (rgbColor1[0]-rgbColor2[0])*0.3 ).pow(2) +
                ( (rgbColor1[1]-rgbColor2[1]) * 0.59).pow(2) +
                ( ( rgbColor1[2]-rgbColor2[2]) * 0.11).pow(2) )
    }

    override suspend fun getClosestPaintWeighted(rgbColor: IntArray): MiniaturePaintDetails {
        TODO("Not yet implemented")
    }
}