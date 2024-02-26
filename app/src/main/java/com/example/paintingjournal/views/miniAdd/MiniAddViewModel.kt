package com.example.paintingjournal.views.miniAdd

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintingStepMappingTable
import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.model.PaintingStepImageMappingTable
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class MiniAddViewModel(
    private val miniaturesRepository: MiniaturesRepository,
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set
    var miniatureId: Long = 0

    init {
        createMiniInDb()
    }

    fun getPaintsForMiniature() {
        viewModelScope.launch {
            miniatureUiState = miniatureUiState.copy(
                paintList = miniaturesRepository.getPaintsForMiniature(miniatureId.toLong())
                    .filterNotNull()
                    .first()
                    .toList()
            )
        }
    }

    // Insert an empty miniature into the database to allow for creation of mappings for miniature and paints
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

    fun addImageToPaintingStep(stepIdAndUri: PaintingStepIdAndUri) {
        if(stepIdAndUri.uri != null) {
            val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
            val expandablePaintingStep = expandablePaintingStepList.find { it.id == stepIdAndUri.id }
            val index = miniatureUiState.expandablePaintingStepList.indexOf(expandablePaintingStep)
            if(expandablePaintingStep != null) {
                val imageList: MutableList<Image> = expandablePaintingStep.imageList.toMutableList()

                viewModelScope.launch {
                    val imageId = imagesRepository.insertImage(Image(imageUri = stepIdAndUri.uri, saveState = SaveStateEnum.NEW))
                    imageList.add(Image(id = imageId, imageUri = stepIdAndUri.uri))
                    expandablePaintingStep.imageList = imageList
                }
            }

            if (expandablePaintingStep != null) {
                expandablePaintingStepList[index] = expandablePaintingStep
            }

            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList)
        }
    }

    fun removeImageFromPaintingStep(paintingStepIdAndImage: PaintingStepIdAndImage) {
        val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
        val expandablePaintingStep = expandablePaintingStepList.find { it.id == paintingStepIdAndImage.id }
        val index = miniatureUiState.expandablePaintingStepList.indexOf(expandablePaintingStep)
        if(expandablePaintingStep != null) {
            val imageList: MutableList<Image> = expandablePaintingStep.imageList.toMutableList()

            viewModelScope.launch {
                imagesRepository.deleteImage(paintingStepIdAndImage.image)
                imageList.remove(paintingStepIdAndImage.image)
                expandablePaintingStep.imageList = imageList
            }
        }

        if (expandablePaintingStep != null) {
            expandablePaintingStepList[index] = expandablePaintingStep
        }

        miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
        miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList)
    }

    fun togglePaintingStepImageEditMode(id: Long) {
        val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
        val expandablePaintingStep =
            expandablePaintingStepList.find { it.id == id }
        val index = miniatureUiState.expandablePaintingStepList.indexOf(expandablePaintingStep)
        if (expandablePaintingStep != null) {
            expandablePaintingStep.canEditImageList = !expandablePaintingStep.canEditImageList
            expandablePaintingStepList[index] = expandablePaintingStep
        }

        miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
        miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList)
    }

    fun addPaintingStepToList() {
        viewModelScope.launch {
            val paintingStepId = miniaturesRepository.insertPaintingStep(
                PaintingStep(
                    stepTitle = "",
                    stepDescription = "",
                    stepOrder = 0,
                    saveState = SaveStateEnum.NEW
                )
            )

            val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
            expandablePaintingStepList.add(
                ExpandablePaintingStep(
                    id = paintingStepId,
                    stepTitle = "",
                    stepDescription = "",
                    stepOrder = 0,
                    isExpanded = true,
                    saveState = SaveStateEnum.NEW
                )
            )
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList.toList())
        }
    }

    fun removePaintingStepFromList(expandablePaintingStep: ExpandablePaintingStep) {
        viewModelScope.launch {
            val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
            expandablePaintingStepList.remove(expandablePaintingStep)
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList.toList())

            miniaturesRepository.deletePaintingStep(expandablePaintingStep.id)
        }
    }

    fun updatePaintStepUiState(expandablePaintingStep: ExpandablePaintingStep) {
        val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList.toMutableList()
        val index = miniatureUiState.expandablePaintingStepList.indexOfFirst { it.id == expandablePaintingStep.id }
        if(index >= 0) {
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            expandablePaintingStepList[index] = expandablePaintingStep
            miniatureUiState =
                miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList.toList())
        }
    }

    fun togglePaintingStepExpand(paintingStep: ExpandablePaintingStep) {
        val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList
        val index = miniatureUiState.expandablePaintingStepList.indexOf(paintingStep)
        if(index >= 0) {
            miniatureUiState = miniatureUiState.copy(expandablePaintingStepList = listOf())
            expandablePaintingStepList[index].isExpanded =
                !expandablePaintingStepList[index].isExpanded
            miniatureUiState =
                miniatureUiState.copy(expandablePaintingStepList = expandablePaintingStepList)
        }
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

            val paintingStepList = createPaintingStepList(miniatureUiState.expandablePaintingStepList)
            paintingStepList.forEach { paintingStep ->
                val savedPaintingStep = paintingStep.copy(saveState = SaveStateEnum.SAVED)
                miniaturesRepository.updatePaintingStep(savedPaintingStep)
                miniaturesRepository.addPaintingStepForMiniature(MiniaturePaintingStepMappingTable(miniatureIdRef = miniatureId, paintingStepIdRef = savedPaintingStep.id))
            }

            val expandablePaintingStepList = miniatureUiState.expandablePaintingStepList
            expandablePaintingStepList.forEach { expandablePaintingStep ->
                expandablePaintingStep.imageList.forEach { image ->
                    image.saveState = SaveStateEnum.SAVED
                    miniaturesRepository.insertImageForPaintingStep(
                        PaintingStepImageMappingTable(
                            expandablePaintingStep.id,
                            image.id
                        )
                    )
                    imagesRepository.updateImage(image)
                }
            }
        }
    }

    fun switchEditMode() {
        miniatureUiState = miniatureUiState.copy(canEdit = !miniatureUiState.canEdit)
    }

    fun updateUiState(miniatureDetails: MiniatureDetails) {
        miniatureUiState = miniatureUiState.copy(miniatureDetails = miniatureDetails, isEntryValid = validateInput(miniatureDetails))
    }

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }

    private fun createPaintingStepList(expandablePaintingStepList: List<ExpandablePaintingStep>) : List<PaintingStep> {
        val paintingStepList: MutableList<PaintingStep> = mutableListOf()
        expandablePaintingStepList.forEach { expandablePaintingStep ->
            paintingStepList.add(
                PaintingStep(
                    id = expandablePaintingStep.id,
                    stepTitle = expandablePaintingStep.stepTitle,
                    stepDescription = expandablePaintingStep.stepDescription,
                    stepOrder = expandablePaintingStep.stepOrder
                )
            )
        }
        return paintingStepList.toList()
    }
}

data class MiniatureUiState(
    val miniatureDetails: MiniatureDetails = MiniatureDetails(),
    val isEntryValid: Boolean = false,
    val imageList: List<Image> = listOf(),
    val originalImageList: List<Image> = listOf(),
    val canEdit: Boolean = false,
    val paintList: List<MiniaturePaint> = listOf(),
    val originalPaintingStepList: List<PaintingStep> = listOf(),
    val paintingStepList: List<PaintingStep> = listOf(),
    val expandablePaintingStepList: List<ExpandablePaintingStep> = listOf()
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

data class ExpandablePaintingStep(
    val id: Long = 0,
    val stepTitle: String,
    val stepDescription: String,
    val stepOrder: Int,
    var isExpanded: Boolean = false,
    val saveState: SaveStateEnum,
    var hasChanged: Boolean = false,
    var imageList: List<Image> = listOf(),
    var originalImageList: List<Image> = listOf(),
    var canEditImageList: Boolean = false
)

data class PaintingStepIdAndUri(
    val uri: Uri?,
    val id: Long
)

data class PaintingStepIdAndImage(
    val image: Image,
    val id: Long
)
