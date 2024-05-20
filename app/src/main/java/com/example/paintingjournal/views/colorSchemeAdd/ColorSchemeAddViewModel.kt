package com.example.paintingjournal.views.colorSchemeAdd

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.ColorSchemeRepository
import com.example.paintingjournal.data.PaintsRepository
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.ColorSchemeEnum
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.RgbEnum
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.services.ColorService
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ColorSchemeAddViewModel(
    private val paintsRepository: PaintsRepository,
    private val colorSchemeRepository: ColorSchemeRepository,
    private val colorService: ColorService
) : ViewModel() {
    private var colorSchemeId: Long = 0
    private var colorSchemeExists = false

    var colorSchemeAddUiState by mutableStateOf(ColorSchemeUiState())
        private set

    init {
        createColorSchemeInDb()
        getPaints()
    }

    private fun createColorSchemeInDb() {
        runBlocking {
            colorSchemeId = colorSchemeRepository.insertColorScheme(ColorSchemeDetails().toColorScheme())
            colorSchemeAddUiState = colorSchemeAddUiState.copy(
                colorSchemeDetails = colorSchemeAddUiState.colorSchemeDetails.copy(
                    id = colorSchemeId
                )
            )
            colorSchemeExists = true
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

    fun updateMainColor() {
        viewModelScope.launch {
            if(colorSchemeExists) {
                val colorScheme = colorSchemeRepository.getColorScheme(colorSchemeId)
                    .filterNotNull()
                    .first()
                    .toColorSchemeDetails()
                colorSchemeAddUiState = colorSchemeAddUiState.copy(
                    mainColorRgb = colorService.getRgbFromHex(colorScheme.originalColor)
                )
            }
        }
    }

    fun onColorPickerValueChanged(newValue: Int, rgbEnum: RgbEnum) {
        val rgbColor = colorSchemeAddUiState.mainColorRgb
        when(rgbEnum) {
            RgbEnum.RED -> {
                rgbColor[0] = newValue
            }
            RgbEnum.GREEN -> {
                rgbColor[1] = newValue
            }
            RgbEnum.BLUE -> {
                rgbColor[2] = newValue
            }
        }
        colorSchemeAddUiState = colorSchemeAddUiState.copy(mainColorRgb = IntArray(3))
        colorSchemeAddUiState = colorSchemeAddUiState.copy(
            mainColorRgb = rgbColor
        )

        adjustColorSchemeColors()
    }

    private fun adjustColorSchemeColors() {
        when(colorSchemeAddUiState.selectedColorScheme) {
            ColorSchemeEnum.NONE -> return
            ColorSchemeEnum.ANALOGOUS -> getAnalogousColors()
            ColorSchemeEnum.TRIADIC -> getTriadicColors()
            ColorSchemeEnum.TETRADIC -> getTetradicColors()
        }
    }

    fun toggleColorPicker() {
        colorSchemeAddUiState = colorSchemeAddUiState.copy(
            showColorPicker = !colorSchemeAddUiState.showColorPicker
        )
    }

    fun getAnalogousColors() {
        val mainColorHsl = colorService.getHslFromRgb(colorSchemeAddUiState.mainColorRgb)
        val analogousColorsRgb = colorService.getRgbListFromHslList(
            colorService.getAnalogousColors(mainColorHsl)
        )
        colorSchemeAddUiState = colorSchemeAddUiState.copy(
            colorSchemeColors = analogousColorsRgb,
            selectedColorScheme = ColorSchemeEnum.ANALOGOUS
        )
    }

    fun getTriadicColors() {
        val mainColorHsl = colorService.getHslFromRgb(colorSchemeAddUiState.mainColorRgb)
        val triadicColorsRgb = colorService.getRgbListFromHslList(
            colorService.getTriadicColors(mainColorHsl)
        )
        colorSchemeAddUiState = colorSchemeAddUiState.copy(
            colorSchemeColors = triadicColorsRgb,
            selectedColorScheme = ColorSchemeEnum.TRIADIC
        )
    }

    fun getTetradicColors() {
        val mainColorHsl = colorService.getHslFromRgb(colorSchemeAddUiState.mainColorRgb)
        val tetradicColorsRgb = colorService.getRgbListFromHslList(
            colorService.getTetradicColors(mainColorHsl)
        )
        colorSchemeAddUiState = colorSchemeAddUiState.copy(
            colorSchemeColors = tetradicColorsRgb,
            selectedColorScheme = ColorSchemeEnum.TETRADIC
        )
    }
}

data class ColorSchemeUiState(
    val colorSchemeDetails: ColorSchemeDetails = ColorSchemeDetails(),
    val paints: List<MiniaturePaint> = listOf(),
    val mainColorRgb: IntArray = intArrayOf(0,0,0),
    val showColorPicker: Boolean = false,
    val colorSchemeColors: List<IntArray> = listOf(),
    val selectedColorScheme: ColorSchemeEnum = ColorSchemeEnum.NONE
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