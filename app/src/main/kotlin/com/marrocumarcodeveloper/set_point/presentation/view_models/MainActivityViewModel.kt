package com.marrocumarcodeveloper.set_point.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marrocumarcodeveloper.set_point.presentation.states.MainScreenState
import com.marrocumarcodeveloper.set_point.presentation.events.MainViewEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickCancelResetEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickConfirmResetEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickPLayerOneScoredEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickPLayerTwoScoredEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickResetEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickSettingsEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickUndoEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnSettingsShownEvent
import com.marrocumarcodeveloper.set_point.use_case.MatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainActivityViewModel @Inject constructor(private var matchUseCase: MatchUseCase) : ViewModel() {
    // State is maintained using StateFlow
    private val _mainScreenState = MutableStateFlow(MainScreenState.initValue)
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState.asStateFlow()

    private val _navigationEvent = MutableStateFlow<MainViewEvent?>(null)
    val navigationEvent: StateFlow<MainViewEvent?> = _navigationEvent.asStateFlow()

    private val _showConfirmationDialog = MutableStateFlow(false)
    val showConfirmationDialog: StateFlow<Boolean> = _showConfirmationDialog.asStateFlow()

    // Function to get the current state
    private fun currentState(): MainScreenState = _mainScreenState.value

    // Function to update the state
    private fun updateState(newState: MainScreenState) {
        _mainScreenState.value = newState
    }

    private fun updateState() {
        updateState(
            currentState().copy(
                player1Serves = matchUseCase.player1Serves,
                player1PointsDescription = matchUseCase.player1PointsDescription,
                player2PointsDescription = matchUseCase.player2PointsDescription,
                winnerDescription = matchUseCase.winnerDescription,
                player1FinalScoreDescription = matchUseCase.player1FinalScoreDescription,
                player2FinalScoreDescription = matchUseCase.player2FinalScoreDescription,
                player1NumberOfGames = matchUseCase.player1NumberOfGames,
                player2NumberOfGames = matchUseCase.player2NumberOfGames,
                player1NumberOfSets = matchUseCase.player1NumberOfSets,
                player2NumberOfSets = matchUseCase.player2NumberOfSets,
                showCurrentSetScore = matchUseCase.matchEnded.not(),
                showEndedMatchAlert = matchUseCase.matchEnded,
                pointButtonsEnabled = matchUseCase.matchEnded.not(),
                undoButtonEnabled = matchUseCase.canUndo
            )
        )
    }

    // Process events sent from the view
    fun onEvent(event: MainViewEvent) {
        viewModelScope.launch {
            when (event) {
                is OnClickPLayerOneScoredEvent -> onClickPlayerOneScoredEvent()
                is OnClickPLayerTwoScoredEvent -> onClickPlayerTwoScoredEvent()
                is OnClickUndoEvent -> onClickUndoEvent()
                is OnClickSettingsEvent -> onClickSettingsEvent()
                is OnSettingsShownEvent -> onSettingsShown()
                is OnClickResetEvent -> onClickResetEvent()
                is OnClickConfirmResetEvent -> OnClickConfirmResetEvent()
                is OnClickCancelResetEvent -> onClickCancelResetEvent()
            }
        }
    }

    private fun onClickResetEvent() {
        _showConfirmationDialog.value = true
    }
    private fun onClickCancelResetEvent() {
        _showConfirmationDialog.value = false
    }

    private suspend fun OnClickConfirmResetEvent() {
        matchUseCase.resetMatch()
        _showConfirmationDialog.value = false
        updateState()
    }

    // Handle the OnClickCountUpEvent
    private suspend fun onClickPlayerOneScoredEvent() {
        matchUseCase.pointWonByPlayerOne()
        updateState()
    }

    private suspend fun onClickPlayerTwoScoredEvent() {
        matchUseCase.pointWonByPlayerTwo()
        updateState()
    }

    private suspend fun onClickUndoEvent() {
        matchUseCase.undo()
        updateState()
    }

    private fun onClickSettingsEvent() {
        _navigationEvent.value = OnClickSettingsEvent
    }

    private fun onSettingsShown() {
        _navigationEvent.value = null
    }
}