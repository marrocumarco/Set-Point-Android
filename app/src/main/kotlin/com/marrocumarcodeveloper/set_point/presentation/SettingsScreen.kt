package com.marrocumarcodeveloper.set_point.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.DialogProperties
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.rememberPickerState

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val state = settingsViewModel.settingsScreenState.collectAsState()
    var openNumberOfSetsDialog by remember { mutableStateOf(false) }

    if (openNumberOfSetsDialog) {
        NumberOfSetsDialog(
            values = state.value.numberOfSets,
            currentValue = state.value.selectedNumberOfSets,
            onDismiss = { openNumberOfSetsDialog = false },
            onConfirm = { selectedNumber ->
                openNumberOfSetsDialog = false
                settingsViewModel.onEvent(
                    OnNumberOfSetsSelectedEvent(selectedNumber)
                )
                openNumberOfSetsDialog = false
            }
        )
    } else {
        TileList(
            state = state.value,
            onclickNumberOfSetsChip = { openNumberOfSetsDialog = true },
            onclickTiebreakChip = {
                settingsViewModel.onEvent(
                    OnClickTiebreakEvent
                )
            })
    }
}

@Composable
fun NumberOfSetsDialog(
    values: IntArray,
    currentValue: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val state = rememberPickerState(
        initialNumberOfOptions = values.size,
        initiallySelectedOption = values.indexOf(currentValue),
        repeatItems = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select number of Sets") },
        text = {
            Picker(
                state = state, contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            ) { itemIndex -> Text(text = values[itemIndex].toString()) }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(values[state.selectedOption])
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun TileList(
    state: SettingsScreenState,
    onclickTiebreakChip: () -> Unit,
    onclickNumberOfSetsChip: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isRound = configuration.isScreenRound

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = if (isRound) 0.1f * configuration.screenWidthDp.dp else 0.dp,
                vertical = if (isRound) 0.1f * configuration.screenHeightDp.dp else 0.dp
            )
    ) {
        item {
            createCustomChip(onClick = onclickTiebreakChip) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tiebreak")
                    Switch(
                        checked = state.tiebreakEnabled,
                        onCheckedChange = { onclickTiebreakChip() })
                }
            }
        }

        item {
            createCustomChip(onClick = onclickNumberOfSetsChip) {
                Row {
                    Text("Number of Sets:")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.selectedNumberOfSets.toString())
                }
            }
        }
    }
}

@Composable
private fun createCustomChip(onClick: () -> Unit, content: @Composable() (RowScope.() -> Unit)) {
    Chip(
        onClick = { onClick() },
        colors = ChipDefaults.primaryChipColors(),
        border = ChipDefaults.chipBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}


