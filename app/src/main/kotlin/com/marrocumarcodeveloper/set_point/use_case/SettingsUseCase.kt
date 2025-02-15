package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings

internal class SettingsUseCase(private val settings: Settings, private val dataAccess: DataAccess) {

    init {
        settings.setSelectedNumberOfSets(dataAccess.getSelectedNumberOfSets(settings.getDefaultNumberOfSets()), false)
        settings.setTiebreakEnabled(dataAccess.getTiebreakEnabled(settings.getDefaultTiebreakEnabled()), false)
    }

    fun getSelectableNumberOfSets(): IntArray {
        return settings.getSelectableNumberOfSets()
    }

    fun setSelectedNumberOfSets(numberOfSets: Int) {
        settings.setSelectedNumberOfSets(numberOfSets, true)
    }

    fun getSelectedNumberOfSets(): Int {
        return settings.getSelectedNumberOfSets()
    }

    fun setTiebreakEnabled(enabled: Boolean) {
        settings.setTiebreakEnabled(enabled, true)
    }

    fun getTiebreakEnabled(): Boolean {
        return settings.getTiebreakEnabled()
    }

    fun confirmSettings() {
        dataAccess.setSelectedNumberOfSets(settings.getSelectedNumberOfSets())
        dataAccess.setTiebreakEnabled(settings.getTiebreakEnabled())
    }

    fun resetToLastSavedSettings() {
        settings.setSelectedNumberOfSets(dataAccess.getSelectedNumberOfSets(settings.getDefaultNumberOfSets()), false,)
        settings.setTiebreakEnabled(dataAccess.getTiebreakEnabled(settings.getDefaultTiebreakEnabled()), false)
        settings.resetSettingsStatus()
    }

    fun showConfirmSettingsAlert(): Boolean {
        return settings.getSettingsChanged()
    }
}