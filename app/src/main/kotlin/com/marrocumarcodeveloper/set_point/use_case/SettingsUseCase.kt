package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings

class SettingsUseCase(private val settings: Settings) {
    fun getNumberOfSets(): IntArray {
        return settings.getNumberOfSets()
    }

    fun setSelectedNumberOfSets(numberOfSets: Int) {
        settings.setSelectedNumberOfSets(numberOfSets)
    }

    fun getSelectedNumberOfSets(): Int {
        return settings.getSelectedNumberOfSets()
    }

    fun setTiebreakEnabled(enabled: Boolean) {
        settings.setTiebreakEnabled(enabled)
    }

    fun getTiebreakEnabled(): Boolean {
        return settings.getTiebreakEnabled()
    }
}