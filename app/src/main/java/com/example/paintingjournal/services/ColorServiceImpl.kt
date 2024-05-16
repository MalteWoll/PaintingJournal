package com.example.paintingjournal.services

import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red

class ColorServiceImpl : ColorService {
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
        val r = color.red
        val g = color.green
        val b = color.blue
        return intArrayOf(r,g,b)
    }
}