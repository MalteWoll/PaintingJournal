package com.example.paintingjournal.views.paintList

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object PaintListDestination : NavigationDestination {
    override val route = "paint_list"
    override val titleRes = R.string.paint_list_title
}

@Composable
fun PaintListView(
    navigateToPaintAdd: () -> Unit,
    navigateBack: () -> Unit,
    navigateToPaintEntry: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: PaintListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
}