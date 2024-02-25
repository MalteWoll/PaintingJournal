package com.example.paintingjournal.views.miniEdit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaintingStepMappingTable
import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.services.MiniaturesService
import com.example.paintingjournal.views.miniAdd.ExpandablePaintingStep
import com.example.paintingjournal.views.miniAdd.MiniatureDetails
import com.example.paintingjournal.views.miniAdd.MiniatureUiState
import com.example.paintingjournal.views.miniAdd.toMiniature
import com.example.paintingjournal.views.miniAdd.toMiniatureDetails
import com.example.paintingjournal.views.miniAdd.toMiniatureUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MiniEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val miniaturesRepository: MiniaturesRepository,
    private val imagesRepository: ImagesRepository,
    private val miniaturesService: MiniaturesService
    ) : ViewModel() {
    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set

    val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureEditDestination.miniatureArg])

    init {
        // runBlocking, otherwise getPaintsForMiniature() messes up UiState object
        runBlocking {
            miniatureUiState = miniaturesRepository.getMiniatureStream(miniatureId)
                .filterNotNull()
                .first()
                .toMiniatureUiState(true)
        }

        runBlocking {
            val imageList = miniaturesRepository.getImagesForMiniature(miniatureId)
                .filterNotNull()
                .first()
                .toList()
            if(imageList.isNotEmpty()) {
                miniatureUiState = miniatureUiState.copy(imageList = imageList, originalImageList = imageList)
            }
        }

        runBlocking {
            val paintingStepList = miniaturesRepository.getPaintingStepsForMiniature(miniatureId.toLong())
                .filterNotNull()
                .first()
                .toList()
            if(paintingStepList.isNotEmpty()) {
                miniatureUiState = miniatureUiState.copy(
                    expandablePaintingStepList = miniaturesService.createExpandablePaintingStepList(paintingStepList),
                    paintingStepList = paintingStepList,
                    originalPaintingStepList = paintingStepList
                )
            }
        }
    }

    // Gets called every time user navigates back from paint list
    fun getPaintsForMiniature() {
        viewModelScope.launch {
            miniatureUiState = miniatureUiState.copy(
                paintList = miniaturesRepository.getPaintsForMiniature(miniatureId.toLong())
                    .filterNotNull()
                    .first()
                    .toList()
            )
        }
    }

    fun updateUiState(miniatureDetails: MiniatureDetails) {
        miniatureUiState =
            miniatureUiState.copy(miniatureDetails = miniatureDetails, isEntryValid = validateInput(miniatureDetails))
    }

    fun addImageToList(uri: Uri?) {
        if(uri != null) {
            val imageList: MutableList<Image> = miniatureUiState.imageList.toMutableList()

            viewModelScope.launch {
                val imageId = imagesRepository.insertImage(Image(imageUri = uri, saveState = SaveStateEnum.NEW))
                imageList.add(Image(id = imageId, imageUri = uri))
                miniatureUiState = miniatureUiState.copy(imageList = imageList)
            }
        }
    }

    fun removeImageFromList(image: Image) {
        val imageList: MutableList<Image> = miniatureUiState.imageList.toMutableList()

        // New images get deleted without warning, previously added images only after save button is pressed
        if(image.saveState == SaveStateEnum.NEW) {
            viewModelScope.launch {
                imagesRepository.deleteImage(image)
            }
        }

        try {
            imageList.remove(image)
        } catch(e: Exception) {
            println(e)
        }
        miniatureUiState = miniatureUiState.copy(imageList = imageList)
    }

    fun switchEditMode() {
        miniatureUiState = miniatureUiState.copy(canEdit = !miniatureUiState.canEdit)
    }

    fun addPaintingStepToList() {
        viewModelScope.launch {
            val paintingStepId = miniaturesRepository.insertPaintingStep(
                PaintingStep(
                    stepTitle = "",
                    stepDescription = "",
                    stepOrder = 0,
                    saveState = SaveStateEnum.NEW
                )
            )

            val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
            expandablePaintingStepList.add(
                ExpandablePaintingStep(
                    id = paintingStepId,
                    stepTitle = "",
                    stepDescription = "",
                    stepOrder = 0,
                    isExpanded = true,
                    saveState = SaveStateEnum.NEW
                )
            )
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList.toList())
        }
    }

    fun removePaintingStepFromList(expandablePaintingStep: ExpandablePaintingStep) {
        viewModelScope.launch {
            val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
            expandablePaintingStepList.remove(expandablePaintingStep)
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList.toList())

            miniaturesRepository.deletePaintingStep(expandablePaintingStep.id)
        }
    }

    fun togglePaintingStepExpand(paintingStep: ExpandablePaintingStep) {
        val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList
        val index = miniatureUiState.expandablePaintingStepList.indexOf(paintingStep)
        if(index >= 0) {
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            expandablePaintingStepList[index].isExpanded =
                !expandablePaintingStepList[index].isExpanded
            miniatureUiState =
                miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList)
        }
    }

    fun updateUiState(expandablePaintingStep: ExpandablePaintingStep) {
        val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
        val index = miniatureUiState.expandablePaintingStepList.indexOfFirst { it.id == expandablePaintingStep.id }
        if(index >= 0) {
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            expandablePaintingStep.hasChanged = true
            expandablePaintingStepList[index] = expandablePaintingStep
            miniatureUiState =
                miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList.toList())
        }
    }

    suspend fun updateMiniature() {
        if(validateInput(miniatureUiState.miniatureDetails)) {
            updateMiniatureDetails()
            deleteDeletedImage()
            saveNewImages()
            miniatureUiState = miniatureUiState.copy(
                paintingStepList = miniaturesService.createPaintingStepList(miniatureUiState.expandablePaintingStepList)
            )
            deleteDeletedPaintingSteps()
            updatePaintingSteps()
        }
    }

    private suspend fun updateMiniatureDetails() {
        if(miniatureUiState.imageList.isNotEmpty()) {
            val miniatureDetails = miniatureUiState.miniatureDetails
            miniatureDetails.previewImageUri = miniatureUiState.imageList[0].imageUri
            miniatureUiState = miniatureUiState.copy(miniatureDetails = miniatureDetails)
        } else {
            val miniatureDetails = miniatureUiState.miniatureDetails
            miniatureDetails.previewImageUri = null
            miniatureUiState = miniatureUiState.copy(miniatureDetails = miniatureDetails)
        }

        miniaturesRepository.updateMiniature(miniatureUiState.miniatureDetails.toMiniature())
    }

    private suspend fun deleteDeletedImage() {
        miniatureUiState.originalImageList.forEach { image ->
            if(!miniatureUiState.imageList.contains(image)) {
                imagesRepository.deleteImage(image)
            }
        }
    }

    private suspend fun saveNewImages() {
        miniatureUiState.imageList.forEach { image ->
            if(image.saveState != SaveStateEnum.SAVED) {
                image.saveState = SaveStateEnum.SAVED
                imagesRepository.updateImage(image)
                miniaturesRepository.addImageForMiniature(
                    MiniatureImageMappingTable(
                        miniatureId.toLong(),
                        image.id
                    )
                )
            }
        }
    }

    private suspend fun deleteDeletedPaintingSteps() {
        miniatureUiState.originalPaintingStepList.forEach { paintingStep ->
            if(miniatureUiState.paintingStepList.find { it.id == paintingStep.id } == null) {
                miniaturesRepository.deletePaintingStep(paintingStep.id)
            }
        }
    }

    private suspend fun updatePaintingSteps() {
        miniatureUiState.paintingStepList.forEach { paintingStep ->
            if(paintingStep.saveState == SaveStateEnum.NEW) {
                paintingStep.saveState = SaveStateEnum.SAVED
                miniaturesRepository.updatePaintingStep(paintingStep)
                miniaturesRepository.addPaintingStepForMiniature(
                    MiniaturePaintingStepMappingTable(
                        miniatureId.toLong(),
                        paintingStep.id
                    )
                )
            }
            if(paintingStep.saveState == SaveStateEnum.SAVED && paintingStep.hasChanged) {
                paintingStep.hasChanged = false
                miniaturesRepository.updatePaintingStep(paintingStep)
            }
        }
    }

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}