package com.marrocumarcodeveloper.set_point.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberColumnState
import com.marrocumarcodeveloper.set_point.presentation.components.SettingsScreen
import com.marrocumarcodeveloper.set_point.presentation.components.ConfirmationDialog
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickCancelEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickCancelResetEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickConfirmEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickConfirmResetEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickPLayerOneScoredEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickPLayerTwoScoredEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickResetEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickSettingsEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickUndoEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnConfirmSettingsAlertClosedEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnSettingsScreenClosedEvent
import com.marrocumarcodeveloper.set_point.presentation.states.MainScreenState
import com.marrocumarcodeveloper.set_point.presentation.theme.SetPointTheme
import com.marrocumarcodeveloper.set_point.presentation.view_models.MainActivityViewModel
import com.marrocumarcodeveloper.set_point.presentation.view_models.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(viewModel, settingsViewModel = settingsViewModel)
        }
    }
}

@Composable
private fun WearApp(viewModel: MainActivityViewModel, settingsViewModel: SettingsViewModel) {
    val showConfirmationDialog by viewModel.showConfirmationDialog.collectAsState()
    val showSettingsConfirmationDialog by settingsViewModel.showConfirmationDialog.collectAsState()
    SetPointTheme {
        if (showConfirmationDialog) {
            ConfirmationDialog(
                text = "Confirm match reset?",
                onConfirm = { viewModel.onEvent(OnClickConfirmResetEvent) },
                onCancel = { viewModel.onEvent(OnClickCancelResetEvent) })
        } else if (showSettingsConfirmationDialog) {
            ConfirmationDialog(
                text = "Confirm settings?\nThe match will be reset.",
                onConfirm = {
                    settingsViewModel.onEvent(OnClickConfirmEvent)
                    viewModel.onEvent(OnConfirmSettingsAlertClosedEvent(true))
                },
                onCancel = {
                    settingsViewModel.onEvent(OnClickCancelEvent)
                    viewModel.onEvent(OnConfirmSettingsAlertClosedEvent(false))
                })
        } else {
            NavigationScreen(viewModel = viewModel,
                settingsViewModel = settingsViewModel,
                onIncrementPlayer1 = { viewModel.onEvent(OnClickPLayerOneScoredEvent) },
                onIncrementPlayer2 = { viewModel.onEvent(OnClickPLayerTwoScoredEvent) },
                onUndo = { viewModel.onEvent(OnClickUndoEvent) },
                onShowSettings = { viewModel.onEvent(OnClickSettingsEvent) },
                onResetScore = { viewModel.onEvent(OnClickResetEvent) },
                onSettingScreenClosed = { viewModel.onEvent(OnSettingsScreenClosedEvent) })
        }
    }
}

@Composable
private fun NavigationScreen(
    viewModel: MainActivityViewModel,
    settingsViewModel: SettingsViewModel,
    player1Name: String = "P1",
    player2Name: String = "P2",
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onUndo: () -> Unit,
    onShowSettings: () -> Unit,
    onSettingScreenClosed: () -> Unit,
    onResetScore: () -> Unit) {
    val state by viewModel.mainScreenState.collectAsState()
    val navigationEventState by viewModel.navigationEvent.collectAsState()
    val navController = rememberSwipeDismissableNavController()

    if (navigationEventState != null && navigationEventState is OnClickSettingsEvent) {
        navController.navigate("settings_screen")
    }

    AppScaffold(timeText = {
        TimeText() // Show hour on top
    }) {

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "tennis_match_screen", userSwipeEnabled = false
        ) {
            composable("tennis_match_screen") {
                TennisMatchScreen(
                    player1Name,
                    player2Name,
                    state,
                    onIncrementPlayer1,
                    onIncrementPlayer2,
                    onUndo,
                    onResetScore,
                    onShowSettings
                )
            }
            composable("settings_screen") {
                SettingsScreen(settingsViewModel = settingsViewModel, onSettingsEditEnd = {
                    navController.popBackStack()
                    onSettingScreenClosed()
                })
            }
        }
    }
}

@Composable
private fun TennisMatchScreen(
    player1Name: String,
    player2Name: String,
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onUndo: () -> Unit,
    onResetScore: () -> Unit,
    onShowSettings: () -> Unit,
) {
    if (state.showEndedMatchAlert) {
        GameOverConfirmation(
            state.winnerDescription,
            player1Name,
            player2Name,
            state.player1FinalScoreDescription,
            state.player2FinalScoreDescription,
            onUndo,
            onResetScore
        )
    } else {
        MatchScoreBoard(
            player1Name,
            player2Name,
            state,
            onIncrementPlayer1,
            onIncrementPlayer2,
            onUndo,
            onResetScore,
            onShowSettings
        )
    }
}

@Composable
@OptIn(ExperimentalHorologistApi::class)
private fun MatchScoreBoard(
    player1Name: String,
    player2Name: String,
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onUndo: () -> Unit,
    onResetScore: () -> Unit,
    onShowSettings: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isRound = configuration.isScreenRound
    val columnState = rememberColumnState(
        factory = ScalingLazyColumnDefaults.responsive(
            userScrollEnabled = false
        )
    )
    ScreenScaffold {
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
                SetsScoreColumn(
                    player1Name = player1Name, player2Name = player2Name, state = state
                )
            }
            item {
                GameScoreRow(state, onIncrementPlayer1, onIncrementPlayer2, onUndo)
            }
            item {
                ResetAndSettingsRow(onShowSettings, onResetScore)
            }
        }
    }
}

@Composable
private fun GameOverConfirmation(
    winnerDescription: String,
    player1Name: String,
    player2Name: String,
    player1Score: String,
    player2Score: String,
    onUndoTap: () -> Unit,
    onResetTap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = winnerDescription,
            textAlign = TextAlign.Center
        )
        Column {
            PlayerScoreRow(player1Name, player1Score)
            PlayerScoreRow(player2Name, player2Score)
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom) {
            UndoButton(true, onUndoTap)
            ResetButton(onResetTap)
        }
    }
}

@Composable
private fun PlayerScoreRow(winnerDescription: String, winnerScore: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = winnerDescription,
            textAlign = TextAlign.Center
        )
        Text(
            text = winnerScore,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GameScoreRow(
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onUndo: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            PlayerScoreButton(
                state.player1PointsDescription, enabled = state.pointButtonsEnabled
            ) {
                onIncrementPlayer1()
            }
        }
        Spacer(modifier = Modifier.size(8.dp)) // Add space between buttons
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            UndoButton(state.undoButtonEnabled, onUndo)
        }
        Spacer(modifier = Modifier.size(8.dp)) // Add space between button
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            PlayerScoreButton(
                state.player2PointsDescription, enabled = state.pointButtonsEnabled
            ) {
                onIncrementPlayer2()
            }
        }
    }
}

@Composable
private fun ResetAndSettingsRow(
    onShowSettings: () -> Unit,
    onResetScore: () -> Unit
) {
    Row {
        SettingsButton(onShowSettings)
        ResetButton(onResetScore)
    }
}

@Composable
private fun SettingsButton(onShowSettings: () -> Unit) {
    CompactButton(
        onClick = { onShowSettings() },
    ) {
        Icon(
            Icons.Rounded.Settings,
            contentDescription = ""
        )
    }
}

@Composable
private fun ResetButton(onResetScore: () -> Unit) {
    CompactButton(
        onClick = { onResetScore() }
    ) {
        Icon(
            Icons.Rounded.RestartAlt,
            contentDescription = ""
        )
    }
}

@Composable
private fun UndoButton(enabled: Boolean, onUndo: () -> Unit) {
    CompactButton(
        onClick = { onUndo() },
        enabled = enabled,
        colors = androidx.wear.compose.material.ButtonDefaults.secondaryButtonColors()
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.Undo,
            contentDescription = ""
        )
    }
}

@Composable
private fun SetsScoreColumn(player1Name: String, player2Name: String, state: MainScreenState) {
    Column {
        ThreeLabelsRow(player1Name, "", player2Name)
        ThreeLabelsRow(
            state.player1NumberOfGames.toString(),
            "games",
            state.player2NumberOfGames.toString()
        )
        ThreeLabelsRow(
            state.player1NumberOfSets.toString(),
            "sets",
            state.player2NumberOfSets.toString()
        )
    }
}

@Composable
private fun ThreeLabelsRow(firstText: String, secondText: String, thirdText: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = firstText)
        Text(text = secondText)
        Text(text = thirdText)
    }
}

@Composable
fun PlayerScoreButton(playerScore: String, enabled: Boolean, onIncrement: () -> Unit) {
    Button(
        onClick = { onIncrement() },
        enabled = enabled,
        colors = androidx.wear.compose.material.ButtonDefaults.primaryButtonColors()
    ) {
        Text(
            text = playerScore, style = MaterialTheme.typography.bodySmall, fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    GameOverConfirmation("P1", "P1", "P2", "1 2 3 4 5", "1 2 3 4 5", {}, {})
}