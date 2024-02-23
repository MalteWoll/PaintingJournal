package com.example.paintingjournal.views.imageViewer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object ImageViewerDestination : NavigationDestination {
    override val route = "image_viewer"
    override val titleRes = R.string.image_viewer
    const val imageArg = "imageId"
    val routeWithArgs = "$route/{$imageArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerView(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = false,
    viewModel: ImageViewerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = "",
                    canNavigateBack = false
                )
            }
        ) { innerPadding ->

        }
    }
}

@Composable
fun ImageViewerBody(

) {

}