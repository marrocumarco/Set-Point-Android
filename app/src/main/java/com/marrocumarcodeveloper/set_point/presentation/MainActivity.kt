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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
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

    val state by viewModel.state.collectAsState()
    TennisScoreScreen(state = state,
        onReset = {},
        onIncrementPlayer1 = { viewModel.onEvent(OnClickPLayerOneScoredEvent) },
        onIncrementPlayer2 = { viewModel.onEvent(OnClickPLayerTwoScoredEvent) })
}

@Composable
fun TennisScoreScreen(
    state: MainScreenState,
    player1Name: String = "P1",
    player2Name: String = "P2",
    onReset: () -> Unit,
    onIncrementPlayer1: () -> Unit,
    onIncrementPlayer2: () -> Unit
) {
    Scaffold(timeText = {
        TimeText() // Mostra l'ora in alto
    }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            SetsScoreRow(
                player1Name = player1Name, player2Name = player2Name, state = state
            )
//            Button(
//                onClick = { onReset() },
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text(text = "Reset")
//            }

            //Spacer(modifier = Modifier.height(16.dp))

            PlayerScoreRow(
                state.player1GameScoreDescription, enabled = !state.pointButtonsDisabled
            ) {
                onIncrementPlayer1()
            }

            PlayerScoreRow(
                state.player2GameScoreDescription, enabled = !state.pointButtonsDisabled
            ) {
                onIncrementPlayer2()
            }
//            Row {
//
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//
//            }
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
    Chip(onClick = { onIncrement() }, colors = ChipDefaults.primaryChipColors(), enabled = enabled,
        modifier = Modifier.fillMaxWidth(fraction = 0.5f)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = playerScore, style = MaterialTheme.typography.body2, fontSize = 20.sp
            )
        }
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    PlayerScoreRow("1", true, {})
}