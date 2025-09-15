package com.presentation.ui

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val whiteColor = Color(0xFFFAFAFA)
val bgColor = Color(0xFFB2B2B2)
val darkColor = Color(0xFF252526)
val lightLilaColor = Color(0xFFD3D3DE)
val fieldNameColor = Color(0xFF616169)
val fieldValueColor = Color(0xFF3F3F42)
val fieldBorderColor = Color(0xFFC2C2CB)
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
val primaryColorLight = Color(0xFFF9FAFB)
val onPrimaryColorLight = Color(0xFF29282A)

val indicatorColorLight = Color(0xFF8F919D)
val accentColorLight = Color(0xFF838693)


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