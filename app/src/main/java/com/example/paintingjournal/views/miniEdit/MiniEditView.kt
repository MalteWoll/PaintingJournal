package com.example.paintingjournal.views.miniEdit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.views.miniAdd.MiniatureEntryBody
import kotlinx.coroutines.launch

object MiniatureEditDestination : NavigationDestination {
    override val route = "mini_edit"
    override val titleRes = R.string.mini_edit_title
    const val miniatureArg = "miniatureId"
    val routeWithArgs = "$route/{$miniatureArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniEditView(
    navigateBack: () -> Unit,
    navigateToPaintList: (Int) -> Unit,
    navigateToPaintDetails: (Long) -> Unit,
    navigateToImageViewer: (Long, Int) -> Unit,
    canNavigateBack: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: MiniEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getPaintsForMiniature()
    }

    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(MiniatureEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        MiniatureEntryBody(
            miniatureUiState = viewModel.miniatureUiState,
            onMiniatureValueChanged = viewModel::updateUiState,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.updateMiniature()
                    navigateBack()
                }
            },
            onSaveImage = { viewModel.addImageToList(it) },
            onRemoveImage = { viewModel.removeImageFromList(it) },
            switchEditMode = { viewModel.switchEditMode() },
            navigateToPaintList = { navigateToPaintList(it) },
            navigateToPaint = navigateToPaintDetails,
            navigateToImageViewer = navigateToImageViewer,
            onAddPaintingStep = { viewModel.addPaintingStepToList() },
            onTogglePaintingStepExpand = { viewModel.togglePaintingStepExpand(it) },
            onPaintingStepValueChanged =  viewModel::updateUiState,
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