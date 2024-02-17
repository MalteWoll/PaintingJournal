package com.example.paintingjournal.views.paintList

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object PaintListDestination : NavigationDestination {
    override val route = "paint_list"
    override val titleRes = R.string.paint_list_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintListView(
    navigateToPaintAdd: () -> Unit,
    navigateBack: () -> Unit,
    navigateToPaintEntry: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: PaintListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val paintListUiState by viewModel.paintListUiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.paint_list_title),
                    canNavigateBack = true
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
            }
        ) { innerPadding ->
            PaintListBody(
                paintList = paintListUiState.paintList,
                onPaintClick = navigateToPaintEntry,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun PaintListBody(
    paintList: List<MiniaturePaint>,
    onPaintClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(paintList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.paint_list_no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            PaintList(
                paintList = paintList, 
                onPaintclick = { onPaintClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
        }
    }
}

@Composable
private fun PaintList(
    paintList: List<MiniaturePaint>,
    onPaintclick: (MiniaturePaint) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = paintList, key = { it.id }) { paint ->
            PaintItem(paint = paint,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onPaintclick(paint) })
        }
    }
}

@Composable
private fun PaintItem(
    paint: MiniaturePaint,
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
                Text(
                    text = paint.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = paint.manufacturer,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}