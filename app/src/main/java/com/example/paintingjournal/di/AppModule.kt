package com.example.paintingjournal.di

import com.example.paintingjournal.data.MiniaturesRepository
import com.example.paintingjournal.data.OfflineMiniaturesRepository
import com.example.paintingjournal.views.mainMenu.MainMenuViewModel
import com.example.paintingjournal.views.miniList.MiniListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ViewModels
    viewModel { MainMenuViewModel() }
    viewModel { MiniListViewModel() }
}