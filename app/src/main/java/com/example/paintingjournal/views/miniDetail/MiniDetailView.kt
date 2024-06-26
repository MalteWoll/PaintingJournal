package com.example.paintingjournal.views.miniDetail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.ui.composables.MiniPaintingSteps
import com.example.paintingjournal.views.miniAdd.ExpandablePaintingStep
import com.example.paintingjournal.views.miniAdd.MiniatureUiState
import com.example.paintingjournal.views.miniAdd.PaintRow
import com.example.paintingjournal.views.miniAdd.toMiniature
import com.example.paintingjournal.views.paintAdd.ImagesRow
import kotlinx.coroutines.launch

object MiniatureDetailsDestination : NavigationDestination {
    override val route = "mini_details"
    override val titleRes = R.string.mini_details_title
    const val miniatureIdArg = "miniatureId"
    val routeWithArgs = "$route/{$miniatureIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniDetailView(
    navigateToEditMiniature: (Long) -> Unit,
    navigateBack: () -> Unit,
    navigateToPaintDetails: (Long) -> Unit,
    navigateToImageViewer: (Long, Int) -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    viewModel: MiniDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.miniatureDetailsUiState
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(R.string.mini_details_title),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditMiniature(uiState.miniatureDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_title),
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        MiniatureDetailsBody(
            miniatureDetailsUiState = uiState,
            onDelete = {
                       coroutineScope.launch {
                           viewModel.deleteMiniature()
                           navigateBack()
                       }
            },
            navigateToImageViewer = navigateToImageViewer,
            togglePaintingStepExpand = {viewModel.togglePaintingStepExpand(it)},
            navigateToPaint = navigateToPaintDetails,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun MiniatureDetailsBody(
    miniatureDetailsUiState: MiniatureUiState,
    navigateToImageViewer: (Long, Int) -> Unit,
    navigateToPaint: (Long) -> Unit,
    togglePaintingStepExpand: (ExpandablePaintingStep) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        MiniatureDetails(
            miniature = miniatureDetailsUiState.miniatureDetails.toMiniature(),
            modifier = Modifier.fillMaxWidth()
        )
        ImagesRow(
            imageList = miniatureDetailsUiState.imageList,
            onDelete = {},
            showEditIcon = false,
            switchEditMode = {},
            canEdit = false,
            navigateToImageViewer = navigateToImageViewer,
            entryType = 0
        )
        PaintRow(
            miniatureUiState = miniatureDetailsUiState,
            paintList = miniatureDetailsUiState.paintList,
            removePaint = {},
            onPaintClick = {navigateToPaint(it.id)},
            navigateToPaintList = {},
            canEdit = false)
        MiniPaintingSteps(
            paintingStepList = miniatureDetailsUiState.expandablePaintingStepList,
            isEditable = false,
            onToggleExpand = togglePaintingStepExpand,
            onPaintingStepValueChanged = {},
            addPaintingStep = {},
            removePaintingStep = {},
            navigateToImageViewer = navigateToImageViewer,
            onDeleteImage = {},
            onSwitchImageEditMode = {},
            onSaveImage = {}
        )
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.delete))
        }
        if(deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun MiniatureDetails(
    miniature: Miniature,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.padding_medium)
            )
        ) {
            MiniatureDetailsRow(
                labelResId = R.string.miniature,
                miniatureDetail = miniature.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            MiniatureDetailsRow(
                labelResId = R.string.manufacturer,
                miniatureDetail = miniature.manufacturer,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            MiniatureDetailsRow(
                labelResId = R.string.faction,
                miniatureDetail = miniature.faction,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

@Composable
private fun MiniatureDetailsRow(
    @StringRes labelResId: Int,
    miniatureDetail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResId))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = miniatureDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        })
}