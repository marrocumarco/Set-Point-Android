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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScalingLazyColumnState
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberColumnState
import com.google.android.horologist.compose.rotaryinput.rotaryWithScroll
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(viewModel)
        }
    }
}

@Composable
fun WearApp(viewModel: MainActivityViewModel) {

    NavigationScreen(viewModel = viewModel,
        onIncrementPlayer1 = { viewModel.onEvent(OnClickPLayerOneScoredEvent) },
        onIncrementPlayer2 = { viewModel.onEvent(OnClickPLayerTwoScoredEvent) },
        onResetScore = { viewModel.onEvent(OnClickResetScoreEvent) })
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun NavigationScreen(
    viewModel: MainActivityViewModel,
    player1Name: String = "P1",
    player2Name: String = "P2",
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onResetScore: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    AppScaffold(timeText = {
        TimeText() // Mostra l'ora in alto
    }) {
        val navController = rememberSwipeDismissableNavController()
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "tennis_match_screen"
        ) {
            composable("tennis_match_screen") {

                tennisMatchScreen(
                    player1Name,
                    player2Name,
                    state,
                    onIncrementPlayer1,
                    onIncrementPlayer2,
                    onResetScore,
                    navController
                )
            }
            composable("second_screen_test") {
                secondScreenTest()
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
private fun secondScreenTest() {
    val scrollState = rememberScrollState()
    ScreenScaffold(scrollState = scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .rotaryWithScroll(scrollState, rememberActiveFocusRequester())
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (1..100).forEach {
                Text("i = $it")
            }
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
private fun tennisMatchScreen(
    player1Name: String,
    player2Name: String,
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onResetScore: () -> Unit,
    navController: NavHostController,
) {
    val columnState = rememberColumnState(
        factory = ScalingLazyColumnDefaults.responsive(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            rotaryMode = ScalingLazyColumnState.RotaryMode.Snap
        ),
    )
    ScreenScaffold {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxWidth(),
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
                ResetAndSettingsRow(navController, onResetScore)
            }
        }
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
            state.player1GameScoreDescription, enabled = !state.pointButtonsDisabled
        ) {
            onIncrementPlayer1()
        }
        Spacer(modifier = Modifier.width(8.dp))
        PlayerScoreButton(
            state.player2GameScoreDescription, enabled = !state.pointButtonsDisabled
        ) {
            onIncrementPlayer2()
        }
    }
}

@Composable
private fun ResetAndSettingsRow(
    navController: NavHostController,
    onResetScore: () -> Unit
) {
    Row {
//        CompactButton(
//            onClick = { navController.navigate("second_screen_test") },
//        ) {
//            Icon(
//                Icons.Rounded.Settings,
//                contentDescription = ""
//            )
//        }
        CompactButton(
            onClick = { onResetScore() },
        ) {
            Icon(
                Icons.Rounded.RestartAlt,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun SetsScoreRow(player1Name: String, player2Name: String, state: MainScreenState) {
    Row(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        ScoreColumn(player1Name, player2Name)
        Spacer(modifier = Modifier.width(8.dp))

        for (set in state.endedSets) {
            ScoreColumn(set.player1Score.toString(), set.player2Score.toString())
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (state.showCurrentSetScore) {
            ScoreColumn(state.player1SetScore.toString(), state.player2SetScore.toString())
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
        shape = MaterialTheme.shapes.large
    )
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    PlayerScoreButton("1", true, {})
}