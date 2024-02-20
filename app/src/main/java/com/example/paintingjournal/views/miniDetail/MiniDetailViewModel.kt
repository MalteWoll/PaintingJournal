package com.example.paintingjournal.views.miniDetail

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
import com.example.paintingjournal.views.miniAdd.toMiniatureDetails
import com.example.paintingjournal.views.miniAdd.toMiniatureUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MiniDetailViewModel(
    savedStateHandle: SavedStateHandle,
    val miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    private val miniatureId: Int = checkNotNull(savedStateHandle[MiniatureDetailsDestination.miniatureIdArg])
    var miniatureDetailsUiState by mutableStateOf(MiniatureUiState())
        private set

    fun getData() {
        viewModelScope.launch {
            miniatureDetailsUiState = miniaturesRepository.getMiniatureStream(miniatureId)
                .filterNotNull()
                .first()
                .toMiniatureUiState(true)
        }

        viewModelScope.launch {
            val imageList = miniaturesRepository.getImagesForMiniature(miniatureId)
                .filterNotNull()
                .first()
                .toList()
            if(imageList.isNotEmpty()) {
                miniatureDetailsUiState = miniatureDetailsUiState.copy(imageList = imageList)
            }
        }
    }

    suspend fun deleteMiniature() {
        miniaturesRepository.deleteMiniature(miniatureDetailsUiState.miniatureDetails.toMiniature())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}