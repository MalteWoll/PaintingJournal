package com.example.paintingjournal.views.imageViewer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.services.ImageManipulationService
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ImageViewerViewModel(
    savedStateHandle: SavedStateHandle,
    private val imagesRepository: ImagesRepository,
    private val imageManipulationService: ImageManipulationService
): ViewModel() {
    var imageViewerUiState by mutableStateOf(ImageViewerUiState())
        private set

    val imageId: Int = checkNotNull(savedStateHandle[ImageViewerDestination.imageArg])
    var screenToBitmapConversionX: Float = 0f
    var screenToBitmapConversionY: Float = 0f


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
            val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
            imageViewerUiState = imageViewerUiState.copy(
                imageBitmap = bitmap,
                originalImageBitmap = bitmap)
            if (bitmap != null) {
                println("Bitmap size: ${bitmap.width}, ${bitmap.height}")
            }
        }
    }

    fun setImageSize(size: IntSize) {
        if(imageViewerUiState.imageBitmap != null) {
            val imageConversionFloatArray = imageManipulationService.getScreenToBitmapPixelConversion(
                imageViewerUiState.imageBitmap,
                size
            )
            screenToBitmapConversionX = imageConversionFloatArray?.get(0) ?: 0f
            screenToBitmapConversionY = imageConversionFloatArray?.get(1) ?: 0f
            println("Conversion multiplier: ${screenToBitmapConversionX}, $screenToBitmapConversionY")
        }
        imageViewerUiState = imageViewerUiState.copy(composableImageSize = size)
    }

    fun createBitmapAroundPosition(position: Offset) {
        var newBitmap: Bitmap? = null
        viewModelScope.launch {
            newBitmap = imageViewerUiState.imageBitmap?.let {
                imageManipulationService.createBitmapAroundPosition(
                    position,
                    it,
                    IntSize(10, 10),
                    floatArrayOf(screenToBitmapConversionX, screenToBitmapConversionY)
                )
            }
        }
        imageViewerUiState = imageViewerUiState.copy(
            magnifiedBitmap = newBitmap
        )
    }

    fun togglePopupState() {
        imageViewerUiState = imageViewerUiState.copy(showPopup = !imageViewerUiState.showPopup)
    }

    fun togglePreviewState() {
        imageViewerUiState = imageViewerUiState.copy(showMagnifiedPreview = !imageViewerUiState.showMagnifiedPreview)
    }

    fun applyGrayScale() {
        viewModelScope.launch {
            val mutableBitmap = imageViewerUiState.imageBitmap?.copy(Bitmap.Config.ARGB_8888, true)
            val mat = imageManipulationService.getMatFromBitmap(mutableBitmap)
            val grayMat = imageManipulationService.createGrayScaleMat(mat)
            val bitmap = imageManipulationService.getBitmapFromMat(grayMat)
            imageViewerUiState = imageViewerUiState.copy(imageBitmap = bitmap)
        }
    }

    fun resetImage() {
        imageViewerUiState = imageViewerUiState.copy(imageBitmap = imageViewerUiState.originalImageBitmap)
    }
}

data class ImageViewerUiState(
    val imageDetails: ImageDetails = ImageDetails(),
    val showPopup: Boolean = false,
    val imageBitmap: Bitmap? = null,
    val composableImageSize: IntSize? = null,
    val originalImageBitmap: Bitmap? = null,
    val magnifiedBitmap: Bitmap? = null,
    val showMagnifiedPreview: Boolean = false,
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