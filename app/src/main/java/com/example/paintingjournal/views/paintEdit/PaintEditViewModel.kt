package com.example.paintingjournal.views.paintEdit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.MiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PaintEditViewModel(
    savedStateHandle: SavedStateHandle,
    val paintsRepository: PaintsRepository
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
    }

    fun updateUiState(miniaturePaintDetails: MiniaturePaintDetails) {
        miniaturePaintUiState =
            MiniaturePaintUiState(miniaturePaintDetails, isEntryValid = validateInput(miniaturePaintDetails))
    }

    suspend fun updateMiniaturePaint() {
        if(validateInput(miniaturePaintUiState.miniaturePaintDetails)) {
            paintsRepository.updatePaint(miniaturePaintUiState.miniaturePaintDetails.toPaint())
        }
    }

    private fun validateInput(uiState: MiniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}