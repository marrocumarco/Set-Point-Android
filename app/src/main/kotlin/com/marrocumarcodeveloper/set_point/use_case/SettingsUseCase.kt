package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings

internal class SettingsUseCase(private val settings: Settings, private val dataAccess: DataAccess) {

    init {
        settings.setSelectedNumberOfSets(dataAccess.getSelectedNumberOfSets(settings.getDefaultNumberOfSets()))
        settings.setTiebreakEnabled(dataAccess.getTiebreakEnabled(settings.getDefaultTiebreakEnabled()))
    }

    fun getSelectableNumberOfSets(): IntArray {
        return settings.getSelectableNumberOfSets()
    }

    fun setSelectedNumberOfSets(numberOfSets: Int) {
        settings.setSelectedNumberOfSets(numberOfSets)
        dataAccess.setSelectedNumberOfSets(numberOfSets)
    }

    fun getSelectedNumberOfSets(): Int {
        return dataAccess.getSelectedNumberOfSets(settings.getDefaultNumberOfSets())
    }

    fun setTiebreakEnabled(enabled: Boolean) {
        settings.setTiebreakEnabled(enabled)
        dataAccess.setTiebreakEnabled(enabled)
    }

    fun getTiebreakEnabled(): Boolean {
        return dataAccess.getTiebreakEnabled(settings.getDefaultTiebreakEnabled())
    }
}