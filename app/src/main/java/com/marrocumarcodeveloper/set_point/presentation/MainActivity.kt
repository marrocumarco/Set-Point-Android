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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
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
    Scaffold(timeText = {
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

@Composable
private fun secondScreenTest() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "prova secondo schermo")
    }
}

@Composable
private fun tennisMatchScreen(
    player1Name: String,
    player2Name: String,
    state: MainScreenState,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit,
    onResetScore: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            CompactButton(
                onClick = { navController.navigate("second_screen_test") },
            ) {
                Text(text = "Settings")
            }
            CompactButton(
                onClick = { onResetScore() },
            ) {
                Text(text = "Reset")
            }
        }
        SetsScoreRow(
            player1Name = player1Name, player2Name = player2Name, state = state
        )

        //Spacer(modifier = Modifier.height(16.dp))

        Row {
            PlayerScoreRow(
                state.player1GameScoreDescription, enabled = !state.pointButtonsDisabled
            ) {
                onIncrementPlayer1()
            }
            Spacer(modifier = Modifier.width(8.dp))
            PlayerScoreRow(
                state.player2GameScoreDescription, enabled = !state.pointButtonsDisabled
            ) {
                onIncrementPlayer2()
            }
        }
    }
}

@Composable
fun SetsScoreRow(player1Name: String, player2Name: String, state: MainScreenState) {
    Row(
        modifier = Modifier.padding(20.dp)
    ) {
        Column {
            Text(text = player1Name)
            Text(text = player2Name)
        }
        Spacer(modifier = Modifier.width(8.dp))

        for (set in state.endedSets) {
            Column {
                Text(text = set.player1Score.toString())
                Text(text = set.player2Score.toString())
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (state.showCurrentSetScore) {
            Column {
                Text(text = state.player1SetScore.toString())
                Text(text = state.player2SetScore.toString())
            }
        }
    }
}

@Composable
fun PlayerScoreRow(playerScore: String, enabled: Boolean, onIncrement: () -> Unit) {
    Button(
        onClick = { onIncrement() }, enabled = enabled) {
        Text(
            text = playerScore, style = MaterialTheme.typography.body2, fontSize = 20.sp
        )
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    PlayerScoreRow("1", true, {})
}