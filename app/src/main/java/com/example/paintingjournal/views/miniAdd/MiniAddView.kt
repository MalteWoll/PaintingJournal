package com.example.paintingjournal.views.miniAdd

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.ui.composables.ImageSelection
import com.example.paintingjournal.ui.composables.MiniPaintingSteps
import com.example.paintingjournal.views.paintAdd.ImagesRow
import com.example.paintingjournal.views.paintAdd.TakeImage
import kotlinx.coroutines.launch

object MiniAddDestination: NavigationDestination {
    override val route = "mini_add"
    override val titleRes = R.string.mini_add_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniAddView(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    navigateToPaintList: (Int) -> Unit,
    navigateToPaintDetails: (Long) -> Unit,
    navigateToImageViewer: (Long) -> Unit,
    viewModel: MiniAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getPaintsForMiniature()
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.mini_add_title),
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateBack
                )
            }
        ) { innerPadding ->
            MiniatureEntryBody(
                miniatureUiState = viewModel.miniatureUiState,
                onMiniatureValueChanged = viewModel::updateUiState,
                onSaveClicked = {
                                coroutineScope.launch {
                                    viewModel.saveMiniature()
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
                navigateToPaintList = {
                    navigateToPaintList(it)
                },
                navigateToPaint =
                    navigateToPaintDetails,
                navigateToImageViewer =
                    navigateToImageViewer,
                onAddPaintingStep = { viewModel.addPaintingStepToList() },
                onTogglePaintingStepExpand = { viewModel.togglePaintingStepExpand(it) },
                onPaintingStepValueChanged = viewModel::updatePaintStepUiState,
                onRemovePaintingStep = { viewModel.removePaintingStepFromList(it) },
                onSavePaintingStepImage = { viewModel.addImageToPaintingStep(it) },
                onDeletePaintingStepImage = { viewModel.removeImageFromPaintingStep(it) },
                onSwitchPaintingStepImageEditMode = { viewModel.togglePaintingStepImageEditMode(it) },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MiniatureEntryBody(
    miniatureUiState: MiniatureUiState,
    onMiniatureValueChanged: (MiniatureDetails) -> Unit,
    onSaveClicked: () -> Unit,
    onSaveImage: (Uri?) -> Unit,
    onRemoveImage: (Image) -> Unit,
    switchEditMode: () -> Unit,
    navigateToPaintList: (Int) -> Unit,
    navigateToPaint: (Long) -> Unit,
    navigateToImageViewer: (Long) -> Unit,
    onAddPaintingStep: () -> Unit,
    onRemovePaintingStep: (ExpandablePaintingStep) -> Unit,
    onTogglePaintingStepExpand: (ExpandablePaintingStep) -> Unit,
    onPaintingStepValueChanged: (ExpandablePaintingStep) -> Unit,
    onSavePaintingStepImage: (PaintingStepIdAndUri) -> Unit,
    onDeletePaintingStepImage: (PaintingStepIdAndImage) -> Unit,
    onSwitchPaintingStepImageEditMode: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        MiniatureInputForm(
            miniatureDetails = miniatureUiState.miniatureDetails,
            onValueChanged = onMiniatureValueChanged,
            modifier = Modifier.fillMaxWidth()
        )
        ImageSelection(onSaveImage = onSaveImage)
        ImagesRow(
            imageList = miniatureUiState.imageList,
            onDelete = onRemoveImage,
            showEditIcon = true,
            switchEditMode = { switchEditMode() },
            canEdit = miniatureUiState.canEdit,
            navigateToImageViewer = {navigateToImageViewer(it)}
        )
        PaintRow(
            paintList = miniatureUiState.paintList,
            removePaint = {},
            onPaintClick = {navigateToPaint(it.id)},
            navigateToPaintList = navigateToPaintList,
            miniatureUiState = miniatureUiState,
            canEdit = true
        )
        MiniPaintingSteps(
            paintingStepList = miniatureUiState.expandablePaintingStepList,
            isEditable = true,
            onToggleExpand = onTogglePaintingStepExpand,
            onPaintingStepValueChanged = onPaintingStepValueChanged,
            addPaintingStep = onAddPaintingStep,
            removePaintingStep = onRemovePaintingStep,
            navigateToImageViewer = navigateToImageViewer,
            onDeleteImage = onDeletePaintingStepImage,
            onSwitchImageEditMode = onSwitchPaintingStepImageEditMode,
            onSaveImage = onSavePaintingStepImage
        )
        Button(
            onClick = onSaveClicked,
            enabled = miniatureUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.save_action))
            }
    }
}

@Composable
fun PaintRow(
    paintList: List<MiniaturePaint>,
    miniatureUiState: MiniatureUiState,
    removePaint: (MiniaturePaint) -> Unit,
    onPaintClick: (MiniaturePaint) -> Unit,
    navigateToPaintList: (Int) -> Unit,
    canEdit: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(id = R.string.mini_section_paints),
            style = MaterialTheme.typography.bodyLarge
        )
        if(canEdit) {
            Button(
                onClick = { navigateToPaintList(miniatureUiState.miniatureDetails.id.toInt()) },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(id = R.string.mini_edit_paint_list_button))
            }
        }
        if (paintList.isNotEmpty()) {
            Row {
                paintList.forEach { paint ->
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { onPaintClick(paint) }
                    ) {
                        AsyncImage(
                            model = paint.previewImageUri,
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "",
                        )
                        Text(
                            text = paint.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
    Divider()
}

@Composable
fun MiniatureInputForm(
    miniatureDetails: MiniatureDetails,
    modifier: Modifier = Modifier,
    onValueChanged: (MiniatureDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(id = R.string.mini_section_general),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = miniatureDetails.name,
            onValueChange = { onValueChanged(miniatureDetails.copy(name = it)) },
            label = { Text(stringResource(id = R.string.mini_add_form_name)) },
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
            value = miniatureDetails.manufacturer,
            onValueChange = { onValueChanged(miniatureDetails.copy(manufacturer = it)) },
            label = { Text(stringResource(id = R.string.mini_add_form_manufacturer)) },
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
            value = miniatureDetails.faction,
            onValueChange = { onValueChanged(miniatureDetails.copy(faction = it)) },
            label = { Text(stringResource(id = R.string.mini_add_form_faction)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Divider()
    }
}