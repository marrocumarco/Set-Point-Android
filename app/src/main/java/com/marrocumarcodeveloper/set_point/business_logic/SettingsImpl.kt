package com.marrocumarcodeveloper.set_point.business_logic

class SettingsImpl : Settings {
    private val numberOfSets = intArrayOf(1,3,5)
    private var selectedNumberOfSets: Int = defaultNumberOfSets
    private var tieBreakEnabled = true

    override fun setSelectedNumberOfSets(numberOfSets: Int) {
        selectedNumberOfSets = numberOfSets
    }

    override fun getSelectedNumberOfSets(): Int {
        return selectedNumberOfSets
    }

    override fun getNumberOfSets(): IntArray {
        return numberOfSets
    }

    override fun setTiebreakEnabled(enabled: Boolean) {
        tieBreakEnabled = enabled
    }

    override fun getTiebreakEnabled(): Boolean {
        return tieBreakEnabled
    }

    companion object {
        const val defaultNumberOfSets = 3
    }
}