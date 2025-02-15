package com.marrocumarcodeveloper.set_point.presentation.events

internal sealed class SettingsViewEvent
internal data object OnClickTiebreakEvent : SettingsViewEvent()
internal data object OnClickNumberOfSetsSelectedEvent : SettingsViewEvent()
internal data object OnClickConfirmTileEvent : SettingsViewEvent()
internal data object OnClickConfirmEvent : SettingsViewEvent()
internal data object OnClickCancelEvent : SettingsViewEvent()
