package com.example.paintingjournal.views.imageViewer

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerView(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = false,
    viewModel: ImageViewerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getImage()
        viewModel.createBitmap(context)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = "",
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateBack
                )
            },
            floatingActionButton = {
                if(!viewModel.imageViewerUiState.showPopup) {
                    FloatingActionButton(
                        onClick = { viewModel.togglePopupState() },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_title),
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                ) {
                    viewModel.imageViewerUiState.imageBitmap?.let {
                        AsyncImage(
                            model = viewModel.imageViewerUiState.imageBitmap,
                            modifier = Modifier
                                .fillMaxSize()
                                .zoomable(rememberZoomState()),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Selected image",
                        )
                    }
                    ImageViewerPopup(
                        imageViewerUiState = viewModel.imageViewerUiState,
                        onClosePopup = { viewModel.togglePopupState() },
                        onApplyGrayScale = { viewModel.applyGrayScale() }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageViewerPopup(
    imageViewerUiState: ImageViewerUiState,
    onClosePopup: () -> Unit,
    onApplyGrayScale: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(imageViewerUiState.showPopup) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium))
                .background(Color.White)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.image_viewer_popup_title),
                style = MaterialTheme.typography.titleMedium
            )
            Button(onClick = { onApplyGrayScale() }) {
                Text(text = stringResource(id = R.string.image_viewer_popup_apply_grayscale))
            }
            Button(onClick = { /*TODO*/ }) {
                
            }
            Button(onClick = { onClosePopup() }) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    }
}