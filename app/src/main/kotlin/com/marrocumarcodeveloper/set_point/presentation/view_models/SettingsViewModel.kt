package com.marrocumarcodeveloper.set_point.presentation.view_models

import androidx.lifecycle.ViewModel
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickCancelEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickConfirmEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickConfirmTileEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickNumberOfSetsSelectedEvent
import com.marrocumarcodeveloper.set_point.presentation.events.OnClickTiebreakEvent
import com.marrocumarcodeveloper.set_point.presentation.states.SettingsScreenState
import com.marrocumarcodeveloper.set_point.presentation.events.SettingsViewEvent
import com.marrocumarcodeveloper.set_point.use_case.SettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SettingsViewModel(private var settingsUseCase: SettingsUseCase) :
    ViewModel() {

    val confirmTileText: String
        get() = settingsUseCase.confirmTileText
    val numberOfSetsText: String
        get() = settingsUseCase.numberOfSetsText
    val tiebreakText: String
        get() = settingsUseCase.tiebreakText
    val settingsTitle: String
        get() = settingsUseCase.settingsTitle
    private val _settingsScreenState = MutableStateFlow(
        SettingsScreenState(
            settingsUseCase.getTiebreakEnabled(),
            settingsUseCase.getSelectedNumberOfSets(),
            settingsUseCase.getSelectableNumberOfSets()
        )
    )

    internal val settingsScreenState: StateFlow<SettingsScreenState> = _settingsScreenState.asStateFlow()
    private val _showConfirmationDialog = MutableStateFlow(false)
    val showConfirmationDialog: StateFlow<Boolean> = _showConfirmationDialog.asStateFlow()

    private fun currentState(): SettingsScreenState = _settingsScreenState.value

    // Function to update the state
    private fun updateState(newState: SettingsScreenState) {
        _settingsScreenState.value = newState
    }

    private fun updateState() {
        updateState(
            currentState().copy(
                selectedNumberOfSets = getCurrentNumberOfSets(),
                tiebreakEnabled = settingsUseCase.getTiebreakEnabled(),
                numberOfSets = getSelectableNumberOfSets()
            )
        )
    }

    internal fun onEvent(event: SettingsViewEvent) {
        when (event) {
            is OnClickTiebreakEvent -> onTiebreakEnabledStateChanged()
            is OnClickNumberOfSetsSelectedEvent -> onClickNumberOfSetsSelected()
            is OnClickConfirmTileEvent -> onClickConfirmTileEvent()
            is OnClickCancelEvent -> onClickCancelEvent()
            is OnClickConfirmEvent -> onClickConfirmEvent()
        }
    }

    private fun onClickConfirmTileEvent() {
        if (settingsUseCase.showConfirmSettingsAlert()) {
            _showConfirmationDialog.value = true
        }
    }

    private fun onClickConfirmEvent() {
        settingsUseCase.confirmSettings()
        _showConfirmationDialog.value = false
        updateState()
    }

    private fun onClickCancelEvent() {
        settingsUseCase.resetToLastSavedSettings()
        _showConfirmationDialog.value = false
        updateState()
    }

    fun onTiebreakEnabledStateChanged() {
        settingsUseCase.setTiebreakEnabled(currentState().tiebreakEnabled.not())
        updateState()
    }

    private fun onClickNumberOfSetsSelected() {
        val updatedNumberOfSets = getUpdatedNumberOfSets()
        updateNumberOfSets(updatedNumberOfSets)
        updateState()
    }

    private fun getUpdatedNumberOfSets(): Int {
        val selectableNumberOfSets = getSelectableNumberOfSets()
        val nextIndex = calculateNextSelectableNumberOfSetsIndex(selectableNumberOfSets)
        return selectableNumberOfSets[nextIndex]
    }

    private fun getSelectableNumberOfSets() = settingsUseCase.getSelectableNumberOfSets()

    private fun calculateNextSelectableNumberOfSetsIndex(selectableNumberOfSets: IntArray): Int {
        val currentIndex = getCurrentIndexOf(selectableNumberOfSets)
        val nextIndex = (currentIndex + 1) % selectableNumberOfSets.size
        return nextIndex
    }

    private fun updateNumberOfSets(updatedNumberOfSets: Int) {
        settingsUseCase.setSelectedNumberOfSets(updatedNumberOfSets)
    }

    private fun getCurrentIndexOf(selectableNumberOfSets: IntArray): Int {
        val selectedNumberOfSets = getCurrentNumberOfSets()
        val index = selectableNumberOfSets.indexOf(selectedNumberOfSets)
        return index
    }

    private fun getCurrentNumberOfSets() = settingsUseCase.getSelectedNumberOfSets()
}