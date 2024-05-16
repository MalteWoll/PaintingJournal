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
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.services.ColorService
import com.example.paintingjournal.services.ImageManipulationService
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ImageViewerViewModel(
    savedStateHandle: SavedStateHandle,
    private val imagesRepository: ImagesRepository,
    private val paintsRepository: PaintsRepository,
    private val imageManipulationService: ImageManipulationService,
    private val colorService: ColorService
): ViewModel() {
    var imageViewerUiState by mutableStateOf(ImageViewerUiState())
        private set

    val imageId: Int = checkNotNull(savedStateHandle[ImageViewerDestination.imageArg])
    val entryType: Int = checkNotNull(savedStateHandle["entryType"])

    private var screenToBitmapConversionX: Float = 0f
    private var screenToBitmapConversionY: Float = 0f

    @RequiresApi(Build.VERSION_CODES.P)
    fun getData(context: Context) {
        getImage()
        createBitmap(context)
        if(entryType == 1) {
            getMiniaturePaint()
        }
    }

    private fun getImage() {
        runBlocking {
            val imageDetails = imagesRepository.getImageStream(imageId)
                .filterNotNull()
                .first()
                .toImageDetails()
            imageViewerUiState = imageViewerUiState.copy(
                imageDetails = imageDetails,
            )
        }
    }

    private fun getMiniaturePaint() {
        runBlocking {
            val paint = paintsRepository.getPaintForImage(imageId)
                .filterNotNull()
                .first()
                .toMiniaturePaintDetails()
            imageViewerUiState = imageViewerUiState.copy(
                miniaturePaint = paint,
                showAddToPaintButton = true
            )
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
                originalImageBitmap = bitmap
            )
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
        }
        imageViewerUiState = imageViewerUiState.copy(composableImageSize = size)
    }

    fun createBitmapAroundPosition(position: Offset) {
        var magnifiedBitmap: Bitmap? = null
        viewModelScope.launch {
            magnifiedBitmap = imageViewerUiState.imageBitmap?.let {
                imageManipulationService.createBitmapAroundPosition(
                    position,
                    it,
                    IntSize(imageViewerUiState.magnificationPixelSize, imageViewerUiState.magnificationPixelSize),
                    floatArrayOf(screenToBitmapConversionX, screenToBitmapConversionY)
                )
            }
            if(magnifiedBitmap != null) {
                val mutableMagnifiedBitmap = magnifiedBitmap!!.copy(Bitmap.Config.ARGB_8888, true)
                val avgColorHex = imageManipulationService.calculateAveragePixelValue(mutableMagnifiedBitmap)
                imageViewerUiState = imageViewerUiState.copy(hexColor = avgColorHex)

                val colorRgb = colorService.getRgbFromHex(avgColorHex)
                println("RGB: ${colorRgb[0]}, ${colorRgb[1]}, ${colorRgb[2]}")
                val colorHsl = colorService.getHslFromRgb(colorRgb)
                println("HSL: ${colorHsl[0]}, ${colorHsl[1]}, ${colorHsl[2]}")
            }
        }
        imageViewerUiState = imageViewerUiState.copy(
            magnifiedBitmap = magnifiedBitmap
        )
    }

    fun togglePopupState() {
        imageViewerUiState = imageViewerUiState.copy(showPopup = !imageViewerUiState.showPopup)
    }

    fun togglePreviewState() {
        imageViewerUiState = imageViewerUiState.copy(showMagnifiedPreview = !imageViewerUiState.showMagnifiedPreview)
        if(imageViewerUiState.magnifiedBitmap == null) {
            createBitmapAroundPosition(Offset(0f,0f))
        }
    }

    fun changeMagnificationPixelSize(change: Int) {
        val magnificationPixelSize = imageViewerUiState.magnificationPixelSize + change
        if(magnificationPixelSize < 1 || magnificationPixelSize > 100) {
            return
        }
        imageViewerUiState = imageViewerUiState.copy(magnificationPixelSize = magnificationPixelSize)
    }

    fun toggleColorPreview() {
        imageViewerUiState = imageViewerUiState.copy(showColorPreview = !imageViewerUiState.showColorPreview)
    }

    fun applyGrayScale() {
        viewModelScope.launch {
            val mutableBitmap = imageViewerUiState.imageBitmap?.copy(Bitmap.Config.ARGB_8888, true)
            val bitmap = imageManipulationService.getBitmapFromMat(imageManipulationService.createGrayScaleMat(imageManipulationService.getMatFromBitmap(mutableBitmap)))
            imageViewerUiState = imageViewerUiState.copy(imageBitmap = bitmap)
        }
    }

    fun resetImage() {
        imageViewerUiState = imageViewerUiState.copy(imageBitmap = imageViewerUiState.originalImageBitmap)
    }

    fun onUpdateColorForPaint(hexColor: String) {
        viewModelScope.launch {
            if(imageViewerUiState.hexColor != "") {
                imageViewerUiState.miniaturePaint?.copy(hexColor = hexColor)?.let {
                    paintsRepository.updatePaint(
                        it.toPaint()
                    )
                }
            }
        }
    }
}

data class ImageViewerUiState(
    val imageDetails: ImageDetails = ImageDetails(),
    val showPopup: Boolean = false,
    val imageBitmap: Bitmap? = null,
    val composableImageSize: IntSize? = null,
    val originalImageBitmap: Bitmap? = null,
    val magnifiedBitmap: Bitmap? = null,
    val showMagnifiedPreview: Boolean = true,
    val magnificationPixelSize: Int = 10,
    val showColorPreview: Boolean = true,
    val hexColor: String = "",
    val showAddToPaintButton: Boolean = false,
    val miniaturePaint: MiniaturePaintDetails? = null
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