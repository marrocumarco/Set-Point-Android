package com.marrocumarcodeveloper.set_point.business_logic

class Player(val name: String) {

    var points = 0
    var games = 0
    var sets = 0

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