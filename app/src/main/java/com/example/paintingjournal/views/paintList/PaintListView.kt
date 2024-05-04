package com.example.paintingjournal.views.paintList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.FilterSortBy
import com.example.paintingjournal.model.FilterSortByEnum
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.SelectableManufacturer
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
    navigateToPaintEntry: (Long) -> Unit,
    canNavigateBack: Boolean = false,
    viewModel: PaintListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.paint_list_title),
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
            }
        ) { innerPadding ->
            PaintListBody(
                paintListUiState = viewModel.paintListUiState,
                onPaintClick = navigateToPaintEntry,
                viewModel = viewModel,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun PaintListBody(
    paintListUiState: PaintListUiState,
    onPaintClick: (Long) -> Unit,
    viewModel: PaintListViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(paintListUiState.paintList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.paint_list_no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            SearchBar(
                onChange = { viewModel.onSearchBarValueChanged(it) },
                searchBarValue = paintListUiState.searchBarValue
            )
            PaintListFilter(
                paintListUiState = paintListUiState,
                onToggleFilter = { viewModel.togglePaintListFilter() },
                onManufacturerCheckboxChange = { viewModel.onManufacturerFilterCheckboxChange(it) },
                onChangeSorting = { viewModel.onChangeSorting(it)}
            )
            PaintList(
                paintList = paintListUiState.filteredPaintList,
                onPaintClick = { onPaintClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun SearchBar(
    onChange: (String) -> Unit,
    searchBarValue: String
) {
    OutlinedTextField(
        value = searchBarValue,
        onValueChange = { onChange(it) },
        label = { Text(stringResource(id = R.string.paint_list_search_bar_label)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
        singleLine = true
    )
}

@Composable
private fun PaintListFilter(
    paintListUiState: PaintListUiState,
    onToggleFilter: () -> Unit,
    onChangeSorting: (FilterSortByEnum) -> Unit,
    onManufacturerCheckboxChange: (SelectableManufacturer) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            modifier = Modifier.padding(start =  dimensionResource(id = R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.paint_list_filter),
                    style = MaterialTheme.typography.bodyMedium
                )
                if (paintListUiState.showPaintListFilter) {
                    IconButton(onClick = { onToggleFilter() }) {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = "",
                            modifier = Modifier
                        )
                    }
                } else {
                    IconButton(onClick = { onToggleFilter() }) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "",
                            modifier = Modifier
                        )
                    }
                }
            }
            if (paintListUiState.showPaintListFilter) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.paint_list_filter_manufacturer),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        paintListUiState.selectableManufacturers.forEach { manufacturer ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = manufacturer.isSelected,
                                    onCheckedChange = { onManufacturerCheckboxChange(manufacturer) }
                                )
                                Text(
                                    text = manufacturer.manufacturer,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Column {
                        Text(
                            text = stringResource(id = R.string.paint_list_filter_sort_by),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        SortingCheckbox(
                            isSelected = paintListUiState.sortBy.sortByNameAsc,
                            sortByEnum = FilterSortByEnum.NAME_ASC,
                            onStateChange = onChangeSorting,
                            sortByText = stringResource(id = R.string.paint_list_filter_sort_by_name_asc)
                        )
                        SortingCheckbox(
                            isSelected = paintListUiState.sortBy.sortByNameDesc,
                            sortByEnum = FilterSortByEnum.NAME_DESC,
                            onStateChange = onChangeSorting,
                            sortByText = stringResource(id = R.string.paint_list_filter_sort_by_name_desc)
                        )
                        SortingCheckbox(
                            isSelected = paintListUiState.sortBy.sortByNewest,
                            sortByEnum = FilterSortByEnum.NEWEST,
                            onStateChange = onChangeSorting,
                            sortByText = stringResource(id = R.string.paint_list_filter_sort_by_newest)
                        )
                        SortingCheckbox(
                            isSelected = paintListUiState.sortBy.sortByOldest,
                            sortByEnum = FilterSortByEnum.OLDEST,
                            onStateChange = onChangeSorting,
                            sortByText = stringResource(id = R.string.paint_list_filter_sort_by_oldest)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SortingCheckbox(
    isSelected: Boolean,
    sortByEnum: FilterSortByEnum,
    onStateChange: (FilterSortByEnum) -> Unit,
    sortByText: String,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isSelected, onCheckedChange = { onStateChange(sortByEnum)} )
        Text(
            text = sortByText,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PaintList(
    paintList: List<MiniaturePaint>,
    onPaintClick: (MiniaturePaint) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = paintList, key = { it.id }) { paint ->
            PaintItem(paint = paint,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onPaintClick(paint) })
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
            Row {
                if(paint.hexColor != "") {
                    Box(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Color(paint.hexColor.toColorInt())
                            )
                    )
                }
                Column {
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
    }
}