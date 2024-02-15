package com.example.paintingjournal.views.miniList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.PaintingJournalTopAppBar
import com.example.paintingjournal.R
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
    canNavigateBack: Boolean = true,
    viewModel: MiniListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.mini_list_title),
                    canNavigateBack = false
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
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}