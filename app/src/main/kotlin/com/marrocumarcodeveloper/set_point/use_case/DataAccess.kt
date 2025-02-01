package com.marrocumarcodeveloper.set_point.use_case

interface DataAccess {
    fun setSelectedNumberOfSets(numberOfSets: Int)
    fun getSelectedNumberOfSets(defaultValue: Int): Int
    fun setTiebreakEnabled(enabled: Boolean)
    fun getTiebreakEnabled(defaultValue: Boolean): Boolean
}