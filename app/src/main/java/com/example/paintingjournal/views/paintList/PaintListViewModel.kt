package com.example.paintingjournal.views.paintList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PaintListViewModel(val paintsRepository: PaintsRepository) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var paintListUiState by mutableStateOf(PaintListUiState())
        private set

    fun getData() {
        viewModelScope.launch {
            paintListUiState = paintListUiState.copy(
                paintList = paintsRepository.getAllPaintsStream()
                    .filterNotNull()
                    .first()
                    .toList()
            )
        }
    }
}

data class PaintListUiState(
    var paintList: List<MiniaturePaint> = listOf()
)