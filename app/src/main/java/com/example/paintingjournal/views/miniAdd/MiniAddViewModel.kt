package com.example.paintingjournal.views.miniAdd

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.launch
import java.util.Date

class MiniAddViewModel(
    private val miniaturesRepository: MiniaturesRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set
    var miniatureId: Long = 0

    init {
        createMiniInDb()
    }

    // Insert an empty miniature into the database allow for creation of mappings for miniature and paints
    private fun createMiniInDb() {
        viewModelScope.launch {
            miniatureId = miniaturesRepository.insertMiniature(MiniatureDetails().toMiniature())
            miniatureUiState = miniatureUiState.copy(miniatureDetails = miniatureUiState.miniatureDetails.copy(id = miniatureId))
        }
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
        viewModelScope.launch {
            imagesRepository.deleteImage(image)
        }
        try {
            imageList.remove(image)
        } catch(e: Exception) {
            println(e)
        }
        miniatureUiState = miniatureUiState.copy(imageList = imageList)
    }

    suspend fun saveMiniature() {
        if(validateInput()) {
            if(miniatureUiState.imageList.isNotEmpty()) {
                val miniatureDetails = miniatureUiState.miniatureDetails
                miniatureDetails.previewImageUri
            }

            miniatureUiState = miniatureUiState.copy(miniatureDetails = miniatureUiState.miniatureDetails.copy(saveState = SaveStateEnum.SAVED))
            miniaturesRepository.updateMiniature(miniatureUiState.miniatureDetails.toMiniature())

            miniatureUiState.imageList.forEach { image->
                image.saveState = SaveStateEnum.SAVED
                imagesRepository.updateImage(image)
                miniaturesRepository.addImageForMiniature(MiniatureImageMappingTable(miniatureId, image.id))
            }
        }
    }

    fun switchEditMode() {
        miniatureUiState = miniatureUiState.copy(canEdit = !miniatureUiState.canEdit)
    }

    fun updateUiState(miniatureDetails: MiniatureDetails) {
        miniatureUiState =
            MiniatureUiState(miniatureDetails = miniatureDetails, isEntryValid = validateInput(miniatureDetails))
    }

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class MiniatureUiState(
    val miniatureDetails: MiniatureDetails = MiniatureDetails(),
    val isEntryValid: Boolean = false,
    val imageList: List<Image> = listOf(),
    val originalImageList: List<Image> = listOf(),
    val canEdit: Boolean = false,
    val paintList: List<MiniaturePaint> = listOf()
)

data class MiniatureDetails(
    val id: Long = 0,
    val name: String = "",
    val manufacturer: String = "",
    val faction: String = "",
    val createdAt: Date? = null,
    var previewImageUri: Uri? = null,
    val saveState: SaveStateEnum = SaveStateEnum.NEW
)

fun MiniatureDetails.toMiniature(): Miniature = Miniature(
    id = id,
    name = name,
    manufacturer = manufacturer,
    faction = faction,
    createdAt = createdAt,
    previewImageUri = previewImageUri,
    saveState = saveState
)

fun Miniature.toMiniatureUiState(isEntryValid: Boolean = false): MiniatureUiState = MiniatureUiState(
    miniatureDetails = this.toMiniatureDetails(),
    isEntryValid = isEntryValid
)

fun Miniature.toMiniatureDetails(): MiniatureDetails = MiniatureDetails(
    id = id,
    name = name,
    manufacturer = manufacturer,
    faction = faction,
    createdAt = createdAt,
    previewImageUri = previewImageUri,
    saveState = saveState
)
