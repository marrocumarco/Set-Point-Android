package com.marrocumarcodeveloper.set_point.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marrocumarcodeveloper.set_point.use_case.MatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private var matchUseCase: MatchUseCase) : ViewModel() {
    // State is maintained using StateFlow
    private val _mainScreenState = MutableStateFlow(MainScreenState.initValue)
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState.asStateFlow()

    // Function to get the current state
    private fun currentState(): MainScreenState = _mainScreenState.value

    // Function to update the state
    private fun updateState(newState: MainScreenState) {
        _mainScreenState.value = newState
    }

    private var showSettingsView = false

    private fun updateState() {
        updateState(
            currentState().copy(
                player1Serves = matchUseCase.player1Serves,
                player1PointsDescription = matchUseCase.player1PointsDescription,
                player2PointsDescription = matchUseCase.player2PointsDescription,
                winnerDescription = matchUseCase.winnerDescription,
                player1NumberOfGames = matchUseCase.player1NumberOfGames,
                player2NumberOfGames = matchUseCase.player2NumberOfGames,
                player1NumberOfSets = matchUseCase.player1NumberOfSets,
                player2NumberOfSets = matchUseCase.player2NumberOfSets,
                showCurrentSetScore = matchUseCase.showCurrentSetScore,
                showEndedMatchAlert = matchUseCase.showEndedMatchAlert,
                showSettingsView = showSettingsView,
                pointButtonsEnabled = !matchUseCase.matchEnded,
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
            }
        }
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

    private suspend fun onClickSettingsEvent() {
        updateState()
    }
}