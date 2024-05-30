package com.example.paintingjournal.views.ColorSchemeDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ColorSchemeRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.RgbColorWithPaint
import com.example.paintingjournal.services.ColorService
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeDetails
import com.example.paintingjournal.views.colorSchemeAdd.toColorSchemeDetails
import com.example.paintingjournal.views.paintAdd.MiniaturePaintDetails
import com.example.paintingjournal.views.paintAdd.toMiniaturePaintDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class ColorSchemeDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val paintsRepository: PaintsRepository,
    private val colorSchemeRepository: ColorSchemeRepository,
    private val colorService: ColorService
) : ViewModel() {
    private val colorSchemeId: Long = checkNotNull(savedStateHandle[ColorSchemeDetailsDestination.colorSchemeIdArg])

    var colorSchemeDetailsUiState by mutableStateOf(ColorSchemeDetailsUiState())
        private set

    fun getData() {
        viewModelScope.launch {
            val colorScheme = colorSchemeRepository.getColorScheme(colorSchemeId)
                .filterNotNull()
                .first()
                .toColorSchemeDetails()
            colorSchemeDetailsUiState = colorSchemeDetailsUiState.copy(
                colorSchemeDetails = colorScheme)
        }
        viewModelScope.launch {
            val colors = colorSchemeRepository.getColorHexesForColorScheme(colorSchemeId)
                .filterNotNull()
                .first()
                .toList()
            val rgbColorsWithPaint = mutableListOf<RgbColorWithPaint>()
            colors.forEach { color ->
                var paintDetails = MiniaturePaintDetails()
                // TODO: Maybe set a flag in color if it has a paint instead of the following horror?
                try {
                    val paint = paintsRepository.getPaintForColor(color.id)
                        .first()
                    try {
                        paintDetails = paint.toMiniaturePaintDetails()
                    } catch (n: NullPointerException) {
                        println("No paints")
                    }
                } catch(e: NoSuchElementException) {
                    println("No paints")
                }
                rgbColorsWithPaint.add(
                    RgbColorWithPaint(
                        colorService.getRgbFromHex(color.hex),
                        paintDetails
                    )
                )
            }
            colorSchemeDetailsUiState = colorSchemeDetailsUiState.copy(
                rgbColorsWithPaint = rgbColorsWithPaint
            )
        }
    }
}

data class ColorSchemeDetailsUiState (
    val colorSchemeDetails: ColorSchemeDetails = ColorSchemeDetails(),
    val rgbColorsWithPaint: List<RgbColorWithPaint> = listOf()
)