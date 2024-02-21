package com.example.paintingjournal.views.miniEditPaintsList

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class MiniEditPaintsListViewModel(
    savedStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {
    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureEditPaintsListDestination.miniatureArg])

    var miniatureEditPaintsListUiState by mutableStateOf(MiniatureEditPaintsListUiState())
        private set

    init {
        getPaintList()
    }

    fun changePaintSelectionState(selectablePaintDetails: SelectablePaintDetails) {
        val selectablePaintList: MutableList<SelectablePaintDetails> = miniatureEditPaintsListUiState.selectablePaintList.toMutableList()
        val selectablePaint = selectablePaintList.find { it.id == selectablePaintDetails.id }
        if (selectablePaint != null) {
            selectablePaint.isSelected = !selectablePaint.isSelected
        }
        miniatureEditPaintsListUiState =
            miniatureEditPaintsListUiState.copy(selectablePaintList = selectablePaintList)
    }

    fun getPaintList() {
        viewModelScope.launch {
            miniatureEditPaintsListUiState = miniatureEditPaintsListUiState.copy(
                paintList = paintsRepository.getAllPaintsStream()
                    .filterNotNull()
                    .first()
                    .toList()
            )
            createSelectablePaintList()
        }
    }

    fun createSelectablePaintList() {
        val selectablePaintList: MutableList<SelectablePaintDetails> = mutableListOf()
        miniatureEditPaintsListUiState.paintList.forEach { paint ->
            selectablePaintList.add(
                SelectablePaintDetails(
                    id = paint.id,
                    name = paint.name,
                    manufacturer = paint.manufacturer,
                    description = paint.description,
                    type = paint.type,
                    createdAt = paint.createdAt,
                    previewImageUri = paint.previewImageUri,
                    isSelected = false
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