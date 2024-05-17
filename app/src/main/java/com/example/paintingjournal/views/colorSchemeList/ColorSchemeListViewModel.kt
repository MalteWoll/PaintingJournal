package com.example.paintingjournal.views.colorSchemeList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paintingjournal.data.ColorSchemeRepository
import com.example.paintingjournal.model.ColorScheme
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ColorSchemeListViewModel(
    val colorSchemeRepository: ColorSchemeRepository
): ViewModel() {
    var colorSchemeListUiState by mutableStateOf(ColorSchemeListUiState())
        private set

    fun getData() {
        runBlocking {
            val colorSchemeList = colorSchemeRepository.getAllColorSchemesStream()
                .filterNotNull()
                .first()
                .toList()
            colorSchemeListUiState = colorSchemeListUiState.copy(
                colorSchemeList = colorSchemeList
            )
        }
    }
}

data class ColorSchemeListUiState(
    val colorSchemeList: List<ColorScheme> = listOf()
)