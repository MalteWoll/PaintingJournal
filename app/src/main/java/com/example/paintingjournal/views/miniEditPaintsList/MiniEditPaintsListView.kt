package com.example.paintingjournal.views.miniEditPaintsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object MiniatureEditPaintsListDestination : NavigationDestination {
    override val route = "mini_edit_paints_list"
    override val titleRes = R.string.mini_edit_paints_list_title
    const val miniatureArg = "miniatureId"
    val routeWithArgs = "$route/{$miniatureArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniEditPaintsListView (
   navigateBack: () -> Unit,
   navigateToPaintAdd: () -> Unit,
   modifier: Modifier = Modifier,
   canNavigateBack: Boolean = false,
   viewModel: MiniEditPaintsListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(MiniatureEditPaintsListDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToPaintAdd() },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        MiniEditPaintsListBody(
            selectablePaintList = viewModel.miniatureEditPaintsListUiState.selectablePaintList,
            onPaintClick = {},
            onCheckboxClick = { viewModel.changePaintSelectionState(it) },
            miniatureEditPaintsListUiState = viewModel.miniatureEditPaintsListUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun MiniEditPaintsListBody(
    selectablePaintList: List<SelectablePaintDetails>,
    onPaintClick: (Long) -> Unit,
    onCheckboxClick: (SelectablePaintDetails) -> Unit,
    miniatureEditPaintsListUiState: MiniatureEditPaintsListUiState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(selectablePaintList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.paint_list_no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            MiniEditPaintList(
                selectablePaintList = selectablePaintList,
                onPaintClick = {onPaintClick(it.id)},
                onCheckboxClick = { onCheckboxClick(it) },
                miniatureEditPaintsListUiState = miniatureEditPaintsListUiState,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun MiniEditPaintList(
    selectablePaintList: List<SelectablePaintDetails>,
    onPaintClick: (SelectablePaintDetails) -> Unit,
    onCheckboxClick: (SelectablePaintDetails) -> Unit,
    miniatureEditPaintsListUiState: MiniatureEditPaintsListUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = miniatureEditPaintsListUiState.selectablePaintList, key = { it.id }) { paint ->
            MiniEditPaintItem(
                selectablePaint = paint,
                onCheckboxClick = {onCheckboxClick(it) } ,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onPaintClick(paint) })
        }
    }
}

@Composable
private fun MiniEditPaintItem(
    selectablePaint: SelectablePaintDetails,
    onCheckboxClick: (SelectablePaintDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Checkbox(
                        checked = selectablePaint.isSelected,
                        onCheckedChange = { onCheckboxClick(selectablePaint) }
                    )
                }
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectablePaint.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = selectablePaint.manufacturer,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}