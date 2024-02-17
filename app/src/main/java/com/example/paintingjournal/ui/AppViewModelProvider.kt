package com.example.paintingjournal.ui

import android.text.Spannable.Factory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paintingjournal.PaintingJournal
import com.example.paintingjournal.views.mainMenu.MainMenuViewModel
import com.example.paintingjournal.views.miniAdd.MiniAddViewModel
import com.example.paintingjournal.views.miniDetail.MiniDetailViewModel
import com.example.paintingjournal.views.miniEdit.MiniEditViewModel
import com.example.paintingjournal.views.miniList.MiniListViewModel
import com.example.paintingjournal.views.paintList.PaintListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MainMenuViewModel()
        }
        initializer {
            MiniListViewModel(paintingJournalApplication().container.miniaturesRepository)
        }
        initializer {
            MiniAddViewModel(paintingJournalApplication().container.miniaturesRepository)
        }
        initializer {
            MiniDetailViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.miniaturesRepository
            )
        }
        initializer {
            MiniEditViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.miniaturesRepository
            )
        }
        initializer {
            PaintListViewModel(
                paintingJournalApplication().container.paintsRepository
            )
        }
    }
}

fun CreationExtras.paintingJournalApplication(): PaintingJournal =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PaintingJournal)