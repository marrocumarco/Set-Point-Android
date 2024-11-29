package com.marrocumarcodeveloper.set_point.presentation

sealed class MainViewEvent
object OnClickPLayerOneScoredEvent : MainViewEvent()
object OnClickPLayerTwoScoredEvent : MainViewEvent()
object OnClickResetScoreEvent : MainViewEvent()