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
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.views.miniAdd.MiniatureDetails
import com.example.paintingjournal.views.miniAdd.MiniatureUiState
import com.example.paintingjournal.views.miniAdd.toMiniature
import com.example.paintingjournal.views.miniAdd.toMiniatureUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MiniEditViewModel(
    savedStateHandle: SavedStateHandle,
    val miniaturesRepository: MiniaturesRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set

    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureEditDestination.miniatureArg])

    init {
        viewModelScope.launch {
            miniatureUiState = miniaturesRepository.getMiniatureStream(miniatureId)
                .filterNotNull()
                .first()
                .toMiniatureUiState(true)
        }

        viewModelScope.launch {
            val imageList = miniaturesRepository.getImagesForMiniature(miniatureId)
                .filterNotNull()
                .first()
                .toList()
            if(imageList.isNotEmpty()) {
                miniatureUiState = miniatureUiState.copy(imageList = imageList, originalImageList = imageList)
            }
        }
    }

    fun updateUiState(miniatureDetails: MiniatureDetails) {
        miniatureUiState =
            MiniatureUiState(miniatureDetails = miniatureDetails, isEntryValid = validateInput(miniatureDetails))
    }

    fun addImageToList(uri: Uri?) {
        if(uri != null) {
            val imageList: MutableList<Image> = miniatureUiState.imageList.toMutableList()
            imageList.add(Image(imageUri = uri))
            miniatureUiState = miniatureUiState.copy(imageList = imageList)
        }
    }

    fun removeImageFromList(image: Image) {
        val imageList: MutableList<Image> = miniatureUiState.imageList.toMutableList()
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

    suspend fun updateMiniature() {
        if(validateInput(miniatureUiState.miniatureDetails)) {
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

            miniatureUiState.originalImageList.forEach { image ->
                if(!miniatureUiState.imageList.contains(image)) {
                    imagesRepository.deleteImage(image)
                }
            }

            miniatureUiState.imageList.forEach { image ->
                if(image.saveState != SaveStateEnum.SAVED) {
                    image.saveState = SaveStateEnum.SAVED
                    val imageId = imagesRepository.insertImage(image)
                    miniaturesRepository.addImageForMiniature(
                        MiniatureImageMappingTable(
                            miniatureId.toLong(),
                            imageId
                        )
                    )
                }
            }
        }
    }

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}