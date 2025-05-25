package com.marrocumarcodeveloper.set_point

import android.content.Context
import com.marrocumarcodeveloper.set_point.business_logic.MatchImpl
import com.marrocumarcodeveloper.set_point.business_logic.Settings
import com.marrocumarcodeveloper.set_point.business_logic.SettingsImpl
import com.marrocumarcodeveloper.set_point.persistency.LocalizationRepositoryImpl
import com.marrocumarcodeveloper.set_point.persistency.Preferences
import com.marrocumarcodeveloper.set_point.use_case.MatchUseCase
import com.marrocumarcodeveloper.set_point.use_case.SettingsUseCase


class AppContainer(val applicationContext: Context) {
    private val settings: Settings = SettingsImpl()
    private val matchUseCase = MatchUseCase(MatchImpl(settings), LocalizationRepositoryImpl(applicationContext))
    private val settingsUseCase = SettingsUseCase(settings, Preferences(applicationContext.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)), LocalizationRepositoryImpl(applicationContext))
    internal val mainActivityViewModelFactory = MainActivityViewModelFactory(matchUseCase)
    internal val settingsViewModelFactory = SettingsViewModelFactory(settingsUseCase)
}
