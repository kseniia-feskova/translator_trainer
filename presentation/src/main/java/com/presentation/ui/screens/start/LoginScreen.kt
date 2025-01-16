package com.presentation.ui.screens.start

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.model.CoursePreview
import com.presentation.model.ruDeCourse
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.indicatorColorLight
import com.presentation.ui.onPrimaryColorLight
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.primaryColorLight
import com.presentation.ui.redDarkColor
import com.presentation.ui.views.ActionButton
import com.presentation.ui.views.CourseSelector

@Composable
fun LoginScreen(
    state: LoginUIState,
    onUsernameChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onSecondPasswordChanged: (String) -> Unit = {},
    onSwitchAuth: () -> Unit = {},
    onEnterClicked: () -> Unit = {},
    onCourseSelected: (CoursePreview) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 12.dp)
    ) {
        Text(
            text = "Добро пожаловать",
            modifier = Modifier.align(Alignment.TopCenter),
            style = MaterialTheme.typography.titleLarge
        )
        FlippableBox(
            modifier = Modifier
                .align(Alignment.Center),
            isFrontVisible = state.isLogin,
            frontContent = { mod, flip ->
                LoginContent(
                    modifier = mod,
                    username = state.username,
                    email = state.email,
                    password = state.password,
                    switchAuth = {
                        flip()
                        onSwitchAuth()
                    },
                    onEmailChanged = onEmailChanged,
                    onUsernameChanged = onUsernameChanged,
                    onPasswordChanged = onPasswordChanged,
                )

            },
            backContent = { mod, flip ->
                SignUpContent(
                    mod,
                    email = state.email,
                    username = state.username,
                    password = state.password,
                    repeatPassword = state.repeatPassword,
                    switchAuth = {
                        flip()
                        onSwitchAuth()
                    },
                    onUsernameChanged = onUsernameChanged,
                    onEmailChanged = onEmailChanged,
                    onPasswordChanged = onPasswordChanged,
                    onRepeatPasswordChanged = onSecondPasswordChanged
                )
            }
        )

        if (state.error != null) {
            Text(
                text = state.error,
                style = MaterialTheme.typography.bodyMedium.copy(color = redDarkColor),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 100.dp)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            if (!state.isLogin) {
                Text("Оберiть курс")
                SelectCourse(state.selectedCourse) {
                    onCourseSelected(it)
                }
            }
            ActionButton(
                onClick = { onEnterClicked() },
                enabled = state.isValid
            ) {
                Text(
                    text = "Войти",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }
        }
    }
}

@Composable
fun SelectCourse(selectedCourse: CoursePreview?, onSelect: (CoursePreview) -> Unit) {
    Column (modifier = Modifier.fillMaxWidth()) {
        CourseSelector(
            checked = selectedCourse == ruDeCourse,
            course = ruDeCourse,
            onCheckedChange = { onSelect(ruDeCourse) }
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier,
    username: String,
    email: String,
    password: String,
    switchAuth: () -> Unit,
    onUsernameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 24.dp)
    ) {
        var isPasswordVisible by remember { mutableStateOf(false) }

        Text(text = "Войдите в систему", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = username,
            maxLines = 1,
            onValueChange = { onUsernameChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(
                    "Имя пользователя",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = email,
            maxLines = 1,
            onValueChange = { onEmailChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(
                    "Почта",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = password,
            maxLines = 1,
            onValueChange = { onPasswordChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image =
                    if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val tint =
                    if (isPasswordVisible) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.tertiary

                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Toggle password visibility",
                        tint = tint
                    )
                }
            },
            placeholder = {
                Text(
                    "Пароль",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "или",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.tertiary)
            )

            Text(
                modifier = Modifier.clickable { switchAuth() },
                text = "Зарегестрироваться",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SignUpContent(
    modifier: Modifier,
    email: String,
    username: String,
    password: String,
    repeatPassword: String,
    switchAuth: () -> Unit,
    onUsernameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRepeatPasswordChanged: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 24.dp)
    ) {
        var isPasswordVisible by remember { mutableStateOf(false) }
        var isRepeatPasswordVisible by remember { mutableStateOf(false) }

        Text(text = "Зарегестрируйтесь", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = username,
            maxLines = 1,
            onValueChange = { onUsernameChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(
                    "Имя пользователя",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = email,
            maxLines = 1,
            onValueChange = { onEmailChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(
                    "Почта",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = password,
            maxLines = 1,
            onValueChange = { onPasswordChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image =
                    if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val tint =
                    if (isPasswordVisible) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.tertiary

                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Toggle password visibility",
                        tint = tint
                    )
                }
            },
            placeholder = {
                Text(
                    "Пароль",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 1,
            value = repeatPassword,
            onValueChange = { onRepeatPasswordChanged(it) },
            colors = TextFieldDefaults.colors().copy(
                cursorColor = onSurfaceLight,
                unfocusedIndicatorColor = indicatorColorLight,
                unfocusedContainerColor = primaryColorLight,
                focusedIndicatorColor = indicatorColorLight,
                focusedContainerColor = primaryColorLight,
                unfocusedTextColor = onPrimaryColorLight,
                focusedTextColor = onPrimaryColorLight
            ),
            visualTransformation = if (isRepeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = AppTypography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image =
                    if (isRepeatPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val tint =
                    if (isRepeatPasswordVisible) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.tertiary

                IconButton(onClick = { isRepeatPasswordVisible = !isRepeatPasswordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Toggle password visibility",
                        tint = tint
                    )
                }
            },
            placeholder = {
                Text(
                    "Повторите пароль",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                )
            }
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "или",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.tertiary)
            )
            Text(
                modifier = Modifier.clickable { switchAuth() },
                text = "Войти с аккаунтом",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    AppTheme {
        Surface {
            LoginScreen(
                LoginUIState(

                )
            )
        }
    }
}

@Composable
@Preview
fun LoginScreenBackPreview() {
    val state = remember { mutableStateOf(LoginUIState(isLogin = false)) }
    AppTheme {
        Surface {
            LoginScreen(
                state = state.value,
                onSwitchAuth = {
                    state.value = state.value.copy(isLogin = !state.value.isLogin)
                }
            )
        }
    }
}


@Composable
fun FlippableBox(
    modifier: Modifier = Modifier,
    isFrontVisible: Boolean = true,
    frontContent: @Composable BoxScope.(modifier: Modifier, onFlip: () -> Unit) -> Unit,
    backContent: @Composable BoxScope.(modifier: Modifier, onFlip: () -> Unit) -> Unit,
) {
    var flipped by remember { mutableStateOf(!isFrontVisible) } // Инвертируем начальное значение
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(800)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!flipped) 1f else 0f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (flipped) 1f else 0f,
        animationSpec = tween(500)
    )
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardSize = screenHeight * 0.5f
    Box(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .height(cardSize)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = animateFront
                    rotationY = 0f
                }
        ) {
            frontContent(
                Modifier.align(Alignment.Center)
            ) {
                flipped = !flipped
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = animateBack
                    scaleX = -1f
                }
        ) {
            backContent(
                Modifier.align(Alignment.Center)
            ) {
                flipped = !flipped
            }
        }
    }
}