package presentation.utils

import java.util.UUID

fun String?.toUUID(): UUID? {
    if (this == null) return null
    return UUID.fromString(this)
}