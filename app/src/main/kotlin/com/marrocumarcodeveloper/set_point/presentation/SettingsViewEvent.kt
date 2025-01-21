package com.marrocumarcodeveloper.set_point.presentation

sealed class SettingsViewEvent
data object OnClickTiebreakEvent : SettingsViewEvent()
data object OnClickNumberOfSetsSelectedEvent : SettingsViewEvent()
