package com.example.paintingjournal.views.colorSchemeList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.ColorScheme
import com.example.paintingjournal.model.SaveStateEnum
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.views.colorSchemeAdd.ColorSchemeDetails

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
        if(colorSchemeListUiState.colorSchemeDetailsList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.color_scheme_list_no_entries),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            ColorSchemeList(
                colorSchemeList = colorSchemeListUiState.colorSchemeDetailsList,
                onColorSchemeClick = { onColorSchemeClick(it.id) }
            )
        }
    }
}

@Composable
private fun ColorSchemeList(
    colorSchemeList: List<ColorSchemeDetails>,
    onColorSchemeClick: (ColorSchemeDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = colorSchemeList, key = { it.id }) { colorScheme ->
            ColorSchemeItem(
                colorScheme = colorScheme,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onColorSchemeClick(colorScheme) }
            )
        }
    }
}

@Composable
private fun ColorSchemeItem(
    colorScheme: ColorSchemeDetails,
    modifier: Modifier = Modifier
) {
    if(colorScheme.saveState == SaveStateEnum.SAVED) {
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
                        text = colorScheme.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.weight(1f))
                    ColorSchemeItemColorSquares(colors = colorScheme.colorList)
                }
            }
        }
    }
}

@Composable
private fun ColorSchemeItemColorSquares(
    colors: List<IntArray>,
    modifier: Modifier = Modifier
) {
    Row {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(RectangleShape)
                    .background(
                        Color(
                            red = color[0],
                            green = color[1],
                            blue = color[2]
                        )
                    )
            )
        }
    }
}