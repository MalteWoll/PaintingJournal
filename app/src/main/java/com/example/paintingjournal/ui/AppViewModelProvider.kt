package com.example.paintingjournal.ui

import android.text.Spannable.Factory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paintingjournal.PaintingJournal
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeAddViewModel
import com.example.paintingjournal.views.colorSchemeAddPaintList.ColorSchemeAddPaintListViewModel
import com.example.paintingjournal.views.colorSchemeList.ColorSchemeListViewModel
import com.example.paintingjournal.views.imageViewer.ImageViewerViewModel
import com.example.paintingjournal.views.mainMenu.MainMenuViewModel
import com.example.paintingjournal.views.miniAdd.MiniAddViewModel
import com.example.paintingjournal.views.miniDetail.MiniDetailViewModel
import com.example.paintingjournal.views.miniEdit.MiniEditViewModel
import com.example.paintingjournal.views.miniEditPaintsList.MiniEditPaintsListViewModel
import com.example.paintingjournal.views.miniList.MiniListViewModel
import com.example.paintingjournal.views.paintAdd.PaintAddViewModel
import com.example.paintingjournal.views.paintDetail.PaintDetailViewModel
import com.example.paintingjournal.views.paintEdit.PaintEditViewModel
import com.example.paintingjournal.views.paintList.PaintListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ColorSchemeAddViewModel(
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.colorSchemeRepository,
                paintingJournalApplication().container.colorService,
            )
        }
        initializer {
            ColorSchemeAddPaintListViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.colorSchemeRepository
            )
        }
        initializer {
            ColorSchemeListViewModel(
                paintingJournalApplication().container.colorSchemeRepository
            )
        }
        initializer {
            ImageViewerViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.imagesRepository,
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.imageManipulationService,
                paintingJournalApplication().container.colorService
            )
        }
        initializer {
            MainMenuViewModel(
                paintingJournalApplication().container.miniaturesRepository,
                paintingJournalApplication().container.imagesRepository,
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.importService,
                paintingJournalApplication().container.preferencesService
            )
        }
        initializer {
            MiniListViewModel(paintingJournalApplication().container.miniaturesRepository)
        }
        initializer {
            MiniAddViewModel(
                paintingJournalApplication().container.miniaturesRepository,
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.imagesRepository
            )
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
                paintingJournalApplication().container.miniaturesRepository,
                paintingJournalApplication().container.imagesRepository,
                paintingJournalApplication().container.miniaturesService
            )
        }
        initializer {
            MiniEditPaintsListViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.miniaturesRepository,
            )
        }
        initializer {
            PaintAddViewModel(
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.imagesRepository,
                paintingJournalApplication().container.miniaturesService
            )
        }
        initializer {
            PaintDetailViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.paintsRepository
            )
        }
        initializer {
            PaintEditViewModel(
                this.createSavedStateHandle(),
                paintingJournalApplication().container.paintsRepository,
                paintingJournalApplication().container.imagesRepository,
                paintingJournalApplication().container.miniaturesService
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