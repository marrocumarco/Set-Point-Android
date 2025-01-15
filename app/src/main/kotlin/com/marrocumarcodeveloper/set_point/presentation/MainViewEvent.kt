package com.marrocumarcodeveloper.set_point.presentation

sealed class MainViewEvent
data object OnClickPLayerOneScoredEvent : MainViewEvent()
data object OnClickPLayerTwoScoredEvent : MainViewEvent()
data object OnClickUndoEvent : MainViewEvent()
data object OnClickSettingsEvent : MainViewEvent()
data object OnSettingsShownEvent : MainViewEvent()