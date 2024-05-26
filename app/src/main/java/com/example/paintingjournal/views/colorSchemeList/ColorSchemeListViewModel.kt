package com.example.paintingjournal.views.colorSchemeList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paintingjournal.data.ColorSchemeRepository
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.services.ColorService
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeDetails
import com.example.paintingjournal.views.colorSchemeAdd.toColorSchemeDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ColorSchemeListViewModel(
    val colorSchemeRepository: ColorSchemeRepository,
    val colorService: ColorService
): ViewModel() {
    var colorSchemeListUiState by mutableStateOf(ColorSchemeListUiState())
        private set

    fun getData() {
        runBlocking {
            val colorSchemeList = colorSchemeRepository.getAllColorSchemesStream()
                .filterNotNull()
                .first()
                .toList()

            val colorSchemeDetailsList: MutableList<ColorSchemeDetails> = mutableListOf()
            colorSchemeList.forEach { colorScheme ->
                val colorSchemeDetails = colorScheme.toColorSchemeDetails()
                val hexColors = colorSchemeRepository.getColorHexesForColorScheme(colorScheme.id)
                    .filterNotNull()
                    .first()
                    .toList()
                hexColors.forEach { hexColor ->
                    colorSchemeDetails.colorList.add(colorService.getRgbFromHex(hexColor.hex))
                }
                colorSchemeDetailsList.add(colorSchemeDetails)
            }
            colorSchemeListUiState = colorSchemeListUiState.copy(colorSchemeDetailsList = colorSchemeDetailsList)
        }
    }
}

data class ColorSchemeListUiState(
    val colorSchemeDetailsList: List<ColorSchemeDetails> = listOf()
)