package com.translator.app.utils

fun Long.toTimeFormat(): String {
    val first = if (this / (60 * 1000L) < 10) {
        "0${(this / (60 * 1000L))}"
    } else this / (60 * 1000L)

    val second = if ((this / 1000L) < 10) {
        "0${(this / 1000L)}"
    } else {
        this / 1000L
    }
    return "$first:$second"
}

sealed interface TextToken {
    data class Word(val value: String) : TextToken
    data class Separator(val value: String) : TextToken
}

fun String.tokenize(): List<TextToken> {
    val regex = Regex("""\w+|[^\w]+""")
    return regex.findAll(this).map { match ->
        val value = match.value
        if (value.any { it.isLetterOrDigit() }) {
            TextToken.Word(value)
        } else {
            TextToken.Separator(value)
        }
    }.toList()
}

