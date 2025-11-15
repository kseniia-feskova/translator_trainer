package presentation.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.shared.R
import com.presentation.ui.gradientBrush
import com.presentation.ui.redDarkColor
import presentation.ui.AppTypography
import presentation.ui.views.OrView
import presentation.ui.views.buttons.CustomShadowButton
import presentation.ui.views.input.OutlinedTextField
import presentation.ui.views.input.OutlinedTextFieldWithVisibility


@Composable
fun LoginForm(
    state: AuthUIState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onGuestClicked: () -> Unit = {},
    onCreateAccountClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = stringResource(R.string.login_subtitle),
                style = AppTypography.displayMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.email,
                label = stringResource(R.string.email_label),
                onValueChange = onEmailChanged,
                isError = state.error == AuthError.EMPTY_FIELDS && state.email.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldWithVisibility(
                value = state.password,
                label = stringResource(R.string.password_label),
                onValueChange = onPasswordChanged,
                isError = state.error == AuthError.EMPTY_FIELDS && state.email.isEmpty()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                stringResource(R.string.forgot_password_label),
                style = AppTypography.titleSmall,
                modifier = Modifier.align(Alignment.End)
            )

            if (state.error != null) {
                Text(
                    text = stringResource(state.error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            OrView()

            Spacer(modifier = Modifier.height(16.dp))

            CustomShadowButton(
                isEnabled = true,
                text = stringResource(R.string.guest_button),
                onClick = onGuestClicked
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomShadowButton(
                isEnabled = false,
                text = "Continue with Google",
                icon = painterResource(R.drawable.ic_google)
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable { onCreateAccountClicked() },
                text = stringResource(R.string.create_account_button),
                style = AppTypography.displaySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomShadowButton(
                text = stringResource(R.string.login_button),
                onClick = onLoginClicked
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoginFormPreview() {
    Surface(
        modifier = Modifier.background(
            brush = gradientBrush,
        )
    ) {
        LoginForm(
            state = AuthUIState()
        )
    }
}