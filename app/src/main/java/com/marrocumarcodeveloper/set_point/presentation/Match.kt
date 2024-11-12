package com.marrocumarcodeveloper.set_point.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.marrocumarcodeveloper.set_point.business_logic.EndedSet
import com.marrocumarcodeveloper.set_point.business_logic.Player
import com.marrocumarcodeveloper.set_point.business_logic.Point

class Match(player1Name: String, player2Name: String, application: Application) :
    AndroidViewModel(application) {

    val player1 = Player(player1Name)
    val player2 = Player(player2Name)

    var player1Serves = true
    var endedSets = ArrayList<EndedSet>()
    var player1GameScoreDescription = Point.ZERO.name
    var player2GameScoreDescription = Point.ZERO.name
    var isTiebreak = false
    var showCurrentSetScore = true
    var showEndedMatchAlert = false
    var pointButtonsDisabled = false

    var winner: Player? = null
    var isTiebreakEnabled = true
    var numberOfSetsNeededToWin = 3

    fun pointWonBy(player: Player) {
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

    fun checkGameWin(player: Player) {
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

    fun checkSetWin(player: Player) {
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

    fun checkMatchWin(player: Player) {
        if (player.sets == numberOfSetsNeededToWin) {
            winner = player
            showCurrentSetScore = false
            showEndedMatchAlert = true
            pointButtonsDisabled = true
        }
    }

    fun resetMatch() {
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

    fun calculatePointDescription(player: Player) {
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