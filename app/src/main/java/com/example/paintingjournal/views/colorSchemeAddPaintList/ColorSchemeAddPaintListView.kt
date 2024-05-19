package com.example.paintingjournal.views.colorSchemeAddPaintList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider
import com.example.paintingjournal.views.paintList.PaintItem

object ColorSchemeAddPaintListDestination: NavigationDestination {
    override val route: String = "color_scheme_add_paint"
    override val titleRes = R.string.color_scheme_add_paint_list_title
    const val colorSchemeIdArg = "colorSchemeId"
    val routeWithArgs = "$route/{$colorSchemeIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSchemeAddPaintListView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    viewModel: ColorSchemeAddPaintListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            PaintingJournalTopAppBar(
                title = stringResource(R.string.mini_details_title),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ColorSchemeAddPaintListBody(
            paints = viewModel.colorSchemeAddPaintListUiState.paints,
            onSelectPaint = { viewModel.onSelectPaint(it) },
            onNavigateBack = navigateBack,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ColorSchemeAddPaintListBody(
    paints: List<MiniaturePaint>,
    onSelectPaint: (MiniaturePaint) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = paints, key = { it.id }) { paint ->
            if(paint.hexColor != "") { // Only display paints with color added
                PaintItem(
                    paint = paint,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .clickable {
                            onSelectPaint(paint)
                            onNavigateBack()
                        }
                )
            }
        }
    }
}