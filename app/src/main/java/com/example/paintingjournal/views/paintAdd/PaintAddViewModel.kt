package com.example.paintingjournal.views.paintAdd

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.views.miniAdd.MiniatureUiState
import java.util.Date

class PaintAddViewModel(
    private val paintsRepository: PaintsRepository
) : ViewModel() {
    suspend fun saveMiniaturePaint() {
        if(validateInput()) {
            paintsRepository.insertPaint(miniaturePaintUiState.miniaturePaintDetails.toPaint())
        }
    }

    var miniaturePaintUiState by mutableStateOf(MiniaturePaintUiState())
        private set

    fun updateUiState(miniaturePaintDetails: MiniaturePaintDetails) {
        miniaturePaintUiState =
            MiniaturePaintUiState(miniaturePaintDetails = miniaturePaintDetails, isEntryValid = validateInput(miniaturePaintDetails))
    }

    private fun validateInput(uiState: MiniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class MiniaturePaintUiState(
    val miniaturePaintDetails: MiniaturePaintDetails = MiniaturePaintDetails(),
    val isEntryValid: Boolean = false
)

data class MiniaturePaintDetails(
    val id: Int = 0,
    val name: String = "",
    val manufacturer: String = "",
    val description: String = "",
    val type: String = "",
    val imageUri: Uri? = null,
    val createdAt: Date? = null
)

fun MiniaturePaintDetails.toPaint(): MiniaturePaint = MiniaturePaint(
    id = id,
    name = name,
    manufacturer = manufacturer,
    description = description,
    type = type,
    imageUri = imageUri,
    createdAt = createdAt
)

fun MiniaturePaint.toMiniaturePaintUiState(isEntryValid: Boolean = false): MiniaturePaintUiState = MiniaturePaintUiState(
    miniaturePaintDetails = this.toMiniaturePaintDetails(),
    isEntryValid = isEntryValid
)

fun MiniaturePaint.toMiniaturePaintDetails(): MiniaturePaintDetails = MiniaturePaintDetails(
    id = id,
    name = name,
    manufacturer = manufacturer,
    description = description,
    type = type,
    imageUri = imageUri,
    createdAt = createdAt
)