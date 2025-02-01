package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.EndedSet
import com.marrocumarcodeveloper.set_point.business_logic.Match
import java.util.ArrayList

internal class MatchUseCase(private val match: Match) {
    val canUndo: Boolean
        get() = match.canUndo
    val player1PointsDescription: String
        get() = match.player1PointsDescription
    val player2PointsDescription: String
        get() = match.player2PointsDescription
    val player1NumberOfSets: Int
        get() = match.player1NumberOfSets
    val player2NumberOfSets: Int
        get() = match.player2NumberOfSets
    val player1NumberOfGames: Int
        get() = match.player1NumberOfGames
    val player2NumberOfGames: Int
        get() = match.player2NumberOfGames
    val winnerDescription: String
        get() = getFormattedWinnerDescription(match.winnerDescription)
    val player1FinalScoreDescription: String
        get() = match.endedSets.map { "${it.player1Score}" }.joinToString(" ")
    val player2FinalScoreDescription: String
        get() = match.endedSets.map { "${it.player2Score}" }.joinToString(" ")
    val matchEnded: Boolean
        get() = match.matchEnded
    val endedSets: ArrayList<EndedSet>
        get() = match.endedSets
    val player1Serves: Boolean
        get() = match.player1Serves

    private fun getFormattedWinnerDescription(winnerName: String) = "Game, set, match " + winnerName

    suspend fun pointWonByPlayerOne() {
        match.pointWonByPlayerOne()
    }
    suspend fun pointWonByPlayerTwo() {
        match.pointWonByPlayerTwo()
    }
    suspend fun resetMatch() {
        match.resetMatch()
    }
    suspend fun undo() {
        match.undo()
    }
}