package com.example.paintingjournal.views.paintEdit

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
import com.example.paintingjournal.views.paintAdd.PaintEntryBody
import kotlinx.coroutines.launch

object PaintEditDestination : NavigationDestination {
    override val route = "paint_edit"
    override val titleRes = R.string.paint_edit_title
    const val paintArg = "paintId"
    val routeWithArgs = "$route/{$paintArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintEditView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToImageViewer: (Long, Int) -> Unit,
    canNavigateBack: Boolean = false,
    viewModel: PaintEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(PaintEditDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        PaintEntryBody(
            miniaturePaintUiState = viewModel.miniaturePaintUiState,
            onMiniaturePaintValueChanged = viewModel::updateUiState,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.updateMiniaturePaint()
                    navigateBack()
                }
            },
            onSaveImage = { viewModel.addImageToList(it)},
            onRemoveImage = { viewModel.removeImageFromList(it) },
            switchEditMode = { viewModel.switchEditMode() },
            navigateToImageViewer = navigateToImageViewer,
            onColorChanged = { viewModel.changeColor(it) },
            entryType = 1,
            onToggleColorPicker = {viewModel.toggleColorPicker()},
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}