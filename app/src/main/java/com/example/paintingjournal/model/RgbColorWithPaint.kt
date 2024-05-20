package com.example.paintingjournal.model

import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails

data class RgbColorWithPaint (
    val rgbColor: IntArray = IntArray(3),
    var miniaturePaint: MiniaturePaintDetails = MiniaturePaintDetails()
)