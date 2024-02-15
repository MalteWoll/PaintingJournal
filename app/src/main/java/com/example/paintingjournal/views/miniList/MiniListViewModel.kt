package com.example.paintingjournal.views.miniList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.Miniature
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MiniListViewModel(miniaturesRepository: MiniaturesRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val miniListUiState: StateFlow<MiniListUiState> =
        miniaturesRepository.getAllMiniaturesStream().map { MiniListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MiniListUiState()
            )
}

data class MiniListUiState(
    val miniatureList: List<Miniature> = listOf()
)