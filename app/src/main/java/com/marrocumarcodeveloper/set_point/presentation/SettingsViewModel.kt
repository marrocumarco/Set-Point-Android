package com.marrocumarcodeveloper.set_point.presentation

import androidx.lifecycle.ViewModel
import com.marrocumarcodeveloper.set_point.use_case.SettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private var settingsUseCase: SettingsUseCase) : ViewModel() {

    private val _settingsScreenState = MutableStateFlow(SettingsScreenState.initialValue)
    val settingsScreenState: StateFlow<SettingsScreenState> = _settingsScreenState.asStateFlow()

    private fun currentState(): SettingsScreenState = _settingsScreenState.value

    // Function to update the state
    private fun updateState(newState: SettingsScreenState) {
        _settingsScreenState.value = newState
    }

    private fun updateState() {
        updateState(
            currentState().copy(
                selectedNumberOfSets = settingsUseCase.getSelectedNumberOfSets(),
                tiebreakEnabled = settingsUseCase.getTiebreakEnabled(),
                numberOfSets = settingsUseCase.getNumberOfSets()
            )
        )
    }

    // Handle the OnClickCountUpEvent
    private fun onTiebreakEnabledStateChanged(enabled: Boolean) {
        settingsUseCase.setTiebreakEnabled(enabled)
        updateState()
    }

    private fun onNumberOfSetsSelected(numberOfSets: Int) {
        settingsUseCase.setSelectedNumberOfSets(numberOfSets)
        updateState()
    }
}