package com.marrocumarcodeveloper.set_point.presentation.events

internal sealed class SettingsViewEvent
internal data object OnClickTiebreakEvent : SettingsViewEvent()
internal data object OnClickNumberOfSetsSelectedEvent : SettingsViewEvent()
