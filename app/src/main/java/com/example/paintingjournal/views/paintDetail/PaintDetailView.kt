package com.example.paintingjournal.views.paintDetail

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.views.miniDetail.DeleteConfirmationDialog
import com.example.paintingjournal.views.paintAdd.MiniaturePaintImages
import com.example.paintingjournal.views.paintAdd.MiniaturePaintUiState
import com.example.paintingjournal.views.paintAdd.toPaint
import kotlinx.coroutines.launch

object PaintDetailsDestination : NavigationDestination {
    override val route = "paint_details"
    override val titleRes = R.string.paint_details_title
    const val paintIdArg = "paintId"
    val routeWithArgs = "$route/{$paintIdArg}"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintDetailView(
    navigateToEditPaint: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaintDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.miniaturePaintDetailsUiState
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(R.string.mini_details_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditPaint(uiState.miniaturePaintDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_title),
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        MiniaturePaintDetailsBody(
            miniaturePaintDetailsUiState = uiState,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deletePaint()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun MiniaturePaintDetailsBody(
    miniaturePaintDetailsUiState: MiniaturePaintUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        MiniaturePaintDetails(
            paint = miniaturePaintDetailsUiState.miniaturePaintDetails.toPaint(),
            modifier = Modifier.fillMaxWidth()
        )
        MiniaturePaintImages(
            imageList = miniaturePaintDetailsUiState.imageList,
            canEdit = false,
            onDelete = {}
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
fun MiniaturePaintDetails(
    paint: MiniaturePaint,
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
            MiniaturePaintDetailsRow(
                labelResId = R.string.paint,
                miniaturePaintDetail = paint.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            MiniaturePaintDetailsRow(
                labelResId = R.string.manufacturer,
                miniaturePaintDetail = paint.manufacturer,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            MiniaturePaintDetailsRow(
                labelResId = R.string.description,
                miniaturePaintDetail = paint.description,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
            MiniaturePaintDetailsRow(
                labelResId = R.string.type,
                miniaturePaintDetail = paint.type,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

@Composable
private fun MiniaturePaintDetailsRow(
    @StringRes labelResId: Int,
    miniaturePaintDetail : String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(stringResource(labelResId))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = miniaturePaintDetail, fontWeight = FontWeight.Bold)
    }
}