package com.example.paintingjournal.ui.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.paintingjournal.R
import com.example.paintingjournal.data.ComposeFileProvider
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.PaintingStep
import com.example.paintingjournal.views.miniAdd.ExpandablePaintingStep
import com.example.paintingjournal.views.miniAdd.PaintingStepIdAndUri
import com.example.paintingjournal.views.paintAdd.ImagesRow
import com.example.paintingjournal.views.paintAdd.TakeImage

@Composable
fun MiniPaintingSteps(
    paintingStepList: List<ExpandablePaintingStep>,
    isEditable: Boolean,
    onToggleExpand: (ExpandablePaintingStep) -> Unit,
    onPaintingStepValueChanged: (ExpandablePaintingStep) -> Unit,
    addPaintingStep: () -> Unit,
    removePaintingStep: (ExpandablePaintingStep) -> Unit,
    onDeleteImage: (Image) -> Unit,
    onSwitchImageEditMode: () -> Unit,
    navigateToImageViewer: (Long) -> Unit,
    onSaveImage: (PaintingStepIdAndUri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(id = R.string.mini_section_steps),
            style = MaterialTheme.typography.bodyLarge
        )
        PaintingStepsList(
            paintingStepList = paintingStepList,
            isEditable = isEditable,
            onToggleExpand = onToggleExpand,
            onValueChanged = onPaintingStepValueChanged,
            onRemoveStep = removePaintingStep,
            onDeleteImage = onDeleteImage,
            onSwitchImageEditMode = onSwitchImageEditMode,
            navigateToImageViewer = navigateToImageViewer,
            onSaveImage = onSaveImage
        )
        if(isEditable) {
            IconButton(
                onClick = { addPaintingStep() },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun PaintingStepsList(
    paintingStepList: List<ExpandablePaintingStep>,
    isEditable: Boolean,
    onToggleExpand: (ExpandablePaintingStep) -> Unit,
    onValueChanged: (ExpandablePaintingStep) -> Unit,
    onRemoveStep: (ExpandablePaintingStep) -> Unit,
    onDeleteImage: (Image) -> Unit,
    onSwitchImageEditMode: () -> Unit,
    navigateToImageViewer: (Long) -> Unit,
    onSaveImage: (PaintingStepIdAndUri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        paintingStepList.forEach { paintingStep ->
            PaintingStepEntry(
                paintingStep = paintingStep,
                isEditable = isEditable,
                onToggleExpand = onToggleExpand,
                onValueChanged = onValueChanged,
                onRemove = onRemoveStep,
                onDeleteImage = onDeleteImage,
                onSwitchImageEditMode = onSwitchImageEditMode,
                navigateToImageViewer = navigateToImageViewer,
                onSaveImage = onSaveImage
            )
        }
    }
}

@Composable
fun PaintingStepEntry(
    paintingStep: ExpandablePaintingStep,
    isEditable: Boolean,
    onToggleExpand: (ExpandablePaintingStep) -> Unit,
    onRemove: (ExpandablePaintingStep) -> Unit,
    onDeleteImage: (Image) -> Unit,
    onSwitchImageEditMode: () -> Unit,
    navigateToImageViewer: (Long) -> Unit,
    onSaveImage: (PaintingStepIdAndUri) -> Unit,
    modifier: Modifier = Modifier,
    onValueChanged: (ExpandablePaintingStep) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            if (isEditable) {
                OutlinedTextField(
                    value = paintingStep.stepTitle,
                    onValueChange = { onValueChanged(paintingStep.copy(stepTitle = it)) },
                    label = { Text(stringResource(id = R.string.painting_step_title)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(
                    text = paintingStep.stepTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if(!paintingStep.isExpanded) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { onToggleExpand(paintingStep) },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = ""
                        )
                    }
                }
            }
            if (paintingStep.isExpanded) {
                if (isEditable) {
                    OutlinedTextField(
                        value = paintingStep.stepDescription,
                        onValueChange = { onValueChanged(paintingStep.copy(stepDescription = it)) },
                        label = { Text(stringResource(id = R.string.painting_step_description)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )
                } else {
                    Text(
                        text = paintingStep.stepDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            if(paintingStep.isExpanded) {
                PaintingStepsImageSelection(
                    onSaveImage = onSaveImage,
                    expandablePaintingStep = paintingStep,
                )
                ImagesRow(
                    imageList = paintingStep.imageList,
                    onDelete = onDeleteImage,
                    showEditIcon = isEditable,
                    switchEditMode = onSwitchImageEditMode,
                    canEdit = isEditable,
                    navigateToImageViewer = navigateToImageViewer
                )
            }
            if(paintingStep.isExpanded) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(isEditable) {
                        IconButton(
                            onClick = { onRemove(paintingStep) }
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { onToggleExpand(paintingStep) },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaintingStepsImageSelection(
    onSaveImage: (PaintingStepIdAndUri) -> Unit,
    expandablePaintingStep: ExpandablePaintingStep,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Text(
            text = stringResource(id = R.string.mini_section_images),
            style = MaterialTheme.typography.bodyLarge
        )
        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))) {
            TakePaintingStepImage(
                onSaveImage = onSaveImage,
                paintingStep = expandablePaintingStep
            )
            PaintingStepImagePicker(
                onSaveImage = onSaveImage,
                paintingStep = expandablePaintingStep
            )
        }
    }
}

@Composable
fun TakePaintingStepImage(
    modifier: Modifier = Modifier,
    paintingStep: ExpandablePaintingStep,
    onSaveImage: (PaintingStepIdAndUri) -> Unit
) {
    val context = LocalContext.current

    var hasImage by remember {
        mutableStateOf(false)
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        hasImage = success
        onSaveImage(PaintingStepIdAndUri(imageUri, paintingStep.id))
    }

    Button(
        modifier = Modifier,
        onClick = {
            val uri = ComposeFileProvider.getImageUri(context)
            imageUri = uri
            cameraLauncher.launch(uri)
        },
    ) {
        Text(
            text = stringResource(id = R.string.take_photo)
        )
    }
}

@Composable
fun PaintingStepImagePicker(
    onSaveImage: (PaintingStepIdAndUri) -> Unit,
    paintingStep: ExpandablePaintingStep,
    modifier: Modifier = Modifier
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onSaveImage(PaintingStepIdAndUri(uri, paintingStep.id))
        }
    )

    Button(
        modifier = Modifier,
        onClick = {
            imagePicker.launch("image/*")
        },
    ) {
        Text(
            text = stringResource(id = R.string.open_gallery)
        )
    }
}