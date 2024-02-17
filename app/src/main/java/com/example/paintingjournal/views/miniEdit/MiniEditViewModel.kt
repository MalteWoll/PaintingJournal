package com.example.paintingjournal.views.miniEdit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.views.miniAdd.MiniatureDetails
import com.example.paintingjournal.views.miniAdd.MiniatureUiState
import com.example.paintingjournal.views.miniAdd.toMiniature
import com.example.paintingjournal.views.miniAdd.toMiniatureUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MiniEditViewModel(
    savedStateHandle: SavedStateHandle,
    val miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set

    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureEditDestination.miniatureArg])

    init {
        viewModelScope.launch {
            miniatureUiState = miniaturesRepository.getMiniatureStream(miniatureId)
                .filterNotNull()
                .first()
                .toMiniatureUiState(true)
        }
    }

    fun updateUiState(miniatureDetails: MiniatureDetails) {
        miniatureUiState =
            MiniatureUiState(miniatureDetails = miniatureDetails, isEntryValid = validateInput(miniatureDetails))
    }

    suspend fun updateMiniature() {
        if(validateInput(miniatureUiState.miniatureDetails)) {
            miniaturesRepository.updateMiniature(miniatureUiState.miniatureDetails.toMiniature())
        }
    }

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}