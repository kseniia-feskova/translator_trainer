package domain.di

actual fun platformModule() =
    listOf(databaseModule, preferencesModule, translateModule)
