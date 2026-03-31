package data.api

import data.prefs.ITokenStorage
import io.ktor.client.HttpClient

expect fun provideHttpClient(tokenProvider: ITokenStorage): HttpClient
