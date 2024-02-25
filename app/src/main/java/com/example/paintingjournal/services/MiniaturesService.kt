package com.example.paintingjournal.services

import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.views.miniAdd.ExpandablePaintingStep

interface MiniaturesService {
    fun createExpandablePaintingStepList(paintingStepList: List<PaintingStep>) : List<ExpandablePaintingStep>
    fun createPaintingStepList(expandablePaintingStepList: List<ExpandablePaintingStep>) : List<PaintingStep>
}