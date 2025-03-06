package com.marrocumarcodeveloper.set_point.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.times
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberColumnState
import com.marrocumarcodeveloper.set_point.R
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
import com.marrocumarcodeveloper.set_point.presentation.theme.SetPointTheme
import com.marrocumarcodeveloper.set_point.presentation.theme.wearColorPalette
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
            WearApp(viewModel, settingsViewModel)
        }
    }
}

@Composable
private fun WearApp(viewModel: MainActivityViewModel, settingsViewModel: SettingsViewModel) {
    val showConfirmationDialog by viewModel.showConfirmationDialog.collectAsState()
    val showSettingsConfirmationDialog by settingsViewModel.showConfirmationDialog.collectAsState()
    SetPointTheme {
        when {
            showConfirmationDialog -> {
                ConfirmationDialog(
                    text = stringResource(id = R.string.confirm_match_reset),
                    confirmCaption = viewModel.confirmCaption,
                    cancelCaption = viewModel.cancelCaption,
                    onConfirm = { viewModel.onEvent(OnClickConfirmResetEvent) },
                    onCancel = { viewModel.onEvent(OnClickCancelResetEvent) }
                )
            }
            showSettingsConfirmationDialog -> {
                ConfirmationDialog(
                    text = stringResource(id = R.string.confirm_settings),
                    confirmCaption = viewModel.confirmCaption,
                    cancelCaption = viewModel.cancelCaption,
                    onConfirm = {
                        settingsViewModel.onEvent(OnClickConfirmEvent)
                        viewModel.onEvent(OnConfirmSettingsAlertClosedEvent(true))
                    },
                    onCancel = {
                        settingsViewModel.onEvent(OnClickCancelEvent)
                        viewModel.onEvent(OnConfirmSettingsAlertClosedEvent(false))
                    }
                )
            }
            else -> {
                NavigationScreen(viewModel, settingsViewModel)
            }
        }
    }
}

@Composable
private fun NavigationScreen(viewModel: MainActivityViewModel, settingsViewModel: SettingsViewModel) {
    val navigationEventState by viewModel.navigationEvent.collectAsState()
    val navController = rememberSwipeDismissableNavController()

    if (navigationEventState is OnClickSettingsEvent) {
        navController.navigate("settings_screen")
    }

    AppScaffold(timeText = { TimeText() }) {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "tennis_match_screen",
            userSwipeEnabled = false
        ) {
            composable("tennis_match_screen") {
                TennisMatchScreen(viewModel)
            }
            composable("settings_screen") {
                SettingsScreen(
                    settingsViewModel = settingsViewModel,
                    onSettingsEditEnd = {
                        navController.popBackStack()
                        viewModel.onEvent(OnSettingsScreenClosedEvent)
                    }
                )
            }
        }
    }
}

@Composable
private fun TennisMatchScreen(viewModel: MainActivityViewModel) {
    val state by viewModel.mainScreenState.collectAsState()
    if (state.showEndedMatchAlert) {
        GameOverConfirmation(viewModel)
    } else {
        MatchScoreBoard(viewModel)
    }
}

@Composable
private fun GameOverConfirmation(viewModel: MainActivityViewModel) {
    val state by viewModel.mainScreenState.collectAsState()
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
            text = state.winnerDescription,
            textAlign = TextAlign.Center
        )
        Column {
            PlayerScoreRow(viewModel.player1Name, state.player1FinalScoreDescription)
            PlayerScoreRow(viewModel.player2Name, state.player2FinalScoreDescription)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            UndoButton(true, { viewModel.onEvent(OnClickUndoEvent) })
            ResetButton { viewModel.onEvent(OnClickResetEvent) }
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
private fun MatchScoreBoard(viewModel: MainActivityViewModel) {
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
                SetsScoreColumn(viewModel)
            }
            item {
                GameScoreRow(viewModel)
            }
            item {
                ResetAndSettingsRow(viewModel)
            }
        }
    }
}

@Composable
private fun GameScoreRow(viewModel: MainActivityViewModel) {
    val state by viewModel.mainScreenState.collectAsState()
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
                viewModel.onEvent(OnClickPLayerOneScoredEvent)
            }
        }
        Spacer(modifier = Modifier.size(8.dp)) // Add space between buttons
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            UndoButton(state.undoButtonEnabled, { viewModel.onEvent(OnClickUndoEvent) })
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
                viewModel.onEvent(OnClickPLayerTwoScoredEvent)
            }
        }
    }
}

@Composable
private fun ResetAndSettingsRow(viewModel: MainActivityViewModel) {
    Row {
        SettingsButton { viewModel.onEvent(OnClickSettingsEvent) }
        ResetButton { viewModel.onEvent(OnClickResetEvent) }
    }
}

@Composable
private fun SetsScoreColumn(viewModel: MainActivityViewModel) {
    val state by viewModel.mainScreenState.collectAsState()
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = viewModel.player1Name)
                Spacer(modifier = Modifier.size(3.dp))
                ServeIcon(isVisible = state.player1Serves)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ServeIcon(isVisible = !state.player1Serves)
                Spacer(modifier = Modifier.size(3.dp))
                Text(text = viewModel.player2Name)
            }
        }
        ThreeLabelsRow(
            state.player1NumberOfGames.toString(),
            viewModel.gamesCaption,
            state.player2NumberOfGames.toString()
        )
        ThreeLabelsRow(
            state.player1NumberOfSets.toString(),
            viewModel.setsCaption,
            state.player2NumberOfSets.toString()
        )
    }
}

@Composable
private fun ServeIcon(isVisible: Boolean) {
AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn(),
    exit = fadeOut()) {
    Icon(
        painter = painterResource(id = R.drawable.tennis_ball),
        contentDescription = "",
        modifier = Modifier.size(10.dp),
        tint = wearColorPalette.primary
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