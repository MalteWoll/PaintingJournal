package com.example.paintingjournal.views.imageViewer

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ImageViewerViewModel(
    savedStateHandle: SavedStateHandle,
    private val imagesRepository: ImagesRepository
): ViewModel() {
    var imageViewerUiState by mutableStateOf(ImageViewerUiState())
        private set

    val imageId: Int = checkNotNull(savedStateHandle[ImageViewerDestination.imageArg])

    init {
        viewModelScope.launch {
            val imageDetails = imagesRepository.getImageStream(imageId)
                .filterNotNull()
                .first()
                .toImageDetails()
            imageViewerUiState = imageViewerUiState.copy(imageDetails = imageDetails)
        }
    }
}

data class ImageViewerUiState(
    val imageDetails: ImageDetails = ImageDetails()
)

data class ImageDetails(
    val id: Long = 0,
    val imageUri: Uri? = null,
    val saveState: SaveStateEnum = SaveStateEnum.SAVED
)

fun Image.toImageDetails(): ImageDetails = ImageDetails(
    id = id,
    imageUri = imageUri,
    saveState = saveState
)