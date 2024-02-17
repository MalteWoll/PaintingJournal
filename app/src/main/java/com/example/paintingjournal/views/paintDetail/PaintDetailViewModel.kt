package com.example.paintingjournal.views.paintDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.views.miniDetail.MiniatureDetailsUiState
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PaintDetailViewModel(
    savedStateHandle: SavedStateHandle,
    val paintsRepository: PaintsRepository
) : ViewModel() {
    private val miniaturePainId: Int = checkNotNull(savedStateHandle[PaintDetailsDestination.paintIdArg])
    val uiState: StateFlow<MiniaturePaintDetailsUiState> =
        paintsRepository.getPaintStream(miniaturePainId)
            .filterNotNull()
            .map {
                MiniaturePaintDetailsUiState(miniaturePaintDetails = it.toMiniaturePaintDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MiniaturePaintDetailsUiState()
            )

    suspend fun deletePaint() {
        paintsRepository.deletePaint(uiState.value.miniaturePaintDetails.toPaint())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class MiniaturePaintDetailsUiState(
    val miniaturePaintDetails: MiniaturePaintDetails = MiniaturePaintDetails()
)
