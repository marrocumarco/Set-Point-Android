package com.marrocumarcodeveloper.set_point.business_logic

import android.content.SharedPreferences

class SettingsImpl(val sharedPref: SharedPreferences) : Settings {

    private val numberOfSets = intArrayOf(1,3,5)
    //private var selectedNumberOfSets: Int = defaultNumberOfSets
    private var defaultTieBreakEnabled = true

    override fun setSelectedNumberOfSets(numberOfSets: Int) {
        //selectedNumberOfSets = numberOfSets
        sharedPref.edit().apply {
            putInt(NUMBER_OF_SETS, numberOfSets)
            commit()
        }
    }

    override fun getSelectedNumberOfSets(): Int {
        return sharedPref.getInt(NUMBER_OF_SETS, defaultNumberOfSets)
    }

    override fun getNumberOfSets(): IntArray {
        return numberOfSets
    }

    override fun setTiebreakEnabled(enabled: Boolean) {
        sharedPref.edit().apply {
            putBoolean(
                TIEBREAK_ENABLED, enabled)
            commit()
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