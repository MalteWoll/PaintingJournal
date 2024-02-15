package com.example.paintingjournal.views.miniAdd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.Miniature

class MiniAddViewModel(
    private val miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    suspend fun saveMiniature() {
        if(validateInput()) {
            miniaturesRepository.insertMiniature(miniatureUiState.miniatureDetails.toMiniature())
        }
    }

    var miniatureUiState by mutableStateOf(MiniatureUiState())
        private set

    fun updateUiState(miniatureDetails: MiniatureDetails) {
        miniatureUiState =
            MiniatureUiState(miniatureDetails = miniatureDetails, isEntryValid = validateInput(miniatureDetails))
    }

    private fun validateInput(uiState: MiniatureDetails = miniatureUiState.miniatureDetails) : Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

data class MiniatureUiState(
    val miniatureDetails: MiniatureDetails = MiniatureDetails(),
    val isEntryValid: Boolean = false
)

data class MiniatureDetails(
    val id: Int = 0,
    val name: String = "",
    val manufacturer: String = "",
    val faction: String = ""
)

fun MiniatureDetails.toMiniature(): Miniature = Miniature(
    id = id,
    name = name,
    manufacturer = manufacturer,
    faction = faction
)

fun Miniature.toMiniatureUiState(isEntryValid: Boolean = false): MiniatureUiState = MiniatureUiState(
    miniatureDetails = this.toMiniatureDetails(),
    isEntryValid = isEntryValid
)

fun Miniature.toMiniatureDetails(): MiniatureDetails = MiniatureDetails(
    id = id,
    name = name,
    manufacturer = manufacturer,
    faction = faction
)
