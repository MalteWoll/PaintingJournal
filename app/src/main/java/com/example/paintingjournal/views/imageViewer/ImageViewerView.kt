package com.example.paintingjournal.views.imageViewer

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
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
    const val optionalEntryType = "entryType"
    val routeWithArgs = "$route/{$imageArg}?${optionalEntryType}={${optionalEntryType}}"
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
        viewModel.getData(context)
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
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                var size by remember { mutableStateOf(IntSize.Zero) }
                Box(
                    modifier = Modifier
                ) {
                    viewModel.imageViewerUiState.imageBitmap?.let {
                        AsyncImage(
                            model = viewModel.imageViewerUiState.imageBitmap,
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Selected image",
                            onSuccess = { viewModel.setImageSize(size) },
                            modifier = Modifier
                                .fillMaxSize()
                                .zoomable(rememberZoomState())
                                .onGloballyPositioned { coordinates ->
                                    size = coordinates.size
                                }
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        viewModel.createBitmapAroundPosition(it)
                                    }
                                }
                        )
                    }
                    ColorPreview(
                        hexColor = viewModel.imageViewerUiState.hexColor,
                        showPreview = viewModel.imageViewerUiState.showColorPreview,
                        showAddButton = viewModel.imageViewerUiState.showAddToPaintButton,
                        addColorToPaint = { viewModel.onUpdateColorForPaint(it) },
                        onNavigateBack = navigateBack
                    )
                    MagnifiedImage(
                        magnifiedImage = viewModel.imageViewerUiState.magnifiedBitmap,
                        showMagnifier = viewModel.imageViewerUiState.showMagnifiedPreview
                    )
                    ImageViewerPopup(
                        imageViewerUiState = viewModel.imageViewerUiState,
                        onApplyGrayScale = { viewModel.applyGrayScale() },
                        onResetImage = { viewModel.resetImage() },
                        onToggleMagnifier = { viewModel.togglePreviewState() },
                        onToggleColorPreview = { viewModel.toggleColorPreview() },
                        onChangeMagnificationPixelSize = { viewModel.changeMagnificationPixelSize(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageViewerPopup(
    imageViewerUiState: ImageViewerUiState,
    onApplyGrayScale: () -> Unit,
    onResetImage: () -> Unit,
    onToggleMagnifier: () -> Unit,
    onChangeMagnificationPixelSize: (Int) -> Unit,
    onToggleColorPreview: () -> Unit,
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { onToggleMagnifier() }) {
                    Text(text = stringResource(id = R.string.image_viewer_popup_show_magnifier))
                }
                IconButton(onClick = { onChangeMagnificationPixelSize(-1) }) {
                    Icon(
                        Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "",
                        modifier = Modifier
                    )
                }
                Text(text = imageViewerUiState.magnificationPixelSize.toString())
                IconButton(onClick = { onChangeMagnificationPixelSize(+1) }) {
                    Icon(
                        Icons.Outlined.KeyboardArrowRight,
                        contentDescription = "",
                        modifier = Modifier
                    )
                }
            }
            Button(onClick = { onToggleColorPreview() }) {
                Text(text = stringResource(id = R.string.image_viewer_popup_toggle_color_preview))
            }
            Button(onClick = { onApplyGrayScale() }) {
                Text(text = stringResource(id = R.string.image_viewer_popup_apply_grayscale))
            }
            Button(onClick = { onResetImage() }) {
                Text(text = stringResource(id = R.string.image_viewer_popup_reset_image))
            }
        }
    }
}

@Composable
fun MagnifiedImage(
    magnifiedImage: Bitmap?,
    showMagnifier: Boolean,
    modifier: Modifier = Modifier
) {
    if(showMagnifier) {
        magnifiedImage?.let {
            Column(
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    AsyncImage(
                        model = magnifiedImage,
                        contentDescription = "Magnified image",
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .border(BorderStroke(2.dp, Color.White))
                            .size(150.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ColorPreview(
    hexColor: String,
    showPreview: Boolean,
    showAddButton: Boolean,
    addColorToPaint: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(showPreview && hexColor != "") {
        Column {
            Row {
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(hexColor.toColorInt()))
                ) {
                    if(showAddButton) {
                        IconButton(onClick = {
                            addColorToPaint(hexColor)
                            onNavigateBack()
                        }) {
                            Icon(
                                Icons.Outlined.AddCircle,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(80.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}