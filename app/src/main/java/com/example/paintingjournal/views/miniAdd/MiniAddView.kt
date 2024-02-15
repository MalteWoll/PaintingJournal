package com.example.paintingjournal.views.miniAdd

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

object MiniAddDestination: NavigationDestination {
    override val route = "mini_add"
    override val titleRes = R.string.mini_add_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniAddView(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: MiniAddViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                PaintingJournalTopAppBar(
                    title = stringResource(id = R.string.mini_add_title),
                    canNavigateBack = false
                )
            }
        ) { innerPadding ->
            MiniatureEntryBody(
                miniatureUiState = viewModel.miniatureUiState,
                onMiniatureValueChanged = viewModel::updateUiState,
                onSaveClicked = {
                                coroutineScope.launch {
                                    viewModel.saveMiniature()
                                    navigateBack()
                                }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MiniatureEntryBody(
    miniatureUiState: MiniatureUiState,
    onMiniatureValueChanged: (MiniatureDetails) -> Unit,
    onSaveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        MiniatureInputForm(
            miniatureDetails = miniatureUiState.miniatureDetails,
            onValueChanged = onMiniatureValueChanged,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClicked,
            enabled = miniatureUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.save_action))
            }
    }
}

@Composable
fun MiniatureInputForm(
    miniatureDetails: MiniatureDetails,
    modifier: Modifier = Modifier,
    onValueChanged: (MiniatureDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = miniatureDetails.name,
            onValueChange = { onValueChanged(miniatureDetails.copy(name = it)) },
            label = { Text(stringResource(id = R.string.mini_add_form_name)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = miniatureDetails.manufacturer,
            onValueChange = { onValueChanged(miniatureDetails.copy(manufacturer = it)) },
            label = { Text(stringResource(id = R.string.mini_add_form_manufacturer)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = miniatureDetails.faction,
            onValueChange = { onValueChanged(miniatureDetails.copy(faction = it)) },
            label = { Text(stringResource(id = R.string.mini_add_form_faction)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}