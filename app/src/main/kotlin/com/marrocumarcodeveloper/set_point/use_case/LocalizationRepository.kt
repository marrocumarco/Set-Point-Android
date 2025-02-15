package com.marrocumarcodeveloper.set_point.use_case

interface LocalizationRepository {
    fun getPlayer1Name(): String
    fun getPlayer2Name(): String
    fun getConfirmMatchRestartMessage(): String
    fun getConfirmSettingsMessage(): String
    fun getEndedMatchMessage(): String
    fun getGamesCaption(): String
    fun getSetsCaption(): String
    fun getSettingsTitle(): String
    fun getTiebreakText(): String
    fun getNumberOfSetsText(): String
    fun getConfirmTileText(): String
    fun getConfirmCaption(): String
    fun getCancelCaption(): String
}