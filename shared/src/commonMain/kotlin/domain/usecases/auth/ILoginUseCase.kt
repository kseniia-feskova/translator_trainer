package domain.usecases.auth

import kotlinx.coroutines.flow.Flow

interface ILoginUseCase {
    suspend fun invoke(email: String, username: String, password: String): Result<String>

    fun listenUserId(): Flow<String?>
}

/*
*  при логине нужно проверить айди курса
*  если его нет, то показать диалог перед HomeScreen
*  взять список наборов для пользователя и выбрать из них один
*
* */