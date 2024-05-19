package com.example.paintingjournal.views.colorSchemeAddPaintList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ColorSchemeRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeDetails
import com.example.paintingjournal.views.colorSchemeAdd.toColorScheme
import com.example.paintingjournal.views.colorSchemeAdd.toColorSchemeDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ColorSchemeAddPaintListViewModel (
    saveStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository,
    private val colorSchemeRepository: ColorSchemeRepository
) : ViewModel() {
    private val colorSchemeId: Long = checkNotNull(saveStateHandle[ColorSchemeAddPaintListDestination.colorSchemeIdArg])
    var colorSchemeAddPaintListUiState by mutableStateOf(ColorSchemeAddPaintListUiState())
        private set

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            val paints = paintsRepository.getAllPaintsStream()
                .filterNotNull()
                .first()
                .toList()
            val colorScheme = colorSchemeRepository.getColorScheme(colorSchemeId)
                .filterNotNull()
                .first()
                .toColorSchemeDetails()
            colorSchemeAddPaintListUiState = colorSchemeAddPaintListUiState.copy(
                paints = paints,
                colorScheme = colorScheme
            )
        }
    }


    fun onSelectPaint(miniaturePaint: MiniaturePaint) {
        colorSchemeAddPaintListUiState = colorSchemeAddPaintListUiState.copy(
            colorScheme = colorSchemeAddPaintListUiState.colorScheme.copy(
                originalColor = miniaturePaint.hexColor
            )
        )
        runBlocking {
            colorSchemeRepository.updateColorScheme(
                colorSchemeAddPaintListUiState.colorScheme.toColorScheme()
            )
        }
    }
}

data class ColorSchemeAddPaintListUiState(
    val paints: List<MiniaturePaint> = listOf(),
    val colorScheme: ColorSchemeDetails = ColorSchemeDetails()
)