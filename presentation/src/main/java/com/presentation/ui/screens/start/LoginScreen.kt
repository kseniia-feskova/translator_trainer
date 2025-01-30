package com.presentation.ui.screens.start

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.R
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.fieldColors
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.viewLightColor
import com.presentation.ui.whiteColor

@Composable
fun LoginScreen(
    state: LoginUIState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onGuestClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(24.dp))
        MarqueeText()
        Spacer(Modifier.height(24.dp))
        Circles(Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(32.dp))
        LoginForm(state, onEmailChanged, onPasswordChanged, onLoginClicked, onGuestClicked)
    }
}

@Composable
fun MarqueeText() {
    val text = stringResource(R.string.login_marquee)
    val repeatedText = text.repeat(10)

    Row(
        modifier = Modifier.basicMarquee(repeatDelayMillis = 1, velocity = 30.dp)
    ) {
        repeatedText.chunked(text.length).forEachIndexed { index, chunk ->
            val color = if (index % 2 == 0) darkColor else whiteColor
            Text(
                text = chunk,
                color = color,
                style = AppTypography.displayLarge
            )
        }
    }
}


@Composable
fun Circles(modifier: Modifier) {
    Box(modifier = modifier.width(300.dp)) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(lightLilaColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterEnd)
                .background(whiteColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomCenter)
                .background(darkColor, shape = CircleShape)
        )
    }
}

@Composable
fun LoginForm(
    state: LoginUIState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onGuestClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteColor, shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp))
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        var passwordVisible by remember { mutableStateOf(false) }
        Column {
            Text(
                stringResource(R.string.login_subtitle),
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(22f, TextUnitType.Sp)),
                color = bgColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                onValueChange = onEmailChanged,
                singleLine = true,
                label = {
                    Text(
                        stringResource(R.string.email_label),
                        style = AppTypography.titleSmall,
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                isError = state.error == LoginError.EMPTY_FIELDS && state.email.isEmpty()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = onPasswordChanged,
                singleLine = true,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                        imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = stringResource(R.string.password_label),
                        tint = fieldBorderColor
                    )
                },
                label = {
                    Text(
                        stringResource(R.string.password_label),
                        style = AppTypography.titleSmall,
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                isError = (state.error == LoginError.EMPTY_FIELDS && state.password.isEmpty()) || state.error == LoginError.WRONG_PASSWORD,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.forgot_password_label),
                style = AppTypography.titleSmall,
                color = fieldBorderColor,
                modifier = Modifier.align(Alignment.End)
            )
            if (state.error != null) {
                Text(
                    text = stringResource(state.error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            if (state.error == null) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            OrView()
            Spacer(modifier = Modifier.height(16.dp))
            CustomShadowButton(stringResource(R.string.guest_button), onClick = onGuestClicked)
            //TODO: Google Sign-In
//            CustomShadowButton(
//                text = "Continue with Google",
//                icon = painterResource(R.drawable.ic_google)
//            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.create_account_button),
                color = bgColor,
                style = AppTypography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomShadowButton(stringResource(R.string.login_button), onClick = onLoginClicked)
        }
    }
}

@Composable
fun OrView() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = viewLightColor)
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.or_view))
        Spacer(Modifier.width(8.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(2.dp)
                .background(color = viewLightColor)
        )
    }
}

@Composable
fun CustomShadowButton(text: String, icon: Painter? = null, onClick: () -> Unit = {}) {
    Box() {
        Spacer(
            modifier = Modifier
                .padding(top = 5.dp)
                .height(42.dp)
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(30.dp),
                    color = fieldBorderColor
                )
        )
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    color = fieldBorderColor,
                    shape = RoundedCornerShape(30.dp)
                ),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.White
            )
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = icon,
                    contentDescription = "Icon",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = text,
                color = bgColor,
                style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

        }

    }
}

private val listOfStates = listOf(
    LoginUIState(),
    LoginUIState(email = "test@gmail.com"),
    LoginUIState(email = "test@gmail.com", error = LoginError.EMPTY_FIELDS),
    LoginUIState(password = "test@gmail.com"),
    LoginUIState(email = "test@gmail.com", password = "12345"),
    LoginUIState(
        email = "test@gmail.com",
        password = "12345",
        error = LoginError.USER_DOES_NOT_EXIST
    ),
    LoginUIState(
        email = "test@gmail.com",
        password = "12345",
        error = LoginError.WRONG_PASSWORD
    ),
)

private class PreviewProvider : PreviewParameterProvider<LoginUIState> {
    override val values: Sequence<LoginUIState>
        get() = listOfStates.asSequence()
}

@Preview()
@Composable
fun NewLoginViewPreview(@PreviewParameter(PreviewProvider::class) state: LoginUIState) {
    AppTheme {
        Surface {
            LoginScreen(state = state)
        }
    }
}
