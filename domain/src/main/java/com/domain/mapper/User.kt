package com.domain.mapper

import com.data.model.UserEntity
import com.presentation.model.User

fun UserEntity.toUI(): User {
    return User(userId = id, username = username ?: email, email = email, photo = photoUrl)
}