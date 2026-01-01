package presentation.viewmodel

data class MainUiState(
    val isUserAuthorized: Boolean = false,
    val isInternetAvailable: Boolean = true,
    val showSplash: Boolean = true,
    val isNewUser: Boolean? = null
)
