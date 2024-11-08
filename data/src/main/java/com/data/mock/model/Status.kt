package com.data.mock.model

enum class Status {
    NEW,
    LEARNING,
    GOOD_LEARNING,
    KNOW;

    fun convertToStars(): List<Boolean> {
        return when (this) {
            KNOW -> listOf(true, true, true)
            GOOD_LEARNING -> listOf(true, true, false)
            LEARNING -> listOf(true, false, false)
            else -> listOf(false, false, false)
        }
    }
}

