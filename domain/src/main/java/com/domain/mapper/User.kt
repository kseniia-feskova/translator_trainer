package com.domain.mapper

import com.data.model.UserEntity
import com.presentation.model.UserUI

fun UserEntity.toUI(): UserUI {
    return UserUI(
        userId = id,
        username = username ?: email,
        email = email,
        photo = photoUrl,
        courses = courses
    )
}