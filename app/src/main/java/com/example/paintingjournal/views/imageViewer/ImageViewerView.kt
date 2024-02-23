package com.example.paintingjournal.views.imageViewer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

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
                    canNavigateBack = canNavigateBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = viewModel.imageViewerUiState.imageDetails.imageUri,
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(rememberZoomState()),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = "Selected image",
                )
            }
        }
    }
}

@Composable
fun ImageViewerBody(

) {

}