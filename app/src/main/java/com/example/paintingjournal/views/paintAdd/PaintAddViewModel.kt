package com.example.paintingjournal.views.paintAdd

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintImageMappingTable
import com.example.paintingjournal.model.SaveStateEnum
import java.util.Date

class PaintAddViewModel(
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    suspend fun saveMiniaturePaint() {
        if(validateInput()) {
            val paintId = paintsRepository.insertPaint(miniaturePaintUiState.miniaturePaintDetails.toPaint())
            miniaturePaintUiState.imageList.forEach {image ->
                image.saveState = SaveStateEnum.SAVED
                val imageId = imagesRepository.insertImage(image)
                paintsRepository.addImageForPaint(PaintImageMappingTable(paintId, imageId))
            }
        }
    }

    fun addImageToList(uri: Uri?) {
        if(uri != null) {
            val imageList: MutableList<Image> = miniaturePaintUiState.imageList.toMutableList()
            imageList.add(Image(imageUri = uri))
            miniaturePaintUiState = miniaturePaintUiState.copy(imageList = imageList)
        }
    }

    fun removeImageFromList(image: Image) {
        val imageList: MutableList<Image> = miniaturePaintUiState.imageList.toMutableList()
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
        val imageList = miniaturePaintUiState.imageList
        miniaturePaintUiState =
            MiniaturePaintUiState(
                miniaturePaintDetails = miniaturePaintDetails,
                isEntryValid = validateInput(miniaturePaintDetails),
                imageList = imageList)
    }

    private fun validateInput(uiState: MiniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class MiniaturePaintUiState(
    val miniaturePaintDetails: MiniaturePaintDetails = MiniaturePaintDetails(),
    val isEntryValid: Boolean = false,
    val imageList: List<Image> = listOf(),
    val originalImageList: List<Image> = listOf(),
    val canEdit: Boolean = false
)

data class MiniaturePaintDetails(
    val id: Long = 0,
    val name: String = "",
    val manufacturer: String = "",
    val description: String = "",
    val type: String = "",
    val createdAt: Date? = null
)

fun MiniaturePaintDetails.toPaint(): MiniaturePaint = MiniaturePaint(
    id = id,
    name = name,
    manufacturer = manufacturer,
    description = description,
    type = type,
    createdAt = createdAt
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
    createdAt = createdAt
)