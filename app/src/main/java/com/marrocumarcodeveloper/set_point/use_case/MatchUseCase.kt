package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.EndedSet
import com.marrocumarcodeveloper.set_point.business_logic.Match
import java.util.ArrayList

class MatchUseCase(private val match: Match) {
    val player1GameScoreDescription: String
        get() = match.player1GameScoreDescription
    val player2GameScoreDescription: String
        get() = match.player2GameScoreDescription
    val winnerDescription: String
        get() = match.winnerDescription
    val player1SetScore: Int
        get() = match.player1SetScore
    val player2SetScore: Int
        get() = match.player2SetScore
    val pointsButtonsDisabled: Boolean
        get() = match.pointButtonsDisabled
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
}