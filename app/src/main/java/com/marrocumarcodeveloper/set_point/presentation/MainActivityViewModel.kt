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

    private fun updateState() {
        updateState(
            currentState().copy(
                player1Serves = matchUseCase.player1Serves,
                endedSets = matchUseCase.endedSets,
                player1GameScoreDescription = matchUseCase.player1GameScoreDescription,
                player2GameScoreDescription = matchUseCase.player2GameScoreDescription,
                winnerDescription = matchUseCase.winnerDescription,
                player1SetScore = matchUseCase.player1SetScore,
                player2SetScore = matchUseCase.player2SetScore,
                showCurrentSetScore = matchUseCase.showCurrentSetScore,
                showEndedMatchAlert = matchUseCase.showEndedMatchAlert,
                pointButtonsDisabled = matchUseCase.pointsButtonsDisabled
            )
        )
    }

    // Process events sent from the view
    fun onEvent(event: MainViewEvent) {
        viewModelScope.launch {
            when (event) {
                is OnClickPLayerOneScoredEvent -> onClickPlayerOneScoredEvent()
                is OnClickPLayerTwoScoredEvent -> onClickPlayerTwoScoredEvent()
                is OnClickResetScoreEvent -> onClickResetScoreEvent()
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

    private suspend fun onClickResetScoreEvent() {
        matchUseCase.resetMatch()
        updateState()
    }
}