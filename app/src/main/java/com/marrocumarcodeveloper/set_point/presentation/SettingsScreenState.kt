package com.marrocumarcodeveloper.set_point.presentation

data class SettingsScreenState(
    val tiebreakEnabled: Boolean,
    val selectedNumberOfSets: Int,
    val numberOfSets: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SettingsScreenState

        if (tiebreakEnabled != other.tiebreakEnabled) return false
        if (selectedNumberOfSets != other.selectedNumberOfSets) return false
        if (!numberOfSets.contentEquals(other.numberOfSets)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tiebreakEnabled.hashCode()
        result = 31 * result + selectedNumberOfSets
        result = 31 * result + numberOfSets.contentHashCode()
        return result
    }

    companion object {
        val initialValue = SettingsScreenState(
            tiebreakEnabled = true,
            selectedNumberOfSets = 3,
            numberOfSets = intArrayOf(1,3,5)
        )
    }
}
