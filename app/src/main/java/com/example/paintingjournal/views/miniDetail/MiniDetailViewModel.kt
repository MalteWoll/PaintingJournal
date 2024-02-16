package com.example.paintingjournal.views.miniDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.views.miniAdd.MiniatureDetails
import com.example.paintingjournal.views.miniAdd.toMiniatureDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MiniDetailViewModel(
    savedStateHandle: SavedStateHandle,
    miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureDetailsDestination.miniatureIdArg])
    val uiState: StateFlow<MiniatureDetailsUiState> =
        miniaturesRepository.getMiniatureStream(miniatureId)
            .filterNotNull()
            .map {
                MiniatureDetailsUiState(miniatureDetails = it.toMiniatureDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MiniatureDetailsUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MiniatureDetailsUiState(
    val miniatureDetails: MiniatureDetails = MiniatureDetails()
)