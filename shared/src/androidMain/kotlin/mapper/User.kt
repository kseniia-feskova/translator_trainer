package mapper

import data.model.UserEntity
import presentation.model.UserUI

fun UserEntity.toUI(): UserUI {
    return UserUI(
        userId = id,
        username = username ?: email,
        email = email,
        photo = photoUrl,
        courses = courses.map { it.toUI() }
    )
}