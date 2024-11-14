package com.marrocumarcodeveloper.set_point.presentation

import com.marrocumarcodeveloper.set_point.business_logic.EndedSet

data class MainScreenState(
    val player1Serves: Boolean,
    val endedSets: ArrayList<EndedSet>,
    val player1GameScoreDescription: String,
    val player2GameScoreDescription: String,
    val showCurrentSetScore: Boolean,
    val showEndedMatchAlert: Boolean,
    val pointButtonsDisabled: Boolean
) {
    companion object {
        val initValue = MainScreenState(
            player1Serves = true,
            endedSets = ArrayList<EndedSet>(),
            player1GameScoreDescription = "0",
            player2GameScoreDescription = "0",
            showCurrentSetScore = false,
            showEndedMatchAlert = false,
            pointButtonsDisabled = false
        )
    }
}

