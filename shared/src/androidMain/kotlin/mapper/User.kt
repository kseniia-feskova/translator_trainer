package mapper

import data.model.auth.FirebaseAuthRequest
import data.model.user.UserEntity
import data.model.user.UserResult
import presentation.model.FirebaseUser
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

fun UserResult.toUI(): presentation.model.UserResult {
    return when (this) {
        UserResult.New -> presentation.model.UserResult.New
        is UserResult.Error -> presentation.model.UserResult.Error(this.message)
        is UserResult.Existing -> presentation.model.UserResult.Existing(this.user.toUI())
    }
}

fun FirebaseAuthRequest.toUI(): FirebaseUser {
    return FirebaseUser(
        uuid = uuid,
        email = email,
        displayName = displayName,
        photo = photo,
        phone = phone
    )
}


fun FirebaseUser.toData(): FirebaseAuthRequest {
    return FirebaseAuthRequest(
        uuid = uuid,
        email = email,
        displayName = displayName,
        photo = photo,
        phone = phone
    )
}