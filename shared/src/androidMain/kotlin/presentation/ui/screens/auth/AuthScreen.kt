package presentation.ui.screens.auth

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
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
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.fieldColors
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.viewLightColor
import com.presentation.ui.views.GuestModeDialog
import com.presentation.ui.views.Loader
import com.presentation.ui.whiteColor
import presentation.ui.AppTypography

enum class AuthScreenState { LOGIN, REGISTER }

@Composable
fun AuthScreen(
    state: AuthUIState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onAuthStateChanged: () -> Unit = {},
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Spacer(Modifier.height(24.dp))
        MarqueeText(
            text = if (state.screenState == AuthScreenState.LOGIN) stringResource(R.string.login_marquee) + " "
            else stringResource(R.string.register_marquee) + " "
        )
        Spacer(Modifier.height(24.dp))
        Circles(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            isRegister = state.screenState == AuthScreenState.REGISTER
        )
        Spacer(Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    whiteColor,
                    shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
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
                        state,
                        onEmailChanged,
                        onPasswordChanged,
                        onAuthClicked,
                        onGuestClicked = { showDialog = true },
                        onAuthStateChanged
                    )
                } else {
                    RegisterForm(
                        state.email,
                        state.password,
                        state.error,
                        onEmailChanged,
                        onPasswordChanged,
                        onAuthClicked,
                        onGuestClicked = { showDialog = true },
                        onAuthStateChanged
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

@Composable
fun MarqueeText(text: String) {
    val repeatedText = text.repeat(10)

    Row(
        modifier = Modifier.basicMarquee(repeatDelayMillis = 1, velocity = 30.dp)
    ) {
        repeatedText.chunked(text.length).forEachIndexed { index, chunk ->
            val color = if (index % 2 == 0) darkColor else whiteColor
            Text(text = chunk, color = color, style = AppTypography.displayLarge)
        }
    }
}


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Circles(modifier: Modifier, isRegister: Boolean) {
    val transition = updateTransition(targetState = isRegister, label = "CirclesTransition")
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val circleSize = screenWidth.value / 3
    val center = 0f//screenWidth.value/2
    val circleOffset = circleSize * 1.5f
    val offset1 by transition.animateOffset(
        label = "lightLilaColor",
        transitionSpec = { tween(durationMillis = 1000, easing = EaseInOutQuad) }) { state ->
        if (state) Offset(center + circleOffset, 0f) else Offset(
            center - circleOffset,
            0f
        )  // 1 → 3
    }
    val offset2 by transition.animateOffset(
        label = "whiteColor",
        transitionSpec = { tween(durationMillis = 1000, easing = EaseInOutQuad) }) { state ->
        if (state) Offset(center, 0f) else Offset(center + circleOffset, 0f)   // 2 → 1
    }
    val offset3 by transition.animateOffset(
        label = "darkColor",
        transitionSpec = { tween(durationMillis = 1000, easing = EaseInOutQuad) }) { state ->
        if (state) Offset(center - circleOffset, 0f) else Offset(
            center,
            0f
        ) // 3 → 2 + поднятие вверх
    }
    Box(modifier = modifier.wrapContentWidth()) {
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .offset { offset1.round() }
                .background(lightLilaColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .offset { offset2.round() }
                .zIndex(if (isRegister) 1f else 0f)
                .background(whiteColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .offset { offset3.round() }
                .background(darkColor, shape = CircleShape)
        )
    }
}

@Composable
fun LoginForm(
    state: AuthUIState,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onGuestClicked: () -> Unit = {},
    onCreateAccountClicked: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
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
                isError = state.error == AuthError.EMPTY_FIELDS && state.email.isEmpty()
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
                        painter = painterResource(if (passwordVisible) R.drawable.ic_visible_pass else (R.drawable.ic_hide_pass)),
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
                isError = (state.error == AuthError.EMPTY_FIELDS && state.password.isEmpty()) || state.error == AuthError.WRONG_PASSWORD,
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
            CustomShadowButton(
                text = stringResource(R.string.guest_button),
                onClick = onGuestClicked
            )
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
                modifier = Modifier.clickable { onCreateAccountClicked() },
                text = stringResource(R.string.create_account_button),
                color = bgColor,
                style = AppTypography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomShadowButton(
                text = stringResource(R.string.login_button),
                onClick = onLoginClicked
            )
        }
    }
}

@Composable
fun RegisterForm(
    email: String,
    password: String,
    error: AuthError? = null,
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onLoginClicked: () -> Unit = {},
    onGuestClicked: () -> Unit = {},
    onCreateAccountClicked: () -> Unit = {},
    guestMode: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                stringResource(R.string.register_subtitle),
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(22f, TextUnitType.Sp)),
                color = bgColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
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
                isError = error == AuthError.EMPTY_FIELDS && email.isEmpty() || error == AuthError.EMAIL_TAKEN
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = onPasswordChanged,
                singleLine = true,
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                        painter = painterResource(if (passwordVisible) R.drawable.ic_visible_pass else (R.drawable.ic_hide_pass)),
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
                isError = (error == AuthError.EMPTY_FIELDS && password.isEmpty()),
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
            if (error != null) {
                Text(
                    text = stringResource(error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            if (error == null) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (!guestMode) {
                OrView()
                Spacer(modifier = Modifier.height(16.dp))
                CustomShadowButton(
                    text = stringResource(R.string.guest_button),
                    onClick = onGuestClicked
                )
            }
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
                modifier = Modifier.clickable { onCreateAccountClicked() },
                text = if (guestMode) stringResource(R.string.close_btn) else stringResource(R.string.login_subtitle),
                color = bgColor,
                style = AppTypography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomShadowButton(
                text = stringResource(R.string.signup_button),
                onClick = onLoginClicked
            )
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
fun CustomShadowButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
) {
    Box(modifier = modifier) {
        if (isEnabled) {
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
        }
        Button(
            onClick = { onClick() },
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(if (isEnabled) Color.White else whiteColor)
                .border(
                    1.dp,
                    color = fieldBorderColor,
                    shape = RoundedCornerShape(30.dp)
                ),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = Color.White,
                disabledContainerColor = whiteColor
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
                color = if (isEnabled) bgColor else lightLilaColor,
                style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

        }

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
