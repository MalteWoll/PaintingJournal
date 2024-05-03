package com.example.paintingjournal.views.paintAdd

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintImageMappingTable
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.services.ImageManipulationService
import com.example.paintingjournal.services.MiniaturesService
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

class PaintAddViewModel(
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository,
    private val miniaturesService: MiniaturesService
) : ViewModel() {

    init {
        getManufacturerNames()
        getPaintTypes()
    }

    private fun getManufacturerNames() {
        viewModelScope.launch {
            val manufacturers = miniaturesService.getPaintManufacturersNameList()
            miniaturePaintUiState = miniaturePaintUiState.copy(manufacturerNames = manufacturers)
        }
    }

    private fun getPaintTypes() {
        viewModelScope.launch {
            val paintTypes = miniaturesService.getPaintTypesList()
            miniaturePaintUiState = miniaturePaintUiState.copy(paintTypesList = paintTypes)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun saveMiniaturePaint() {
        if(validateInput()) {
            if(miniaturePaintUiState.imageList.isNotEmpty()) {
                val paintDetails = miniaturePaintUiState.miniaturePaintDetails
                paintDetails.previewImageUri = miniaturePaintUiState.imageList[0].imageUri
                miniaturePaintUiState =
                    miniaturePaintUiState.copy(miniaturePaintDetails = paintDetails)
            }

            val currentInstant = Instant.now()
            miniaturePaintUiState = miniaturePaintUiState.copy(miniaturePaintDetails =
                miniaturePaintUiState.miniaturePaintDetails.copy(createdAt = currentInstant.toEpochMilli()))

            val paintId = paintsRepository.insertPaint(miniaturePaintUiState.miniaturePaintDetails.toPaint())
            miniaturePaintUiState.imageList.forEach {image ->
                image.saveState = SaveStateEnum.SAVED
                imagesRepository.updateImage(image)
                paintsRepository.addImageForPaint(PaintImageMappingTable(paintId, image.id))
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

    fun removeImageFromList(image: Image) {
        val imageList: MutableList<Image> = miniaturePaintUiState.imageList.toMutableList()
        viewModelScope.launch {
            imagesRepository.deleteImage(image)
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

    var miniaturePaintUiState by mutableStateOf(MiniaturePaintUiState())
        private set

    fun updateUiState(miniaturePaintDetails: MiniaturePaintDetails) {
        miniaturePaintUiState = miniaturePaintUiState.copy(miniaturePaintDetails = miniaturePaintDetails,
            isEntryValid = validateInput(miniaturePaintDetails))
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

    fun toggleColorPicker() {
        miniaturePaintUiState = miniaturePaintUiState.copy(showColorPicker = !miniaturePaintUiState.showColorPicker)
    }
}

data class MiniaturePaintUiState(
    val miniaturePaintDetails: MiniaturePaintDetails = MiniaturePaintDetails(),
    val isEntryValid: Boolean = false,
    val imageList: List<Image> = listOf(),
    val originalImageList: List<Image> = listOf(),
    val canEdit: Boolean = false,
    val initialColor: Color? = null,
    val showColorPicker: Boolean = false,
    val manufacturerNames: List<String> = listOf(),
    val paintTypesList: List<String> = listOf()
)

data class MiniaturePaintDetails(
    val id: Long = 0,
    val name: String = "",
    val manufacturer: String = "",
    val description: String = "",
    val type: String = "",
    val createdAt: Long = 0,
    var previewImageUri: Uri? = null,
    val hexColor: String = "",
)

fun MiniaturePaintDetails.toPaint(): MiniaturePaint = MiniaturePaint(
    id = id,
    name = name,
    manufacturer = manufacturer,
    description = description,
    type = type,
    createdAt = createdAt,
    previewImageUri = previewImageUri,
    hexColor = hexColor
)

fun MiniaturePaint.toMiniaturePaintUiState(isEntryValid: Boolean = false): MiniaturePaintUiState = MiniaturePaintUiState(
    miniaturePaintDetails = this.toMiniaturePaintDetails(),
    isEntryValid = isEntryValid
)

fun MiniaturePaint.toMiniaturePaintDetails(): MiniaturePaintDetails = MiniaturePaintDetails(
    id = id,
    name = name,
    manufacturer = manufacturer,
    description = description,
    type = type,
    createdAt = createdAt,
    previewImageUri = previewImageUri,
    hexColor = hexColor
)