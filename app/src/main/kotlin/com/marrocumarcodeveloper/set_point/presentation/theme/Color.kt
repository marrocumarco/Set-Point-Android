package com.marrocumarcodeveloper.set_point.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val OpticYellow = Color(0xFFCCFF00)
val DarkerYellowGreen = Color(0xFF99CC00)
val TennisCourtBlue = Color(0xFF0066CC)
val DarkerBlue = Color(0xFF004C99)
val BrightRed = Color(0xFFFF4D4D)

internal val wearColorPalette: Colors = Colors(
    primary = OpticYellow,
    primaryVariant = DarkerYellowGreen,
    secondary = TennisCourtBlue,
    secondaryVariant = DarkerBlue,
    error = BrightRed,
    onPrimary = Color.DarkGray,
    onSecondary = Color.White,
    onError = Color.Black
)