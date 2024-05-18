package com.example.paintingjournal.views.colorSchemeAdd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ColorSchemeRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.RgbEnum
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ColorSchemeAddViewModel(
    private val paintsRepository: PaintsRepository,
    private val colorSchemeRepository: ColorSchemeRepository
) : ViewModel() {
    var colorSchemeId: Long = 0

    var colorSchemeAddUiState by mutableStateOf(ColorSchemeUiState())
        private set

    init {
        createColorSchemeInDb()
        getPaints()
    }

    private fun createColorSchemeInDb() {
        runBlocking {
            colorSchemeId = colorSchemeRepository.insertColorScheme(ColorSchemeDetails().toColorScheme())
        }
    }

    private fun getPaints() {
        viewModelScope.launch {
            val paints = paintsRepository.getAllPaintsStream()
                .filterNotNull()
                .first()
                .toList()
            colorSchemeAddUiState = colorSchemeAddUiState.copy(
                paints = paints
            )
        }
    }

    fun onColorPickerValueChanged(newValue: Int, rgbEnum: RgbEnum) {
        val rgbColor = colorSchemeAddUiState.mainColorRgb
        when(rgbEnum) {
            RgbEnum.RED -> {
                rgbColor[0] = newValue
            }
            RgbEnum.BLUE -> {
                rgbColor[1] = newValue
            }
            RgbEnum.GREEN -> {
                rgbColor[2] = newValue
            }
        }
        colorSchemeAddUiState = colorSchemeAddUiState.copy(mainColorRgb = IntArray(3))
        colorSchemeAddUiState = colorSchemeAddUiState.copy(
            mainColorRgb = rgbColor
        )
    }
}

data class ColorSchemeUiState(
    val colorSchemeDetails: ColorSchemeDetails = ColorSchemeDetails(),
    val paints: List<MiniaturePaint> = listOf(),
    val mainColorRgb: IntArray = intArrayOf(0,0,0)
)

data class ColorSchemeDetails(
    val id: Long = 0,
    val name: String = "",
    val originalColor: String = "",
    val createdAt: Long = 0,
    val saveState: SaveStateEnum = SaveStateEnum.NEW
)

fun ColorSchemeDetails.toColorScheme(): ColorScheme = ColorScheme(
    id = id,
    name = name,
    originalColor = originalColor,
    createdAt = createdAt,
    saveState = saveState
)

fun ColorScheme.toColorSchemeDetails(): ColorSchemeDetails = ColorSchemeDetails(
    id = id,
    name = name,
    originalColor = originalColor,
    createdAt = createdAt,
    saveState = saveState
)

fun ColorScheme.toColorSchemeUiState(): ColorSchemeUiState = ColorSchemeUiState(
    colorSchemeDetails = this.toColorSchemeDetails()
)