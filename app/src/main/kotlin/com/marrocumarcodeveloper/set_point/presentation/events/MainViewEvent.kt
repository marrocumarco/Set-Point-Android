package com.marrocumarcodeveloper.set_point.presentation.events

internal sealed class MainViewEvent
internal data object OnClickPLayerOneScoredEvent : MainViewEvent()
internal data object OnClickPLayerTwoScoredEvent : MainViewEvent()
internal data object OnClickUndoEvent : MainViewEvent()
internal data object OnClickSettingsEvent : MainViewEvent()
internal data class OnConfirmSettingsAlertClosedEvent(val confirm: Boolean) : MainViewEvent()
internal data object OnSettingsScreenClosedEvent : MainViewEvent()
internal data object OnClickResetEvent : MainViewEvent()
internal data object OnClickConfirmResetEvent : MainViewEvent()
internal data object OnClickCancelResetEvent : MainViewEvent()