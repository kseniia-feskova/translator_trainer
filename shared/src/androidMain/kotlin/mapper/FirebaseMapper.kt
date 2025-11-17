package mapper

import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomain(): presentation.model.FirebaseUser {
    return presentation.model.FirebaseUser(
        uuid = this.uid,
        displayName = displayName,
        email = email,
        phone = phoneNumber,
        photo = photoUrl.toString()
    )
}