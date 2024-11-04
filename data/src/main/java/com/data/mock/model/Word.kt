package com.data.mock.model

data class Word(
    val id: String,
    val text: String,
    val translate: String,
    val status: Status
) {
    companion object {
        fun createId(text: String, translate: String): String {
            return "$text/$translate"
        }
    }
}
