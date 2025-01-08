package com.marrocumarcodeveloper.set_point.business_logic

data class MatchState(
    val player1Points: Int,
    val player1Games: Int,
    val player1Sets: Int,
    val player2Points: Int,
    val player2Games: Int,
    val player2Sets: Int,
    val endedSets: ArrayList<EndedSet>,
    val showCurrentSetScore: Boolean,
    val showEndedMatchAlert: Boolean,
    val matchEnded: Boolean,
    val player1GameScoreDescription: String,
    val player2GameScoreDescription: String,
    val isTiebreak: Boolean,
    val winner: PlayerScore?,
    val player1Serves: Boolean
)