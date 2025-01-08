package com.marrocumarcodeveloper.set_point.presentation

data class MainScreenState(
    val player1Serves: Boolean,
    val player1GameScoreDescription: String,
    val player2GameScoreDescription: String,
    val winnerDescription: String,
    val player1SetScore: Int,
    val player2SetScore: Int,
    val showCurrentSetScore: Boolean,
    val showEndedMatchAlert: Boolean,
    val showSettingsView: Boolean,
    val pointButtonsEnabled: Boolean,
    val undoButtonEnabled: Boolean
) {
    companion object {
        val initValue = MainScreenState(
            player1Serves = true,
            player1GameScoreDescription = "0",
            player2GameScoreDescription = "0",
            winnerDescription = "",
            player1SetScore = 0,
            player2SetScore = 0,
            showCurrentSetScore = true,
            showEndedMatchAlert = false,
            showSettingsView = false,
            pointButtonsEnabled = true,
            undoButtonEnabled = false
        )
    }
}