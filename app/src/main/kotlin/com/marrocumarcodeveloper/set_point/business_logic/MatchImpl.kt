package com.marrocumarcodeveloper.set_point.business_logic

import java.util.EmptyStackException
import java.util.Stack

class MatchImpl(val settings: Settings) : Match {

    private var playerScore1 = PlayerScore("P1")
    private var playerScore2 = PlayerScore("P2")
    private val statesStack: Stack<MatchState> = Stack()

    override var player1Serves = true
        private set
    override var endedSets = ArrayList<EndedSet>()
        private set
    override var matchEnded = false
        private set
    override var player1PointsDescription = Point.ZERO.value.toString()
        private set
    override var player2PointsDescription = Point.ZERO.value.toString()
        private set
    override val player1NumberOfGames: Int
        get() = playerScore1.games
    override val player2NumberOfGames: Int
        get() = playerScore2.games
    override val player1NumberOfSets: Int
        get() = playerScore1.sets
    override val player2NumberOfSets: Int
        get() = playerScore2.sets
    private var isTiebreak = false
    private var winner: PlayerScore? = null
    override val winnerDescription: String
        get() = winner?.name ?: ""
    private val isTiebreakEnabled: Boolean
        get() {
            return settings.getTiebreakEnabled()
        }
    private val numberOfSetsNeededToWin: Int
        get() {
            return (settings.getSelectedNumberOfSets() / 2) + 1
        }
    override val canUndo: Boolean
        get() = !statesStack.empty()

    override suspend fun pointWonByPlayerOne() {
        pointWonBy(playerScore1, opponent = playerScore2)
    }

    override suspend fun pointWonByPlayerTwo() {
        pointWonBy(playerScore2, opponent = playerScore1)
    }

    override suspend fun undo() {
        try {
            getPreviousState()?.let {
                apply(it)
            }
        } catch (e: EmptyStackException) {
            throw IllegalStateException("No previous state")
        }
    }

    private fun apply(state: MatchState) {
        playerScore1 = PlayerScore("P1", state.player1Points, state.player1Games, state.player1Sets)
        playerScore2 = PlayerScore("P2", state.player2Points, state.player2Games, state.player2Sets)
        player1Serves = state.player1Serves
        endedSets.clear()
        endedSets.addAll(state.endedSets)
        matchEnded = state.matchEnded
        player1PointsDescription = state.player1GameScoreDescription
        player2PointsDescription = state.player2GameScoreDescription
        isTiebreak = state.isTiebreak
        winner = state.winner
    }

    private fun getPreviousState(): MatchState? =
        statesStack.pop()

    private fun pointWonBy(playerScore: PlayerScore, opponent: PlayerScore) {
        saveOldState()
        if (isTiebreakMode) {
            addPointInTiebreakMode(playerScore, opponent)
        } else if (needToResetScoreToDeuce(playerScore, opponent))
            resetScoreToForty(opponent)
        else {
            addPointTo(playerScore)
            checkGameWin(playerScore, opponent)
        }
        updateScoreDescriptions()
    }

    private val isTiebreakMode: Boolean
        get() = isTiebreakEnabled && isTiebreak

    private fun addPointInTiebreakMode(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) {
        playerScore.points += 1
        checkSetWin(playerScore, opponent)
        if (needToSwitchPlayerOnServiceDuringTiebreak(playerScore, opponent)) {
            switchPlayerOnService()
        }
    }

    private fun needToSwitchPlayerOnServiceDuringTiebreak(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) = ((playerScore.points + opponent.points) % 2) == 1

    private fun needToResetScoreToDeuce(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) = playerScore.points + 1 == Point.ADVANTAGE.value &&
            opponent.points == Point.ADVANTAGE.value

    private fun resetScoreToForty(opponent: PlayerScore) {
        opponent.points = Point.FORTY.value
    }

    private fun addPointTo(playerScore: PlayerScore) {
        playerScore.points = (playerScore.points + 1) % 6
    }

    private fun checkGameWin(playerScore: PlayerScore, opponent: PlayerScore) {
        if (gameEnded(playerScore, opponent)) {
            increaseGamesCounter(playerScore)
            resetGameScore(playerScore, opponent)
            switchPlayerOnService()
            updateScoreDescriptions()
            checkSetWin(playerScore, opponent)
        }
    }

    private fun switchPlayerOnService() {
        player1Serves = !player1Serves
    }

    private fun increaseGamesCounter(playerScore: PlayerScore) {
        playerScore.games += 1
    }

    private fun gameEnded(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) = playerScore.points >= 4 && playerScore.points >= opponent.points + 2

    private fun checkSetWin(playerScore: PlayerScore, opponent: PlayerScore) {
        var setEnded = false
        if (isTiebreakMode) {
            if (tiebreakEnded(playerScore, opponent)) {
                setEnded = true
                increaseGamesCounter(playerScore)
                resetGameScore(playerScore, opponent)
            }
        } else {
            setEnded = setEnded(playerScore, opponent)
        }
        handleEndedSet(setEnded, playerScore, opponent)
    }

    private fun handleEndedSet(
        setEnded: Boolean,
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) {
        if (setEnded) {
            increaseSetsCounter(playerScore)
            archiveEndedSet()
            resetPlayersGames(playerScore, opponent)
            isTiebreak = false
            checkMatchWin(playerScore)
        } else {
            if (shouldEnableTiebreak(playerScore, opponent)) {
                isTiebreak = true
                resetGameScore(playerScore, opponent)
            }
        }
    }

    private fun archiveEndedSet() {
        endedSets.add(
            EndedSet(
                playerScore1.games,
                playerScore2.games
            )
        )
    }

    private fun shouldEnableTiebreak(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) = isTiebreakEnabled && !isTiebreak && playerScore.games == 6 && opponent.games == 6

    private fun resetPlayersGames(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) {
        playerScore.resetGames()
        opponent.resetGames()
    }

    private fun increaseSetsCounter(playerScore: PlayerScore) {
        playerScore.sets += 1
    }

    private fun setEnded(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) = playerScore.games >= 6 && playerScore.games >= opponent.games + 2

    private fun tiebreakEnded(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) = playerScore.points >= 7 && playerScore.points >= opponent.points + 2

    private fun resetGameScore(
        playerScore: PlayerScore,
        opponent: PlayerScore
    ) {
        playerScore.resetPoints()
        opponent.resetPoints()
    }

    private fun checkMatchWin(playerScore: PlayerScore) {
        if (playerScore.sets == numberOfSetsNeededToWin) {
            winner = playerScore
            matchEnded = true
        }
    }

    private fun saveOldState() {
        val state = createNewState()
        store(state)
    }

    private fun createNewState() = MatchState(
        playerScore1.points,
        playerScore1.games,
        playerScore1.sets,
        playerScore2.points,
        playerScore2.games,
        playerScore2.sets,
        endedSets,
        matchEnded,
        player1PointsDescription,
        player2PointsDescription,
        isTiebreak,
        winner,
        player1Serves
    )

    private fun store(state: MatchState) {
        statesStack.push(state)
    }

    override suspend fun resetMatch() {
        resetGameScore(playerScore1, playerScore2)
        resetSetScore()
        resetMatchScore()
        resetFlags()
        updateScoreDescriptions()
        resetStatesStack()
    }

    private fun resetFlags() {
        isTiebreak = false
        matchEnded = false
        player1Serves = true
    }

    private fun resetMatchScore() {
        playerScore1.resetSets()
        playerScore2.resetSets()
        endedSets.clear()
    }

    private fun resetSetScore() {
        playerScore1.resetGames()
        playerScore2.resetGames()
    }

    private fun updateScoreDescriptions() {
        player1PointsDescription = calculatePointDescription(playerScore1)
        player2PointsDescription = calculatePointDescription(playerScore2)
    }

    private fun resetStatesStack() {
        statesStack.removeAllElements()
    }

    private fun calculatePointDescription(playerScore: PlayerScore): String {
        var pointsDescription = ""
        if (isTiebreak) {
            pointsDescription = playerScore.points.toString()
        } else {
            when (playerScore.points) {
                Point.ZERO.value -> pointsDescription = "0"
                Point.FIFTEEN.value -> pointsDescription = "15"
                Point.THIRTY.value -> pointsDescription = "30"
                Point.FORTY.value -> pointsDescription = "40"
                Point.ADVANTAGE.value -> pointsDescription = "A"
            }
        }
        return pointsDescription
    }
}