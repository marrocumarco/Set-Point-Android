package com.marrocumarcodeveloper.set_point.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.dialog.Confirmation
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
import com.marrocumarcodeveloper.set_point.R
import com.marrocumarcodeveloper.set_point.presentation.theme.SetPointTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetPointTheme {
                WearApp(viewModel, settingsViewModel = settingsViewModel)
            }
        }
    }
}

@Composable
fun WearApp(viewModel: MainActivityViewModel, settingsViewModel: SettingsViewModel) {

    NavigationScreen(viewModel = viewModel,
        settingsViewModel = settingsViewModel,
        onIncrementPlayer1 = { viewModel.onEvent(OnClickPLayerOneScoredEvent) },
        onIncrementPlayer2 = { viewModel.onEvent(OnClickPLayerTwoScoredEvent) },
        onUndo = { viewModel.onEvent(OnClickUndoEvent) },
        onShowSettings = { viewModel.onEvent(OnClickSettingsEvent) },
        onSettingsShown = { viewModel.onEvent(OnSettingsShownEvent) })
}

@Composable
fun NavigationScreen(
    viewModel: MainActivityViewModel,
    settingsViewModel: SettingsViewModel,
    player1Name: String = "P1",
    player2Name: String = "P2",
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onUndo: () -> Unit,
    onShowSettings: () -> Unit,
    onSettingsShown: () -> Unit
) {
    val state by viewModel.mainScreenState.collectAsState()
    val navigationEventState by viewModel.navigationEvent.collectAsState()
    val navController = rememberSwipeDismissableNavController()

    if (navigationEventState != null && navigationEventState is OnClickSettingsEvent) {
        navController.navigate("second_screen_test")
        onSettingsShown()
    }

    AppScaffold(timeText = {
        TimeText() // Show hour on top
    }) {

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "tennis_match_screen"
        ) {
            composable("tennis_match_screen") {
                TennisMatchScreen(
                    player1Name,
                    player2Name,
                    state,
                    onIncrementPlayer1,
                    onIncrementPlayer2,
                    onUndo,
                    onShowSettings
                )
            }
            composable("second_screen_test") {
                SettingsScreen(settingsViewModel = settingsViewModel)
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
    onShowSettings: () -> Unit,
) {
    if (state.showEndedMatchAlert) {
        GameOverConfirmation(state.winnerDescription, onUndo)
    } else {
        MatchScoreBoard(
            player1Name,
            player2Name,
            state,
            onIncrementPlayer1,
            onIncrementPlayer2,
            onUndo,
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
                GameScoreRow(state, onIncrementPlayer1, onIncrementPlayer2, onResetScore)
            }
            item {
                ResetAndSettingsRow(state, onShowSettings)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationGraphicsApi::class)
private fun GameOverConfirmation(winnerDescription: String, onUndo: () -> Unit) {
    val animation = AnimatedImageVector.animatedVectorResource(R.drawable.checkmark_animation)
    Confirmation(
        onTimeout = {
            /* Do something e.g. navController.popBackStack() */
        },
        icon = {
            // Initially, animation is static and shown at the start position (atEnd = false).
            // Then, we use the EffectAPI to trigger a state change to atEnd = true,
            // which plays the animation from start to end.
            var atEnd by remember { mutableStateOf(false) }
            DisposableEffect(Unit) {
                atEnd = true
                onDispose {}
            }
            Image(
                painter = rememberAnimatedVectorPainter(animation, atEnd),
                contentDescription = "Open on phone",
                modifier = Modifier.size(48.dp)
            )
        },
        durationMillis = animation.totalDuration * 2L,
    ) {
        Text(
            text = "$winnerDescription wins!",
            textAlign = TextAlign.Center
        )
        UndoButton(true, onUndo)
    }
}

@Composable
private fun GameScoreRow(
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onResetScore: () -> Unit
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
            UndoButton(state.undoButtonEnabled, onResetScore)
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
    state: MainScreenState,
    onShowSettings: () -> Unit
) {
    //TODO: Add a reset button
    Row {
        CompactButton(
            onClick = { onShowSettings() },
        ) {
            Icon(
                Icons.Rounded.Settings,
                contentDescription = ""
            )
        }
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
fun SetsScoreColumn(player1Name: String, player2Name: String, state: MainScreenState) {
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
    PlayerScoreButton("1", true) {}
}