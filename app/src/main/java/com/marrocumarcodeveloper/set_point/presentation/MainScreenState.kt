package com.marrocumarcodeveloper.set_point.presentation

data class MainScreenState(
    val player1Serves: Boolean,
    val player1PointsDescription: String,
    val player2PointsDescription: String,
    val player1NumberOfGames: Int,
    val player2NumberOfGames: Int,
    val player1NumberOfSets: Int,
    val player2NumberOfSets: Int,
    val winnerDescription: String,
    val showCurrentSetScore: Boolean,
    val showEndedMatchAlert: Boolean,
    val showSettingsView: Boolean,
    val pointButtonsEnabled: Boolean,
    val undoButtonEnabled: Boolean
) {
    companion object {
        val initValue = MainScreenState(
            player1Serves = true,
            player1PointsDescription = "0",
            player2PointsDescription = "0",
            player1NumberOfGames = 0,
            player2NumberOfGames = 0,
            player1NumberOfSets = 0,
            player2NumberOfSets = 0,
            winnerDescription = "",
            showCurrentSetScore = true,
            showEndedMatchAlert = false,
            showSettingsView = false,
            pointButtonsEnabled = true,
            undoButtonEnabled = false
        )
    }
}