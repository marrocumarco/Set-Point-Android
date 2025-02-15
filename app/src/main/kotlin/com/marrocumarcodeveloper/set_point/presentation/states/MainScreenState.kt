package com.marrocumarcodeveloper.set_point.presentation.states

internal data class MainScreenState(
    val player1Serves: Boolean,
    val player1PointsDescription: String,
    val player2PointsDescription: String,
    val player1NumberOfGames: Int,
    val player2NumberOfGames: Int,
    val player1NumberOfSets: Int,
    val player2NumberOfSets: Int,
    val winnerDescription: String,
    val player1FinalScoreDescription: String,
    val player2FinalScoreDescription: String,
    val showCurrentSetScore: Boolean,
    val showEndedMatchAlert: Boolean,
    val showConfirmSettingsAlert: Boolean,
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
            player1FinalScoreDescription = "",
            player2FinalScoreDescription = "",
            showCurrentSetScore = true,
            showEndedMatchAlert = false,
            showConfirmSettingsAlert = false,
            pointButtonsEnabled = true,
            undoButtonEnabled = false
        )
    }
}