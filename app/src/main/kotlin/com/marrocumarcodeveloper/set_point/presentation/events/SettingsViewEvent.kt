package com.marrocumarcodeveloper.set_point.presentation.events

sealed class SettingsViewEvent
data object OnClickTiebreakEvent : SettingsViewEvent()
data object OnClickNumberOfSetsSelectedEvent : SettingsViewEvent()
