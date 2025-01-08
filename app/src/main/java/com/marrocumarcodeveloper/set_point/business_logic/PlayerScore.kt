package com.marrocumarcodeveloper.set_point.business_logic

class PlayerScore(val name: String, var points: Int = 0, var games: Int = 0, var sets: Int = 0) {

    fun resetPoints() {
        points = 0
    }

    fun resetGames() {
        games = 0
    }

    fun resetSets() {
        sets = 0
    }
}