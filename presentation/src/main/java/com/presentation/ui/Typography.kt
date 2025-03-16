package com.presentation.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.presentation.R

val bebasRegular = Font(R.font.bebas_neue_regular, FontWeight.Normal)
val bebasCyrillic = Font(R.font.bebas_neue_cyrillic, FontWeight.Normal)

val BebasFont = FontFamily(
    fonts = listOf(bebasCyrillic, bebasRegular)
)
val NunitoFont = FontFamily(
    fonts = listOf(
        Font(R.font.nunito_bold, FontWeight.Bold),
        Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
        Font(R.font.nunito_regular, FontWeight.Normal),
        Font(R.font.nunito_light, FontWeight.Light),
        Font(R.font.nunito_extra_light, FontWeight.ExtraLight),
        Font(R.font.nunito_black, FontWeight.Black)
    )
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = BebasFont,
        fontSize = 32.sp
    ),
    titleSmall = TextStyle(
        fontFamily = NunitoFont,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = onPrimaryColorLight
    ),
    titleMedium = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = onPrimaryColorLight
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = onPrimaryColorLight

    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = onPrimaryColorLight

    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = onPrimaryColorLight
    )
)