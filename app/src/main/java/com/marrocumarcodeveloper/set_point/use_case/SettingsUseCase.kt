package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings

class SettingsUseCase(private val settings: Settings) {
    fun setNumberOfSets(numberOfSets: Int) {
        settings.setSelectedNumberOfSets(numberOfSets)
    }

    fun setSelectedNumberOfSets(numberOfSets: Int) {
        settings.setSelectedNumberOfSets(numberOfSets)
    }

    fun getSelectedNumberOfSets(): Int {
        return settings.getSelectedNumberOfSets()
    }

    fun getNumberOfSets(): IntArray {
        return settings.getNumberOfSets()
    }

    fun setTiebreakEnabled(enabled: Boolean) {
        settings.setTiebreakEnabled(enabled)
    }

    fun getTiebreakEnabled(): Boolean {
        return settings.getTiebreakEnabled()
    }

}