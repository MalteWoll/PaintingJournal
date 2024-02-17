package com.example.paintingjournal.views.paintDetail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paintingjournal.R
import com.example.paintingjournal.navigation.NavigationDestination
import com.example.paintingjournal.ui.AppViewModelProvider

object PaintDetailsDestination : NavigationDestination {
    override val route = "paint_details"
    override val titleRes = R.string.paint_details_title
    const val paintIdArg = "paintId"
    val routeWithArgs = "$route/{$paintIdArg}"
}

@Composable
fun PaintDetailView(
    navigateToEditPaint: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    //viewModel: PaintDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

}