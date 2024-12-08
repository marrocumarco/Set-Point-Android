package com.marrocumarcodeveloper.set_point.business_logic

interface Settings {
    fun setSelectedNumberOfSets(numberOfSets: Int)
    fun getSelectedNumberOfSets(): Int
    fun getNumberOfSets(): IntArray
    fun setTiebreakEnabled(enabled: Boolean)
    fun getTiebreakEnabled(): Boolean
}