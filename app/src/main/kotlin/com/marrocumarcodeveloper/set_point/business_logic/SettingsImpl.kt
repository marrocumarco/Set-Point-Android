package com.marrocumarcodeveloper.set_point.business_logic

internal class SettingsImpl : Settings {

    private val selectableNumbersOfSets = intArrayOf(1, 3, 5)
    private var selectedNumberOfSets = defaultNumberOfSets
    private var tiebreakEnabled = defaultTiebreakEnabled

    override fun setSelectedNumberOfSets(numberOfSets: Int) {
        if (selectableNumbersOfSets.contains(numberOfSets)) {
            selectedNumberOfSets = numberOfSets
        } else {
            throw IllegalArgumentException("Invalid number of sets")
        }
    }

    override fun getSelectedNumberOfSets(): Int {
        return selectedNumberOfSets
    }

    override fun getDefaultNumberOfSets(): Int {
        return defaultNumberOfSets
    }

    override fun getSelectableNumberOfSets(): IntArray {
        return selectableNumbersOfSets
    }

    override fun setTiebreakEnabled(enabled: Boolean) {
        tiebreakEnabled = enabled
    }

    override fun getTiebreakEnabled(): Boolean {
        return tiebreakEnabled
    }

    override fun getDefaultTiebreakEnabled(): Boolean {
        return defaultTiebreakEnabled
    }

    companion object {
        const val defaultNumberOfSets = 5
        const val defaultTiebreakEnabled = false
    }
}