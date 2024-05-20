package com.example.paintingjournal.services

import com.example.paintingjournal.model.RgbColorWithPaint

interface ColorService {
    fun getArgbFromInt(intColor: Int): IntArray
    fun getHexFromRgb(rgbArray: IntArray): String
    fun getRgbFromHex(hex: String): IntArray
    fun getHslFromRgb(rgb: IntArray): FloatArray
    fun getRgbFromHsl(hsl: FloatArray): IntArray
    fun adjustHue(hsl: FloatArray, angle: Float): FloatArray
    fun getAnalogousColors(originalColor: FloatArray): List<FloatArray>
    fun getTriadicColors(originalColor: FloatArray): List<FloatArray>
    fun getTetradicColors(originalColor: FloatArray): List<FloatArray>
    fun getRgbListFromHslList(hslList: List<FloatArray>): List<IntArray>
    fun getRgbColorsWithPaintFromRgb(rgbColors: List<IntArray>): List<RgbColorWithPaint>
}