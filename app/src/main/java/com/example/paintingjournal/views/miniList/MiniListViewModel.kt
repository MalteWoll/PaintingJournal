package com.example.paintingjournal.views.miniList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MiniListViewModel(
    private val miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var miniListUiState by mutableStateOf(MiniListUiState())
        private set

    suspend fun getData() {
        viewModelScope.launch {
            miniListUiState = miniListUiState.copy(
                miniatureList = miniaturesRepository.getAllMiniaturesStream()
                    .filterNotNull()
                    .first()
                    .toList()
                    .sortedByDescending { it.createdAt }
            )
        }
    }
}

data class MiniListUiState(
    val miniatureList: List<Miniature> = listOf()
)