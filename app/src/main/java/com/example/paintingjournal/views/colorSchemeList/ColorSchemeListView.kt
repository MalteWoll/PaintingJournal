package com.example.paintingjournal.views.colorSchemeList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object ColorSchemeListDestination: NavigationDestination {
    override val route: String = "color_scheme_list"
    override val titleRes: Int = R.string.color_scheme_list_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeListView(
    navigateToColorSchemeAdd: () -> Unit,
    navigateBack: () -> Unit,
    navigateToColorSchemeEntry: (Long) -> Unit,
    canNavigateBack: Boolean = false,
    viewModel: ColorSchemeListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

    }
    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(id = R.string.color_scheme_list_title),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToColorSchemeAdd() },
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
        ColorSchemeListBody(
            colorSchemeListUiState = viewModel.colorSchemeListUiState,
            onColorSchemeClick = navigateToColorSchemeEntry,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun ColorSchemeListBody(
    colorSchemeListUiState: ColorSchemeListUiState,
    onColorSchemeClick: (Long) -> Unit,
    viewModel: ColorSchemeListViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(colorSchemeListUiState.colorSchemeList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.color_scheme_list_no_entries),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {

        }
    }
}

@Composable
private fun ColorSchemeList(
    colorSchemeList: List<ColorScheme>,
    onColorSchemeClick: (ColorScheme) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = colorSchemeList, key = { it.id}) { colorScheme ->

        }
    }
}