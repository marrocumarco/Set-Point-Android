package com.marrocumarcodeveloper.set_point.business_logic

class MatchImpl(val settings: Settings) : Match {

    private val player1 = Player("P1")
    private val player2 = Player("P2")

    override var player1Serves = true
        private set
    override var endedSets = ArrayList<EndedSet>()
        private set
    override var showCurrentSetScore = true
        private set
    override var showEndedMatchAlert = false
        private set
    override var pointButtonsDisabled = false
        private set
    override var player1GameScoreDescription = Point.ZERO.name
        private set

    override val winnerDescription: String
        get() = winner?.name ?: ""
    override val player1SetScore: Int
        get() = player1.games
    override val player2SetScore: Int
        get() = player2.games
    override var player2GameScoreDescription = Point.ZERO.name
        private set

    private var isTiebreak = false
    private var winner: Player? = null
    private val isTiebreakEnabled: Boolean
        get() { return settings.getTiebreakEnabled() }
    private val numberOfSetsNeededToWin: Int
        get() { return (settings.getSelectedNumberOfSets() / 2) + 1 }

    override suspend fun pointWonByPlayerOne() {
        pointWonBy(player1, player2)
    }

    override suspend fun pointWonByPlayerTwo() {
        pointWonBy(player2, player1)
    }

    private fun pointWonBy(player: Player, opponent: Player) {
        if (getIsTiebreakMode()) {
            addPointInTiebreakMode(player, opponent)
        } else if (needToResetScoreToDeuce(player, opponent))
            resetScoreToForty(opponent)
        else {
            addPointTo(player)
            checkGameWin(player, opponent)
        }
        updateScoreDescriptions()
    }

    private fun getIsTiebreakMode(): Boolean =
        isTiebreakEnabled && isTiebreak

    private fun addPointInTiebreakMode(
        player: Player,
        opponent: Player
    ) {
        player.points += 1
        checkSetWin(player, opponent)
        if (needToSwitchPlayerOnServiceDuringTiebreak(player, opponent)) {
            switchPlayerOnService()
        }
    }

    private fun needToSwitchPlayerOnServiceDuringTiebreak(
        player: Player,
        opponent: Player
    ) = ((player.points + opponent.points) % 2) == 1

    private fun needToResetScoreToDeuce(
        player: Player,
        opponent: Player
    ) = player.points + 1 == Point.ADVANTAGE.value &&
            opponent.points == Point.ADVANTAGE.value

    private fun resetScoreToForty(opponent: Player) {
        opponent.points = Point.FORTY.value
    }

    private fun addPointTo(player: Player) {
        player.points = (player.points + 1) % 6
    }

    private fun checkGameWin(player: Player, opponent: Player) {
        if (gameEnded(player, opponent)) {
            increaseGamesCounter(player)
            resetGameScore(player, opponent)
            switchPlayerOnService()
            updateScoreDescriptions()
            checkSetWin(player, opponent)
        }
    }

    private fun switchPlayerOnService() {
        player1Serves = !player1Serves
    }

    private fun increaseGamesCounter(player: Player) {
        player.games += 1
    }

    private fun gameEnded(
        player: Player,
        opponent: Player
    ) = player.points >= 4 && player.points >= opponent.points + 2

    private fun checkSetWin(player: Player, opponent: Player) {
        var setEnded = false
        if (getIsTiebreakMode()) {
            if (tiebreakEnded(player, opponent)) {
                setEnded = true
                increaseGamesCounter(player)
                resetGameScore(player, opponent)
            }
        } else {
            setEnded = setEnded(player, opponent)
        }
        handleEndedSet(setEnded, player, opponent)
    }

    private fun handleEndedSet(
        setEnded: Boolean,
        player: Player,
        opponent: Player
    ) {
        if (setEnded) {
            increaseSetsCounter(player)
            archiveEndedSet()
            resetPlayersGames(player, opponent)
            isTiebreak = false
            checkMatchWin(player)
        } else {
            if (shouldEnableTiebreak(player, opponent)) {
                isTiebreak = true
                resetGameScore(player, opponent)
            }
        }
    }

    private fun archiveEndedSet() {
        endedSets.add(
            EndedSet(
                player1.games,
                player2.games
            )
        )
    }

    private fun shouldEnableTiebreak(
        player: Player,
        opponent: Player
    ) = isTiebreakEnabled && !isTiebreak && player.games == 6 && opponent.games == 6

    private fun resetPlayersGames(
        player: Player,
        opponent: Player
    ) {
        player.resetGames()
        opponent.resetGames()
    }

    private fun increaseSetsCounter(player: Player) {
        player.sets += 1
    }

    private fun setEnded(
        player: Player,
        opponent: Player
    ) = player.games >= 6 && player.games >= opponent.games + 2

    private fun tiebreakEnded(
        player: Player,
        opponent: Player
    ) = player.points >= 7 && player.points >= opponent.points + 2

    private fun resetGameScore(
        player: Player,
        opponent: Player
    ) {
        player.resetPoints()
        opponent.resetPoints()
    }

    private fun checkMatchWin(player: Player) {
        if (player.sets == numberOfSetsNeededToWin) {
            winner = player
            showCurrentSetScore = false
            showEndedMatchAlert = true
            pointButtonsDisabled = true
        }
    }

    override suspend fun resetMatch() {
        resetGameScore(player1, player2)
        resetSetScore()
        resetMatchScore()
        resetFlags()
        updateScoreDescriptions()
    }

    private fun resetFlags() {
        isTiebreak = false
        showCurrentSetScore = true
        showEndedMatchAlert = false
        pointButtonsDisabled = false
        player1Serves = true
    }

    private fun resetMatchScore() {
        player1.resetSets()
        player2.resetSets()
        endedSets.clear()
    }

    private fun resetSetScore() {
        player1.resetGames()
        player2.resetGames()
    }

    private fun updateScoreDescriptions() {
        player1GameScoreDescription = calculatePointDescription(player1)
        player2GameScoreDescription = calculatePointDescription(player2)
    }

    private fun calculatePointDescription(player: Player): String {
        var pointsDescription = ""
        if (isTiebreak) {
            pointsDescription = player.points.toString()
        } else {
            when (player.points) {
                Point.ZERO.value -> pointsDescription = "0"
                Point.FIFTEEN.value -> pointsDescription = "15"
                Point.THIRTY.value -> pointsDescription = "30"
                Point.FORTY.value -> pointsDescription = "40"
                Point.ADVANTAGE.value -> pointsDescription = "A"
                Point.GAMEWON.value -> pointsDescription = "W"
            }
        }
        return pointsDescription
    }
}