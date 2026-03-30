package data.model.user


sealed class UserResult {
    data class Existing(val user: UserEntity) : UserResult()
    object New : UserResult()
    data class Error(val message: String) : UserResult()
}