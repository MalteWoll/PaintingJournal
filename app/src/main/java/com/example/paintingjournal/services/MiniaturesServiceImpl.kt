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
                    isExpanded = false
                )
            )
        }
        return expandablePaintingStepList
    }
}