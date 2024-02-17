package com.example.paintingjournal.views.miniList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object MiniListDestination : NavigationDestination {
    override val route = "mini_list"
    override val titleRes = R.string.mini_list_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniListView(
    navigateToMiniAdd: () -> Unit,
    navigateBack: () -> Unit,
    navigateToMiniatureEntry: (Int) -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: MiniListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val miniListUiState by viewModel.miniListUiState.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.mini_list_title),
                    canNavigateBack = true
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigateToMiniAdd() },
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
            MiniListBody(
                miniatureList = miniListUiState.miniatureList,
                onMiniatureClick = navigateToMiniatureEntry,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun MiniListBody(
    miniatureList: List<Miniature>,
    onMiniatureClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(miniatureList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.mini_list_no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            MiniatureList(
                miniatureList = miniatureList, 
                onMiniatureClick = { onMiniatureClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun MiniatureList(
    miniatureList: List<Miniature>,
    onMiniatureClick: (Miniature) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = miniatureList, key = { it.id }) { miniature ->
            MiniatureItem(miniature = miniature,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onMiniatureClick(miniature) })
        }
    }
}

@Composable
private fun MiniatureItem(
    miniature: Miniature,
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
                    text = miniature.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = miniature.manufacturer,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}