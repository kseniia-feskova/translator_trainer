package presentation.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.bgColor
import com.presentation.ui.gradientBrush
import com.presentation.ui.views.Loader
import presentation.ui.AppTypography
import presentation.ui.dialog.GuestModeDialog
import presentation.ui.views.Circles

enum class AuthScreenState { LOGIN, REGISTER }

@Composable
fun AuthScreen(
    state: AuthUIState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onAuthStateChanged: () -> Unit = {},
    onGoogleSignClick: () -> Unit = {},
    onAuthClicked: () -> Unit = {},
    onGuestSelected: () -> Unit = {}
) = Box {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        GuestModeDialog(
            onConfirm = {
                showDialog = false
                onGuestSelected()
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column() {
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = if (state.screenState == AuthScreenState.LOGIN)
                    stringResource(R.string.login_marquee) else
                    stringResource(R.string.register_marquee),
                style = AppTypography.displayLarge
            )

            Spacer(Modifier.height(8.dp))
            Circles(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                isRegister = state.screenState == AuthScreenState.REGISTER
            )
            Spacer(Modifier.height(32.dp))
        }
        Box(
            modifier = Modifier
                .padding(top = 172.dp)
                .fillMaxSize()
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
                )
        ) {
            AnimatedContent(
                targetState = state.screenState,
                label = "AuthForm",
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { if (targetState == AuthScreenState.LOGIN) 1200 else -1200 },
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = FastOutSlowInEasing
                        )
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { if (targetState == AuthScreenState.LOGIN) -1200 else 1200 },
                        animationSpec = tween(
                            durationMillis = 1000,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
            ) { screen ->
                if (screen == AuthScreenState.LOGIN) {
                    LoginForm(
                        state = state,
                        onEmailChanged = onEmailChanged,
                        onPasswordChanged = onPasswordChanged,
                        onLoginClicked = onAuthClicked,
                        onGuestClicked = { showDialog = true },
                        onCreateAccountClicked = onAuthStateChanged,
                        onGoogleSignClick = onGoogleSignClick
                    )
                } else {
                    RegisterForm(
                        state = state,
                        onEmailChanged = onEmailChanged,
                        onPasswordChanged = onPasswordChanged,
                        onLoginClicked = onAuthClicked,
                        onGuestClicked = { showDialog = true },
                        onCreateAccountClicked = onAuthStateChanged,
                        onGoogleSignClick = onGoogleSignClick
                    )
                }
            }
        }
    }
    if (state.isLoading) {
        Loader(
            modifier = Modifier
                .width(80.dp)
                .align(Alignment.Center)
        )
    }
}

private val listOfStates = listOf(
    AuthUIState(),
    AuthUIState(email = "test@gmail.com"),
    AuthUIState(email = "test@gmail.com", error = AuthError.EMPTY_FIELDS),
    AuthUIState(password = "test@gmail.com"),
    AuthUIState(email = "test@gmail.com", password = "12345"),
    AuthUIState(
        email = "test@gmail.com",
        password = "12345",
        error = AuthError.USER_DOES_NOT_EXIST
    ),
    AuthUIState(
        email = "test@gmail.com",
        password = "12345",
        error = AuthError.WRONG_PASSWORD
    ),
    AuthUIState(screenState = AuthScreenState.REGISTER)
)

private class PreviewProvider : PreviewParameterProvider<AuthUIState> {
    override val values: Sequence<AuthUIState>
        get() = listOfStates.asSequence()
}

@Preview
@Composable
fun NewLoginViewPreview(@PreviewParameter(PreviewProvider::class) state: AuthUIState) {
    AppTheme {
        Surface {
            AuthScreen(state = state)
        }
    }
}
