package com.presentation.utils

import com.presentation.model.Level
import com.presentation.model.WordUI

fun List<WordUI>.selectLevel(): Course {
    val statuses = this.map { it.level }
    return if (statuses.contains(Level.NEW)) Course.SELECT_RUSSIAN
    else Course.SELECT_DEUTSCH
}

enum class Course(val route: String) {
    SELECT_RUSSIAN("select_russian"),
    SELECT_DEUTSCH("select_deutsch")
}