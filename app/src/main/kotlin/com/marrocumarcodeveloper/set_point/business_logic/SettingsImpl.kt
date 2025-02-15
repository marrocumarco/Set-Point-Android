package com.marrocumarcodeveloper.set_point.business_logic

internal class SettingsImpl : Settings {

    private val selectableNumbersOfSets = intArrayOf(1, 3, 5)
    private var selectedNumberOfSets = defaultNumberOfSets
    private var tiebreakEnabled = defaultTiebreakEnabled
    private var settingsChanged = false

    override fun setSelectedNumberOfSets(numberOfSets: Int, fromUser: Boolean) {
        if (isValid(numberOfSets)) {
            if (numberOfSetsChanged(numberOfSets)) {
                selectedNumberOfSets = numberOfSets
                if (fromUser) {
                    settingsChanged = true
                }
            }
        } else {
            throw IllegalArgumentException("Invalid number of sets")
        }
    }

    override fun resetSettingsStatus() {
        settingsChanged = false
    }

    private fun numberOfSetsChanged(numberOfSets: Int) = selectedNumberOfSets != numberOfSets

    private fun isValid(numberOfSets: Int) = selectableNumbersOfSets.contains(numberOfSets)

    override fun getSettingsChanged(): Boolean {
        return settingsChanged
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

    override fun setTiebreakEnabled(enabled: Boolean, fromUser: Boolean) {
        if (tiebreakEnabled != enabled) {
            tiebreakEnabled = enabled
            if (fromUser) {
                settingsChanged = true
            }
        }
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