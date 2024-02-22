package com.example.paintingjournal.views.mainMenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.model.SaveStateEnum
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val miniaturesRepository: MiniaturesRepository
) : ViewModel() {
    init {
        removeCancelledEntries()
    }

    private fun removeCancelledEntries() {
        viewModelScope.launch {
            val miniatureList = miniaturesRepository.getAllMiniaturesStream()
                .filterNotNull()
                .first()
                .toList()
            miniatureList.forEach{ miniature ->
                if(miniature.saveState == SaveStateEnum.NEW) {
                    miniaturesRepository.deleteMiniature(miniature)
                }
            }
        }
    }
}