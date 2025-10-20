package com.presentation.ui

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val whiteColor = Color(0xFFF9F9FF)
val bgColor = Color(0xFFEBEBFF)
val darkColor = Color(0xFF14063D)
val yellowColor = Color(0xFFFFE625)
val lightLilaColor = Color(0xFFD3D3DE)
val fieldNameColor = Color(0xFF616169)
val fieldValueColor = Color(0xFF3F3F42)
val fieldBorderColor = Color(0xFFA59BCA)
val viewLightColor = Color(0xFFEDEDEF)


val primaryColor = Color(0xFFA9A8AB)
val secondaryColor = Color(0xFFF9F9FB)
val accentColor = Color(0xFFD6D5DA)

val accentColor80 = Color(0xCCDEDEE1)
val accentColor50 = Color(0x80F4F4F5)

val greenColor = Color(0xFF2D7A32)
val redColor = Color(0xFFF17575)
val redDarkColor = Color(0xFFC02D2D)
val lightGreenColor = Color(0xFF83FFAD)


val surfaceLight = Color(0xFFFFFFFF)
val onSurfaceLight = Color(0xFF29292C)
val onPrimaryColorLight = Color(0xFF29282A)
val accentColorLight = Color(0xFF838693)

val gradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xE6FFFFFF),
        Color(0xFFF0F2FF),
        Color(0xFFB9C3FF)
    )
)

@Composable
fun fieldColors() = TextFieldDefaults.colors().copy(
    unfocusedLabelColor = fieldNameColor,
    focusedLabelColor = fieldNameColor,
    unfocusedContainerColor = Color.White,
    focusedContainerColor = Color.White,
    unfocusedIndicatorColor = fieldBorderColor,
    focusedIndicatorColor = fieldBorderColor,
    unfocusedTextColor = fieldValueColor,
    focusedTextColor = fieldValueColor,
    errorContainerColor = Color.White,
)