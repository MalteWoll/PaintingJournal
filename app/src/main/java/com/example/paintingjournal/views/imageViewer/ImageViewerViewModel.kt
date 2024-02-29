package com.example.paintingjournal.views.imageViewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.runBlocking

class ImageViewerViewModel(
    savedStateHandle: SavedStateHandle,
    private val imagesRepository: ImagesRepository
): ViewModel() {
    var imageViewerUiState by mutableStateOf(ImageViewerUiState())
        private set

    val imageId: Int = checkNotNull(savedStateHandle[ImageViewerDestination.imageArg])

    fun getImage() {
        runBlocking {
            val imageDetails = imagesRepository.getImageStream(imageId)
                .filterNotNull()
                .first()
                .toImageDetails()
            imageViewerUiState = imageViewerUiState.copy(imageDetails = imageDetails)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun createBitmap(context: Context) {
        viewModelScope.launch {
            val source = imageViewerUiState.imageDetails.imageUri?.let {
                ImageDecoder.createSource(context.contentResolver, it)
            }
            imageViewerUiState = imageViewerUiState.copy(imageBitmap = source?.let { ImageDecoder.decodeBitmap(it) })
        }
    }

    fun togglePopupState() {
        imageViewerUiState = imageViewerUiState.copy(showPopup = !imageViewerUiState.showPopup)
    }

    fun setBitmap(bitmap: Bitmap) {
        imageViewerUiState = imageViewerUiState.copy(imageBitmap = bitmap)
    }
}

data class ImageViewerUiState(
    val imageDetails: ImageDetails = ImageDetails(),
    val showPopup: Boolean = false,
    val imageBitmap: Bitmap? = null
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