package com.marrocumarcodeveloper.set_point.persistency

import android.content.Context
import com.marrocumarcodeveloper.set_point.R
import com.marrocumarcodeveloper.set_point.use_case.LocalizationRepository

class LocalizationRepositoryImpl(private val context: Context): LocalizationRepository {
    override fun getPlayer1Name(): String {
        return context.resources.getString(R.string.player1)
    }

    override fun getPlayer2Name(): String {
        return context.resources.getString(R.string.player2)
    }

    override fun getConfirmMatchRestartMessage(): String {
        return context.resources.getString(R.string.confirm_match_reset)
    }

    override fun getConfirmSettingsMessage(): String {
        return context.resources.getString(R.string.confirm_settings)
    }

    override fun getEndedMatchMessage(): String {
        return context.resources.getString(R.string.ended_match)
    }

    override fun getGamesCaption(): String {
        return context.resources.getString(R.string.games)
    }

    override fun getSetsCaption(): String {
        return context.resources.getString(R.string.sets)
    }

    override fun getSettingsTitle(): String {
        return context.resources.getString(R.string.settings_title)
    }

    override fun getTiebreakText(): String {
        return context.resources.getString(R.string.tiebreak)
    }

    override fun getNumberOfSetsText(): String {
        return context.resources.getString(R.string.sets)
    }

    override fun getConfirmCaption(): String {
        return context.resources.getString(R.string.yes)
    }

    override fun getCancelCaption(): String {
        return context.resources.getString(R.string.no)
    }

    override fun getConfirmTileText(): String {
        return context.resources.getString(R.string.confirm_tile)
    }
}