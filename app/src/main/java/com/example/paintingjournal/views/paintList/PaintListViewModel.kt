package com.example.paintingjournal.views.paintList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PaintListViewModel(paintsRepository: PaintsRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val paintListUiState: StateFlow<PaintListUiState> =
        paintsRepository.getAllPaintsStream().map { PaintListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PaintListUiState()
            )
}

data class PaintListUiState(
    val paintList: List<MiniaturePaint> = listOf()
)