package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.EndedSet
import com.marrocumarcodeveloper.set_point.business_logic.Match
import java.util.ArrayList

class MatchUseCase(private val match: Match) {
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
        get() = match.winnerDescription
    val matchEnded: Boolean
        get() = match.matchEnded
    val showEndedMatchAlert: Boolean
        get() = match.showEndedMatchAlert
    val showCurrentSetScore: Boolean
        get() = match.showCurrentSetScore
    val endedSets: ArrayList<EndedSet>
        get() = match.endedSets
    val player1Serves: Boolean
        get() = match.player1Serves
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