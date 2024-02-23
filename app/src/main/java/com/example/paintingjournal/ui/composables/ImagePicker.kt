package com.example.paintingjournal.ui.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.paintingjournal.R
import com.example.paintingjournal.data.ComposeFileProvider
import com.example.paintingjournal.views.paintAdd.TakeImage

@Composable
fun ImageSelection(
    onSaveImage: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))) {
        TakeImage(
            onSaveImage = onSaveImage
        )
        ImagePicker(
            onSaveImage = onSaveImage
        )
    }
}

@Composable
fun ImagePicker(
    onSaveImage: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onSaveImage(uri)
        }
    )

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = {
            imagePicker.launch("image/*")
        },
    ) {
        Text(
            text = stringResource(id = R.string.open_gallery)
        )
    }
}