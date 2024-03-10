package com.example.paintingjournal.views.paintEdit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.PaintImageMappingTable
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.MiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PaintEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    var miniaturePaintUiState by mutableStateOf(MiniaturePaintUiState())
        private set

    private val paintId : Int = checkNotNull(savedStateHandle[PaintEditDestination.paintArg])

    fun getData() {
        runBlocking {
            miniaturePaintUiState = paintsRepository.getPaintStream(paintId)
                .filterNotNull()
                .first()
                .toMiniaturePaintUiState(true)
            if(miniaturePaintUiState.miniaturePaintDetails.hexColor != "") {
                miniaturePaintUiState = miniaturePaintUiState.copy(initialColor = Color(miniaturePaintUiState.miniaturePaintDetails.hexColor.toColorInt()))
            }
        }

        runBlocking {
            val imageList = paintsRepository.getImagesForPaint(paintId)
                .filterNotNull()
                .first()
                .toList()
            if(imageList.isNotEmpty()) {
                miniaturePaintUiState = miniaturePaintUiState.copy(imageList = imageList, originalImageList = imageList)
            }
        }
    }

    fun updateUiState(miniaturePaintDetails: MiniaturePaintDetails) {
        miniaturePaintUiState =
            miniaturePaintUiState.copy(miniaturePaintDetails, isEntryValid = validateInput(miniaturePaintDetails))
    }

    fun removeImageFromList(image: Image) {
        val imageList: MutableList<Image> = miniaturePaintUiState.imageList.toMutableList()

        // New images get deleted without warning, previously added images only after save button is pressed
        if(image.saveState == SaveStateEnum.NEW) {
            viewModelScope.launch {
                imagesRepository.deleteImage(image)
            }
        }

        try {
            imageList.remove(image)
        } catch (e: Exception) {
            println(e)
        }
        miniaturePaintUiState = miniaturePaintUiState.copy(imageList = imageList)
    }

    fun switchEditMode() {
        miniaturePaintUiState = miniaturePaintUiState.copy(canEdit = !miniaturePaintUiState.canEdit)
    }

    suspend fun updateMiniaturePaint() {
        if(miniaturePaintUiState.imageList.isNotEmpty()) {
            val paintDetails = miniaturePaintUiState.miniaturePaintDetails
            paintDetails.previewImageUri = miniaturePaintUiState.imageList[0].imageUri
            miniaturePaintUiState =
                miniaturePaintUiState.copy(miniaturePaintDetails = paintDetails)
        } else {
            val paintDetails = miniaturePaintUiState.miniaturePaintDetails
            paintDetails.previewImageUri = null
            miniaturePaintUiState =
                miniaturePaintUiState.copy(miniaturePaintDetails = paintDetails)
        }

        paintsRepository.updatePaint(miniaturePaintUiState.miniaturePaintDetails.toPaint())

        miniaturePaintUiState.originalImageList.forEach { image ->
            if (!miniaturePaintUiState.imageList.contains(image)) {
                imagesRepository.deleteImage(image)
            }
        }

        miniaturePaintUiState.imageList.forEach {image ->
            if(image.saveState != SaveStateEnum.SAVED) {
                image.saveState = SaveStateEnum.SAVED
                imagesRepository.updateImage(image)
                paintsRepository.addImageForPaint(
                    PaintImageMappingTable(
                        paintId.toLong(),
                        image.id
                    )
                )
            }

        }
    }

    fun addImageToList(uri: Uri?) {
        if(uri != null) {
            val imageList: MutableList<Image> = miniaturePaintUiState.imageList.toMutableList()

            viewModelScope.launch {
                val imageId = imagesRepository.insertImage(Image(imageUri = uri, saveState = SaveStateEnum.NEW))
                imageList.add(Image(id = imageId, imageUri = uri))
                miniaturePaintUiState = miniaturePaintUiState.copy(imageList = imageList)
            }
        }
    }

    private fun validateInput(uiState: MiniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    fun changeColor(hexColor: String) {
        val formattedHexColor = "#" + hexColor.substring(2)
        miniaturePaintUiState = miniaturePaintUiState.copy(miniaturePaintDetails =
        miniaturePaintUiState.miniaturePaintDetails.copy(hexColor = formattedHexColor))
    }
}