package com.example.paintingjournal.views.miniEdit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.views.miniAdd.MiniatureDetails
import com.example.paintingjournal.views.miniAdd.MiniatureUiState

class MiniEditViewModel(
    savedStateHandle: SavedStateHandle,
    miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set

    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureEditDestination.miniatureArg])

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}