@file:OptIn(ExperimentalHorologistApi::class)

package com.marrocumarcodeveloper.set_point.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Switch
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.rememberColumnState
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickConfirmTileEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickNumberOfSetsSelectedEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickTiebreakEvent
import com.marrocumarcodeveloper.set_point.presentation.view_models.SettingsViewModel

@Composable
internal fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    onSettingsEditEnd: () -> Unit
) {
    val state = settingsViewModel.settingsScreenState.collectAsState()
    val configuration = LocalConfiguration.current
    val isRound = configuration.isScreenRound
    val columnState = rememberColumnState(
        factory = ScalingLazyColumnDefaults.responsive()
    )
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = if (isRound) 0.1f * configuration.screenWidthDp.dp else 0.dp,
                vertical = 0.dp
            ),
        columnState = columnState
    ) {
        item {
            Text(
                text = settingsViewModel.settingsTitle,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.title3
            )
        }
        item {
            createCustomChip(onClick = {
                settingsViewModel.onEvent(
                    OnClickTiebreakEvent
                )
            }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(settingsViewModel.tiebreakText)
                    Switch(
                        checked = state.value.tiebreakEnabled,
                        onCheckedChange = {
                            settingsViewModel.onEvent(
                                OnClickTiebreakEvent
                            )
                        })
                }
            }
        }

        item {
            createCustomChip(onClick = {
                settingsViewModel.onEvent(
                    OnClickNumberOfSetsSelectedEvent
                )
            }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(settingsViewModel.numberOfSetsText)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.value.selectedNumberOfSets.toString())
                }
            }
        }

        item {
            createCustomChip(onClick = {
                settingsViewModel.onEvent(
                    OnClickConfirmTileEvent
                )
                onSettingsEditEnd()
            }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(settingsViewModel.confirmTileText)
                }
            }
        }
    }
}

@Composable
private fun createCustomChip(onClick: () -> Unit, content: @Composable() (RowScope.() -> Unit)) {
    Chip(
        label = { content() },
        onClick = { onClick() },
        colors = ChipDefaults.primaryChipColors(),
        border = ChipDefaults.chipBorder(),
        modifier = Modifier.fillMaxWidth()
    )
}



