package com.example.paintingjournal.views.paintEdit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.PaintImageMappingTable
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.MiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PaintEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    var miniaturePaintUiState by mutableStateOf(MiniaturePaintUiState())
        private set

    private val paintId : Int = checkNotNull(savedStateHandle[PaintEditDestination.paintArg])

    init {
        viewModelScope.launch {
            miniaturePaintUiState = paintsRepository.getPaintStream(paintId)
                .filterNotNull()
                .first()
                .toMiniaturePaintUiState(true)
        }

        viewModelScope.launch {
            val imageList = paintsRepository.getImagesForPaint(paintId)
                .filterNotNull()
                .first()
                .toList()
            if(imageList.isNotEmpty()) {
                val imageUriList = imageList.map { it.imageUri!! }
                miniaturePaintUiState =
                    MiniaturePaintUiState(
                        miniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails,
                        isEntryValid = miniaturePaintUiState.isEntryValid,
                        imageUriList = imageUriList
                    )
            }
        }
    }

    fun updateUiState(miniaturePaintDetails: MiniaturePaintDetails) {
        miniaturePaintUiState =
            MiniaturePaintUiState(miniaturePaintDetails, isEntryValid = validateInput(miniaturePaintDetails))
    }

    suspend fun updateMiniaturePaint() {
        if(validateInput(miniaturePaintUiState.miniaturePaintDetails)) {
            paintsRepository.updatePaint(miniaturePaintUiState.miniaturePaintDetails.toPaint())
            miniaturePaintUiState.imageUriList.forEach {imageUri ->
                val imageId = imagesRepository.insertImage(Image(imageUri = imageUri))
                paintsRepository.addImageForPaint(PaintImageMappingTable(paintId.toLong(), imageId))
            }
        }
    }

    fun addImageToList(uri: Uri?) {
        if(uri != null) {
            val imageUriList: MutableList<Uri> = miniaturePaintUiState.imageUriList.toMutableList()
            imageUriList.add(uri)
            miniaturePaintUiState =
                MiniaturePaintUiState(
                    miniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails,
                    isEntryValid = miniaturePaintUiState.isEntryValid,
                    imageUriList = imageUriList)
        }
    }

    private fun validateInput(uiState: MiniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}