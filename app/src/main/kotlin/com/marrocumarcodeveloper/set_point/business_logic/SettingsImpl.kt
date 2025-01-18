package com.marrocumarcodeveloper.set_point.business_logic

import android.content.SharedPreferences

class SettingsImpl(private val sharedPref: SharedPreferences) : Settings {

    private val selectableNumbersOfSets = intArrayOf(1,3,5)
    //private var selectedNumberOfSets: Int = defaultNumberOfSets
    private var defaultTieBreakEnabled = true

    override fun setSelectedNumberOfSets(numberOfSets: Int) {

        if (selectableNumbersOfSets.contains(numberOfSets)) {
            sharedPref.edit().apply {
                putInt(NUMBER_OF_SETS, numberOfSets)
                apply()
            }
        } else {
            throw IllegalArgumentException("Invalid number of sets")
        }
    }

    override fun getSelectedNumberOfSets(): Int {
        return sharedPref.getInt(NUMBER_OF_SETS, defaultNumberOfSets)
    }

    override fun getNumberOfSets(): IntArray {
        return selectableNumbersOfSets
    }

    override fun setTiebreakEnabled(enabled: Boolean) {
        sharedPref.edit().apply {
            putBoolean(
                TIEBREAK_ENABLED, enabled)
            apply()
        }
    }

    override fun getTiebreakEnabled(): Boolean {
        return sharedPref.getBoolean(TIEBREAK_ENABLED, defaultTieBreakEnabled)
    }

    companion object {
        const val NUMBER_OF_SETS = "number_of_sets"
        const val TIEBREAK_ENABLED = "tiebreak_enabled"

        const val defaultNumberOfSets = 3
    }
}