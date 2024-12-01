package com.marrocumarcodeveloper.set_point.business_logic

class MatchImpl: Match {

    val player1 = Player("P1")
    val player2 = Player("P2")

    override var player1Serves = true
    override var endedSets = ArrayList<EndedSet>()
    override var showCurrentSetScore = true
    override var showEndedMatchAlert = false
    override var pointButtonsDisabled = false
    override var player1GameScoreDescription = Point.ZERO.name
    override val winnerDescription: String
        get() = winner?.name ?: ""
    override val player1SetScore: Int
        get() = player1.games
    override val player2SetScore: Int
        get() = player2.games
    override var player2GameScoreDescription = Point.ZERO.name

    private var isTiebreak = false
    private var winner: Player? = null
    private var isTiebreakEnabled = true
    private var numberOfSetsNeededToWin = 2

    private fun pointWonBy(player: Player) {
        val opponent = if (player === player1) player2 else player1
        if (isTiebreakEnabled && isTiebreak) {
            player.points += 1
            checkSetWin(player)
            if (((player.points + opponent.points) % 2) == 1) {
                player1Serves = !player1Serves
            }
        } else if (player.points + 1 == Point.ADVANTAGE.value &&
            opponent.points == Point.ADVANTAGE.value
        ) opponent.points = Point.FORTY.value else {
            player.points = (player.points + 1) % 6
            checkGameWin(player)
        }
        // deuce: score reset to 40 all

        calculatePointDescription(player)
        calculatePointDescription(opponent)
    }

    private fun checkGameWin(player: Player) {
        val opponent = if (player === player1) player2 else player1
        if (player.points >= 4 && player.points >= opponent.points + 2) {
            player.games += 1
            player.resetPoints()
            opponent.resetPoints()
            player1Serves = !player1Serves
            calculatePointDescription(player)
            calculatePointDescription(opponent)
            checkSetWin(player)
        }
    }

    private fun checkSetWin(player: Player) {
        val opponent = if (player === player1) player2 else player1
        var setWin = false
        if (isTiebreakEnabled && isTiebreak) {
            if (player.points >= 7 && player.points >= opponent.points + 2) {
                setWin = true
                player.games += 1
                player.resetPoints()
                opponent.resetPoints()
            }
        } else {
            setWin = player.games >= 6 && player.games >= opponent.games + 2
        }
        if (setWin) {
            player.sets += 1
            endedSets.add(
                EndedSet(
                    player1.games,
                    player2.games
                )
            )
            player.resetGames()
            opponent.resetGames()
            isTiebreak = false
            checkMatchWin(player)
        } else {
            if (isTiebreakEnabled && !isTiebreak && player.games == 6 && opponent.games == 6) {
                isTiebreak = true
                player.resetPoints()
                opponent.resetPoints()
            }
        }
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
        player1.resetPoints()
        player1.resetGames()
        player1.resetSets()
        player2.resetPoints()
        player2.resetGames()
        player2.resetSets()
        endedSets.clear()
        isTiebreak = false
        showCurrentSetScore = true
        showEndedMatchAlert = false
        pointButtonsDisabled = false
        player1Serves = true
        calculatePointDescription(player1)
        calculatePointDescription(player2)
    }

    override suspend fun pointWonByPlayerOne() {
        pointWonBy(player1)
    }

    override suspend fun pointWonByPlayerTwo() {
        pointWonBy(player2)
    }

    private fun calculatePointDescription(player: Player) {
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

        if (player === player1) {
            player1GameScoreDescription = pointsDescription
        } else {
            player2GameScoreDescription = pointsDescription
        }
    }
}