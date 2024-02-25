package com.example.paintingjournal.services

import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.views.miniAdd.ExpandablePaintingStep

class MiniaturesServiceImpl : MiniaturesService {
    override fun createExpandablePaintingStepList(paintingStepList: List<PaintingStep>) : List<ExpandablePaintingStep> {
        val expandablePaintingStepList: MutableList<ExpandablePaintingStep> = mutableListOf()
        paintingStepList.forEach { paintingStep ->
            expandablePaintingStepList.add(
                ExpandablePaintingStep(
                    id = paintingStep.id,
                    stepTitle = paintingStep.stepTitle,
                    stepDescription = paintingStep.stepDescription,
                    stepOrder = paintingStep.stepOrder,
                    isExpanded = false,
                    hasChanged = paintingStep.hasChanged,
                    saveState = paintingStep.saveState
                )
            )
        }
        return expandablePaintingStepList
    }

    override fun createPaintingStepList(expandablePaintingStepList: List<ExpandablePaintingStep>): List<PaintingStep> {
        val paintingStepList: MutableList<PaintingStep> = mutableListOf()
        expandablePaintingStepList.forEach { expandablePaintingStep ->
            paintingStepList.add(
                PaintingStep(
                    id = expandablePaintingStep.id,
                    stepTitle = expandablePaintingStep.stepTitle,
                    stepDescription = expandablePaintingStep.stepDescription,
                    stepOrder = expandablePaintingStep.stepOrder,
                    hasChanged = expandablePaintingStep.hasChanged,
                    saveState = expandablePaintingStep.saveState
                )
            )
        }
        return paintingStepList.toList()
    }
}