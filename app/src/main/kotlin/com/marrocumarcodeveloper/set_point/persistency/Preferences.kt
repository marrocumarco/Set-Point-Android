package com.marrocumarcodeveloper.set_point.persistency

import android.content.SharedPreferences
import com.marrocumarcodeveloper.set_point.use_case.DataAccess

internal class Preferences(val sharedPreferences: SharedPreferences): DataAccess {
    override fun setSelectedNumberOfSets(numberOfSets: Int) {
        sharedPreferences.edit().putInt(NUMBER_OF_SETS, numberOfSets).apply()
    }

    override fun getSelectedNumberOfSets(defaultValue: Int): Int {
        return sharedPreferences.getInt(NUMBER_OF_SETS, defaultValue)
    }

    override fun setTiebreakEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(TIEBREAK_ENABLED, enabled).apply()
    }

    override fun getTiebreakEnabled(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(TIEBREAK_ENABLED, defaultValue)
    }

    companion object{
        const val NUMBER_OF_SETS = "number_of_sets"
        const val TIEBREAK_ENABLED = "tiebreak_enabled"
    }
}