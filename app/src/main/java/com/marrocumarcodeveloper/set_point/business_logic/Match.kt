package com.marrocumarcodeveloper.set_point.business_logic

import java.util.ArrayList

interface Match {
    suspend fun resetMatch()
    suspend fun pointWonByPlayerOne()
    suspend fun pointWonByPlayerTwo()

    suspend fun undo()

    val canUndo: Boolean
    val player2GameScoreDescription: String
    val player1GameScoreDescription: String
    val winnerDescription: String
    val player1SetScore: Int
    val player2SetScore: Int
    val matchEnded: Boolean
    val showEndedMatchAlert: Boolean
    val showCurrentSetScore: Boolean
    val endedSets: ArrayList<EndedSet>
    val player1Serves: Boolean
}