package presentation.utils

import java.util.UUID

fun String?.toUUID(): UUID? {
    if (this == null) return null
    return UUID.fromString(this)
}


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
