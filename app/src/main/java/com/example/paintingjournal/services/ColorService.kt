package com.example.paintingjournal.services

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
}