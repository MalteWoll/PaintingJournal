package com.example.paintingjournal.views.mainMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val miniaturesRepository: MiniaturesRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    init {
        removeCancelledEntries()
    }

    private fun removeCancelledEntries() {
        viewModelScope.launch {
            val miniatureList = miniaturesRepository.getAllMiniaturesStream()
                .filterNotNull()
                .first()
                .toList()
            miniatureList.forEach{ miniature ->
                if(miniature.saveState == SaveStateEnum.NEW) {
                    miniaturesRepository.deleteMiniature(miniature)
                }
            }
        }

        viewModelScope.launch {
            val paintingStepsList = miniaturesRepository.getAllPaintingSteps()
                .filterNotNull()
                .first()
                .toList()
            paintingStepsList.forEach { paintingStep ->
                if(paintingStep.saveState == SaveStateEnum.NEW) {
                    miniaturesRepository.deletePaintingStep(paintingStep.id)
                }
            }
        }

        viewModelScope.launch {
            val imageList = imagesRepository.getAllImagesStream()
                .filterNotNull()
                .first()
                .toList()
            imageList.forEach { image ->
                if(image.saveState == SaveStateEnum.NEW) {
                    imagesRepository.deleteImage(image)
                    // TODO: Remove image file
                }
            }
        }
    }
}