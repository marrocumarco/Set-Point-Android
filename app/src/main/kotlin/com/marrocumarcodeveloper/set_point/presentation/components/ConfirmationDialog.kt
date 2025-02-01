package com.marrocumarcodeveloper.set_point.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Confirmation

@Composable
internal fun ConfirmationDialog(text: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Confirmation(
        onTimeout = { onCancel() },
        icon = {
            Icon(
                imageVector = Icons.Rounded.RestartAlt,
                contentDescription = "Reset Match",
                modifier = Modifier.size(48.dp)
            )
        },
        durationMillis = 5000L
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(onClick = { onConfirm() }) {
                    Text("Yes")
                }
                Button(onClick = { onCancel() }) {
                    Text("No")
                }
            }
        }
    }
}