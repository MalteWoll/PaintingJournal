package com.example.paintingjournal.views.paintAdd

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.data.ComposeFileProvider
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.ui.composables.ImagePicker
import com.example.paintingjournal.ui.composables.ImageSelection
import kotlinx.coroutines.launch

object PaintAddDestination: NavigationDestination {
    override val route = "paint_add"
    override val titleRes = R.string.paint_add_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintAddView(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToImageViewer: (Long) -> Unit,
    viewModel: PaintAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.paint_add_title),
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateBack
                )
            }
        ) { innerPadding ->
            PaintEntryBody(
                miniaturePaintUiState = viewModel.miniaturePaintUiState,
                onMiniaturePaintValueChanged = viewModel::updateUiState,
                onSaveClicked = {
                    coroutineScope.launch {
                        viewModel.saveMiniaturePaint()
                        navigateBack()
                    }
                },
                onSaveImage = {
                    viewModel.addImageToList(it)
                },
                onRemoveImage = {
                    viewModel.removeImageFromList(it)
                },
                switchEditMode = {
                    viewModel.switchEditMode()
                },
                navigateToImageViewer = navigateToImageViewer,
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun PaintEntryBody(
    miniaturePaintUiState: MiniaturePaintUiState,
    onMiniaturePaintValueChanged: (MiniaturePaintDetails) -> Unit,
    onSaveClicked: () -> Unit,
    onSaveImage: (Uri?) -> Unit,
    onRemoveImage: (Image) -> Unit,
    switchEditMode: () -> Unit,
    navigateToImageViewer: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        MiniaturePaintInputForm(
            miniaturePaintDetails = miniaturePaintUiState.miniaturePaintDetails,
            onValueChanged = onMiniaturePaintValueChanged,
            modifier = Modifier.fillMaxWidth()
        )
        ImageSelection(onSaveImage = onSaveImage)
        ImagesRow(
            imageList = miniaturePaintUiState.imageList,
            onDelete = onRemoveImage,
            showEditIcon = true,
            switchEditMode = { switchEditMode() },
            canEdit = miniaturePaintUiState.canEdit,
            navigateToImageViewer = {navigateToImageViewer(it)}
        )
        Button(
            onClick = onSaveClicked,
            enabled = miniaturePaintUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.save_action))
        }
    }
}

@Composable
fun MiniaturePaintInputForm(
    miniaturePaintDetails: MiniaturePaintDetails,
    modifier: Modifier = Modifier,
    onValueChanged: (MiniaturePaintDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = miniaturePaintDetails.name,
            onValueChange = { onValueChanged(miniaturePaintDetails.copy(name = it)) },
            label = { Text(stringResource(id = R.string.paint_add_form_name)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = miniaturePaintDetails.manufacturer,
            onValueChange = { onValueChanged(miniaturePaintDetails.copy(manufacturer = it)) },
            label = { Text(stringResource(id = R.string.paint_add_form_manufacturer)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = miniaturePaintDetails.description,
            onValueChange = { onValueChanged(miniaturePaintDetails.copy(description = it)) },
            label = { Text(stringResource(id = R.string.paint_add_form_description)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = miniaturePaintDetails.type,
            onValueChange = { onValueChanged(miniaturePaintDetails.copy(type = it)) },
            label = { Text(stringResource(id = R.string.paint_add_form_type)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}

@Composable
fun TakeImage(
    modifier: Modifier = Modifier,
    onSaveImage: (Uri?) -> Unit
) {
    val context = LocalContext.current

    var hasImage by remember {
        mutableStateOf(false)
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        hasImage = success
        onSaveImage(imageUri)
    }

    Button(
        modifier = Modifier,
        onClick = {
            val uri = ComposeFileProvider.getImageUri(context)
            imageUri = uri
            cameraLauncher.launch(uri)
        },
    ) {
        Text(
            text = stringResource(id = R.string.take_photo)
        )
    }
}

@Composable
fun ImagesRow(
    imageList: List<Image>,
    onDelete: (Image) -> Unit,
    showEditIcon: Boolean,
    switchEditMode: () -> Unit,
    canEdit: Boolean,
    navigateToImageViewer: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (imageList.isNotEmpty()) {
        Column {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            ) {
                imageList.forEach { image ->
                    Box {
                        AsyncImage(
                            model = image.imageUri,
                            modifier = Modifier
                                .width(100.dp)
                                .clickable { navigateToImageViewer(image.id) },
                            contentScale = ContentScale.Fit,
                            contentDescription = "Selected image",
                        )
                        if (canEdit) {
                            IconButton(
                                onClick = { onDelete(image) },
                                modifier = Modifier
                                    .size(100.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(100.dp),
                                )
                            }
                        }
                    }
                }
            }
            if(showEditIcon) {
                IconButton(onClick = { switchEditMode() }) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "",
                        modifier = Modifier
                    )
                }
            }
        }
    }
}