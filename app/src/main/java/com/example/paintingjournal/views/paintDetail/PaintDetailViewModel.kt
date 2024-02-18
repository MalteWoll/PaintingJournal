package com.example.paintingjournal.views.paintDetail

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.data.ImagesRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.views.miniDetail.MiniatureDetailsUiState
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.MiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PaintDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository
) : ViewModel() {
    private val miniaturePaintId: Int = checkNotNull(savedStateHandle[PaintDetailsDestination.paintIdArg])
    var miniaturePaintDetailsUiState by mutableStateOf(MiniaturePaintUiState())
        private set

    fun getData() {
        viewModelScope.launch {
            miniaturePaintDetailsUiState = paintsRepository.getPaintStream(miniaturePaintId)
                .filterNotNull()
                .first()
                .toMiniaturePaintUiState(true)
        }

        viewModelScope.launch {
            val imageList = paintsRepository.getImagesForPaint(miniaturePaintId)
                .filterNotNull()
                .first()
                .toList()
            if(imageList.isNotEmpty()) {
                miniaturePaintDetailsUiState =
                    MiniaturePaintUiState(
                        miniaturePaintDetails = miniaturePaintDetailsUiState.miniaturePaintDetails,
                        isEntryValid = miniaturePaintDetailsUiState.isEntryValid,
                        imageList = imageList
                    )
            }
        }
    }

    suspend fun deletePaint() {
        paintsRepository.deletePaint(miniaturePaintDetailsUiState.miniaturePaintDetails.toPaint())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

