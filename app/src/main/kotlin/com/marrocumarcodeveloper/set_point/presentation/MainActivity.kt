/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
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
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.layout.rememberColumnState
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll
import com.marrocumarcodeveloper.set_point.R
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
fun WearApp(viewModel: MainActivityViewModel, settingsViewModel: SettingsViewModel) {

    NavigationScreen(viewModel = viewModel,
        settingsViewModel = settingsViewModel,
        onIncrementPlayer1 = { viewModel.onEvent(OnClickPLayerOneScoredEvent) },
        onIncrementPlayer2 = { viewModel.onEvent(OnClickPLayerTwoScoredEvent) },
        onUndo = { viewModel.onEvent(OnClickUndoEvent) },
        onShowSettings = { viewModel.onEvent(OnClickSettingsEvent) })
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
    onShowSettings: () -> Unit
) {
    val state by viewModel.mainScreenState.collectAsState()

    AppScaffold(timeText = {
        TimeText() // Show hour on top
    }) {
        val navController = rememberSwipeDismissableNavController()
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
                    onShowSettings,
                    navController
                )
            }
            composable("second_screen_test") {
                SecondScreenTest(settingsViewModel)
            }
        }
    }
// ...
// .. Screen level content goes here
    //val scrollState = rememberScrollState()

    // ScreenScaffold(scrollState = scrollState) {
    // Screen content goes here
    //}
}

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)
@Composable
private fun SecondScreenTest(settingsViewModel: SettingsViewModel) {
    val state = settingsViewModel.settingsScreenState.collectAsState()
    val scrollState = rememberScrollState()
    ScreenScaffold(scrollState = scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .rotaryWithScroll(scrollState, rememberActiveFocusRequester())
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val numberOfSets = state.value.selectedNumberOfSets
            Text(text = "Number of sets: $numberOfSets")
            Button(onClick = { settingsViewModel.onTiebreakEnabledStateChanged(!state.value.tiebreakEnabled) }) {
                Text(text = if (state.value.tiebreakEnabled) { "tiebreak enabled" } else { "tiebreak disabled"})
            }
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
private fun TennisMatchScreen(
    player1Name: String,
    player2Name: String,
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onUndo: () -> Unit,
    onShowSettings: () -> Unit,
    navController: NavHostController,
) {
    val columnState = rememberColumnState(
        factory = ScalingLazyColumnDefaults.responsive(
            userScrollEnabled = false
        ),
    )
    if (state.showEndedMatchAlert) {
        GameOverConfirmation(state.winnerDescription, onUndo)
    } else if (state.showSettingsView) {
        navController.navigate("second_screen_test")
    } else {
        MatchScoreBoard(
            columnState,
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
    columnState: ScalingLazyColumnState,
    player1Name: String,
    player2Name: String,
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onResetScore: () -> Unit,
    onShowSettings: () -> Unit
) {
    ScreenScaffold {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxRectangle(),
            columnState = columnState
        ) {
            item {
                SetsScoreRow(
                    player1Name = player1Name, player2Name = player2Name, state = state
                )
            }
            item {
                GameScoreRow(state, onIncrementPlayer1, onIncrementPlayer2)
            }
            item {
                ResetAndSettingsRow(state, onResetScore, onShowSettings)
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
    onIncrementPlayer2: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        PlayerScoreButton(
            state.player1PointsDescription, enabled = state.pointButtonsEnabled
        ) {
            onIncrementPlayer1()
        }
        Spacer(modifier = Modifier.width(8.dp))
        PlayerScoreButton(
            state.player2PointsDescription, enabled = state.pointButtonsEnabled
        ) {
            onIncrementPlayer2()
        }
    }
}

@Composable
private fun ResetAndSettingsRow(
    state: MainScreenState,
    onResetScore: () -> Unit,
    onShowSettings: () -> Unit
) {
    Row {
        CompactButton(
            onClick = { onShowSettings },
        ) {
            Icon(
                Icons.Rounded.Settings,
                contentDescription = ""
            )
        }
        UndoButton(state.undoButtonEnabled, onResetScore)
    }
}

@Composable
private fun UndoButton(enabled: Boolean, onUndo: () -> Unit) {
    CompactButton(
        onClick = { onUndo() },
        enabled = enabled
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.Undo,
            contentDescription = ""
        )
    }
}

@Composable
fun SetsScoreRow(player1Name: String, player2Name: String, state: MainScreenState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        ScoreColumn(player1Name, player2Name)
        Spacer(modifier = Modifier.width(8.dp))

        /*for (set in state.endedSets) {
            ScoreColumn(set.player1Score.toString(), set.player2Score.toString())
            Spacer(modifier = Modifier.width(8.dp))
        }*/

        if (state.showCurrentSetScore) {
            ScoreColumn(state.player1NumberOfSets.toString(), state.player2NumberOfSets.toString())
        }
    }
}

@Composable
private fun ScoreColumn(player1Name: String, player2Name: String) {
    Column {
        Text(text = player1Name)
        Text(text = player2Name)
    }
}

@Composable
fun PlayerScoreButton(playerScore: String, enabled: Boolean, onIncrement: () -> Unit) {
    Chip(modifier = Modifier
        .wrapContentSize(Alignment.Center),
        label = {
            Text(
                text = playerScore, style = MaterialTheme.typography.bodySmall, fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
        }, onClick = { onIncrement() },
        enabled = enabled,
        shape = MaterialTheme.shapes.large)
}


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    PlayerScoreButton("1", true) {}
}