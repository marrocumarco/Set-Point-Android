package com.marrocumarcodeveloper.set_point.business_logic

interface Settings {
    fun setSelectedNumberOfSets(numberOfSets: Int, fromUser: Boolean)
    fun getSelectedNumberOfSets(): Int
    fun getDefaultNumberOfSets(): Int
    fun getSelectableNumberOfSets(): IntArray
    fun setTiebreakEnabled(enabled: Boolean, fromUser: Boolean)
    fun getTiebreakEnabled(): Boolean
    fun getDefaultTiebreakEnabled(): Boolean
    fun getSettingsChanged(): Boolean
    fun resetSettingsStatus()
}