package com.marrocumarcodeveloper.set_point.business_logic

import java.util.ArrayList

interface Match {
    suspend fun resetMatch()
    suspend fun pointWonByPlayerOne()
    suspend fun pointWonByPlayerTwo()

    suspend fun undo()

    val canUndo: Boolean
    val player1PointsDescription: String
    val player2PointsDescription: String
    val player1NumberOfGames: Int
    val player2NumberOfGames: Int
    val player1NumberOfSets: Int
    val player2NumberOfSets: Int
    val winnerDescription: String
    val matchEnded: Boolean
    val endedSets: ArrayList<EndedSet>
    val player1Serves: Boolean
}