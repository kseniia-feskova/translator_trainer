package com.presentation.ui

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val whiteColor = Color(0xFFFAFAFA)
val bgColor = Color(0xFF6860B4)
val darkColor = Color(0xFF4D4386)
val lightLilaColor = Color(0xFFDAD6F5)
val fieldNameColor = Color(0xFF575765)
val fieldValueColor = Color(0xFF37373D)
val fieldBorderColor = Color(0xFFB5AEEC)
val viewLightColor = Color(0xFFDBD5F8)


val primaryColor = Color(0xFF756AB2)
val secondaryColor = Color(0xFFF9F9FB)
val accentColor = Color(0xFFD9CDFD)

val accentColor80 = Color(0xCCD9CDFD)
val accentColor50 = Color(0x80D9CDFD)

val greenColor = Color(0xFF2D7A32)
val redColor = Color(0xFFF17575)
val redDarkColor = Color(0xFFC02D2D)
val lightGreenColor = Color(0xFF83FFAD)


val surfaceLight = Color(0xFFFFFFFF)
val onSurfaceLight = Color(0xFF1C1A23)
val primaryColorLight = Color(0xFFF9FAFB)
val onPrimaryColorLight = Color(0xFF100F11)

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