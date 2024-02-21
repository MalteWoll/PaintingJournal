package com.example.paintingjournal.views.miniEditPaintsList

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class MiniEditPaintsListViewModel(
    savedStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository,
    private val miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureEditPaintsListDestination.miniatureArg])
    private var selectedPaints: List<MiniaturePaint> = listOf()

    var miniatureEditPaintsListUiState by mutableStateOf(MiniatureEditPaintsListUiState())
        private set

    fun getData() {
        getPaintList()
    }

    fun changePaintSelectionState(selectablePaintDetails: SelectablePaintDetails) {
        val selectablePaintList: List<SelectablePaintDetails> = miniatureEditPaintsListUiState.selectablePaintList

        val selectablePaint = selectablePaintList.find { it.id == selectablePaintDetails.id }
        val index = miniatureEditPaintsListUiState.selectablePaintList.indexOf(selectablePaintDetails)

        miniatureEditPaintsListUiState =
            miniatureEditPaintsListUiState.copy(selectablePaintList = listOf())

        /*if (selectablePaint != null) {
            selectablePaint.isSelected = !selectablePaint.isSelected
        }*/
        selectablePaintList[index].isSelected = !selectablePaintList[index].isSelected

        miniatureEditPaintsListUiState =
            miniatureEditPaintsListUiState.copy(selectablePaintList = selectablePaintList)
        if (selectablePaint != null) {
            updateDatabase(selectablePaint)
        }
    }

    private fun updateDatabase(selectablePaintDetails: SelectablePaintDetails) {
        viewModelScope.launch {
            if(selectablePaintDetails.isSelected) {
                try {
                    miniaturesRepository.addPaintForMiniature(
                        MiniaturePaintMappingTable(
                            miniatureIdRef = miniatureId.toLong(),
                            paintIdRef = selectablePaintDetails.id
                        )
                    )
                } catch (e: Exception) {
                    println(e)
                }
            } else {
                try {
                    miniaturesRepository.deletePaintForMiniature(
                        MiniaturePaintMappingTable(
                            miniatureIdRef = miniatureId.toLong(),
                            paintIdRef = selectablePaintDetails.id
                        )
                    )
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
    }

    fun getPaintList() {
        viewModelScope.launch {
            miniatureEditPaintsListUiState = miniatureEditPaintsListUiState.copy(
                paintList = paintsRepository.getAllPaintsStream()
                    .filterNotNull()
                    .first()
                    .toList()
            )
            selectedPaints = miniaturesRepository.getPaintsForMiniature(miniatureId.toLong())
                .filterNotNull()
                .first()
                .toList()
            createSelectablePaintList()
        }
    }

    private fun createSelectablePaintList() {
        val selectablePaintList: MutableList<SelectablePaintDetails> = mutableListOf()
        miniatureEditPaintsListUiState.paintList.forEach { paint ->
            var isSelected = false
            if(selectedPaints.contains(paint)) {
                isSelected = true
            }
            selectablePaintList.add(
                SelectablePaintDetails(
                    id = paint.id,
                    name = paint.name,
                    manufacturer = paint.manufacturer,
                    description = paint.description,
                    type = paint.type,
                    createdAt = paint.createdAt,
                    previewImageUri = paint.previewImageUri,
                    isSelected = isSelected
                )
            )
        }
        miniatureEditPaintsListUiState =
            miniatureEditPaintsListUiState.copy(selectablePaintList = selectablePaintList)
    }

}

data class MiniatureEditPaintsListUiState(
    val paintList: List<MiniaturePaint> = listOf(),
    val selectablePaintList: List<SelectablePaintDetails> = listOf()
)

data class SelectablePaintDetails(
    val id: Long = 0,
    val name: String,
    val manufacturer: String,
    val description: String,
    val type: String,
    val createdAt: Date?,
    val previewImageUri: Uri?,
    var isSelected: Boolean = false
)