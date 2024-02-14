package com.example.paintingjournal.di

import com.example.paintingjournal.views.MainMenu.MainMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ViewModels
    viewModel { MainMenuViewModel() }
}