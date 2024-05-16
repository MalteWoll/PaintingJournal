package com.example.paintingjournal.services

interface ColorService {
    fun getArgbFromInt(intColor: Int): IntArray
    fun getHexFromRgb(rgbArray: IntArray): String
    fun getRgbFromHex(hex: String): IntArray
    fun getHslFromRgb(rgb: IntArray): FloatArray
    fun getRgbFromHsl(hsl: FloatArray): IntArray
}