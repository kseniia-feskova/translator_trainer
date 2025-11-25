package presentation.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.fieldColors
import com.presentation.ui.fieldValueColor
import com.presentation.ui.gradientBrush
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.redDarkColor
import com.presentation.ui.views.Loader
import com.presentation.ui.yellowColor
import presentation.navigation.BottomNavigationBar
import presentation.ui.AppTypography
import presentation.ui.dialog.GuestLimitsDialog
import presentation.ui.views.HomeTopView
import presentation.ui.views.buttons.CustomShadowButton
import presentation.utils.Language

@Composable
fun HomeScreen(
    state: HomeUIState,
    goToAccount: () -> Unit = {},
    hideLimitsError: () -> Unit = {},
    onWordInput: (String) -> Unit = {},
    onEnterText: () -> Unit = { },
    onSaveClick: () -> Unit = {},
    onLanguageChange: (Language) -> Unit = {},
    onAlterTextSelected: (String) -> Unit = {}
) {
    var showTopView by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) { showTopView = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {

        if (state.limitsError) {
            GuestLimitsDialog(
                modifier = Modifier.align(Alignment.Center),
                toAccountSetting = {
                    hideLimitsError()
                    goToAccount()
                },
                dismissDialog = hideLimitsError
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            AnimatedVisibility(
                modifier = Modifier
                    .wrapContentHeight(),
                visible = showTopView,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(1000)
                ),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                HomeTopView(stringResource(R.string.home_title))
            }

            Spacer(modifier = Modifier.height(18.dp))
            Column {
                Spacer(Modifier.height(24.dp))

                if (state.originalLanguage != null) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        value = state.inputText,
                        onValueChange = { onWordInput(it) },
                        singleLine = true,
                        label = {
                            Text(
                                stringResource(state.originalLanguage.getRes()),
                                style = AppTypography.titleSmall
                            )
                        },
                        placeholder = {
                            Text(
                                stringResource(state.originalLanguage.getRes()),
                                style = AppTypography.titleSmall
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = fieldColors(),
                        textStyle = AppTypography.titleSmall,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done // Изменяем кнопку на "Готово"
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onEnterText()
                                keyboardController?.hide() // Скрываем клавиатуру
                            }
                        )
                    )
                }

                Spacer(Modifier.height(24.dp))
                if (state.originalLanguage != null && state.resLanguage != null) {
                    LanguageSwitch(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        state.originalLanguage,
                        state.resLanguage
                    ) { onLanguageChange(it) }
                }

                Spacer(Modifier.height(24.dp))

                if (state.resLanguage != null) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        value = state.translatedText,
                        enabled = false,
                        onValueChange = {},
                        singleLine = true,
                        label = {
                            Text(
                                stringResource(state.resLanguage.getRes()),
                                style = AppTypography.titleSmall,
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = fieldColors().copy(
                            disabledTextColor = fieldValueColor,
                            disabledContainerColor = Color.White,
                            disabledIndicatorColor = fieldBorderColor
                        ),
                        textStyle = AppTypography.titleSmall,
                        trailingIcon = {
                            if (state.isWordSaved) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    tint = darkColor,
                                    contentDescription = "Saved"
                                )
                            } else {
                                if (state.translatedText.isNotEmpty()) {
                                    Icon(
                                        Icons.Default.Add,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .clickable { onSaveClick() }
                                            .background(color = yellowColor, shape = CircleShape)
                                            .border(1.dp, shape = CircleShape, color = darkColor),
                                        tint = darkColor,
                                        contentDescription = "Saved"
                                    )
                                }
                            }
                        }
                    )
                }

                if (state.altTranslates?.isNotEmpty() == true) {
                    Spacer(Modifier.height(12.dp))
                    ListOfAlterTranslates(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 36.dp, end = 32.dp),
                        state.altTranslates,
                        onAlterTextSelected = onAlterTextSelected
                    )
                    Spacer(Modifier.height(12.dp))
                } else {
                    Spacer(Modifier.height(24.dp))
                }
                if (state.error != null) {
                    Text(
                        text = stringResource(state.error.msg),
                        style = AppTypography.titleSmall.copy(color = redDarkColor),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {

            CustomShadowButton(
                text = stringResource(R.string.translate_btn),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                onClick = {
                    onEnterText()
                }
            )
        }

        if (state.loading) {
            Loader(
                modifier = Modifier
                    .width(80.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ListOfAlterTranslates(
    modifier: Modifier = Modifier,
    alterTranslates: List<String>,
    onAlterTextSelected: (String) -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(alterTranslates) { _, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(true) {
                    onAlterTextSelected(item)
                }
            ) {
                Text(
                    item, modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    style = AppTypography.titleSmall.copy(color = darkColor)
                )
                Icon(
                    Icons.Default.Add,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable { onAlterTextSelected(item) }
                        .border(1.dp, shape = CircleShape, color = darkColor), tint = darkColor,
                    contentDescription = "Saved"
                )
            }
        }
    }
}

@Composable
fun LanguageSwitch(
    modifier: Modifier,
    originLang: Language,
    translateLang: Language,
    onClick: (Language) -> Unit
) {
    // Фиксируем первоначальные названия языков при первом рендере
    val firstText = stringResource(remember { originLang }.getRes())
    val secondText = stringResource(remember { translateLang }.getRes())
    var selectedLanguage by remember { mutableStateOf(firstText) }
    val transition = updateTransition(targetState = selectedLanguage, label = "Language Transition")

    val backgroundOffset by transition.animateDp(label = "Background Offset") { language ->
        if (language == firstText) 0.dp else (firstText.length * 14f).dp // Смещение фона
    }

    val backgroundWidth by transition.animateDp(label = "Background Width") { language ->
        if (language == firstText) (firstText.length * 14f).dp else (secondText.length * 14f).dp  // Можно подстроить под разную длину слов
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .wrapContentSize()
            .clickable {},
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = backgroundOffset)
                .width(backgroundWidth)
                .height(40.dp)
                .background(yellowColor, shape = RoundedCornerShape(8.dp))
        )

        SelectLanguages(
            modifier = Modifier.align(Alignment.CenterStart),
            originLang = originLang,
            translateLang = translateLang,
            firstText = firstText,
            secondText = secondText,
            onFirstClick = { language, firstText ->
                selectedLanguage = firstText
                onClick(language)
            },
            onSecondClick = { language, secondText ->
                selectedLanguage = secondText
                onClick(language)
            }
        )
    }
}

@Composable
fun SelectLanguages(
    originLang: Language,
    translateLang: Language,
    modifier: Modifier,
    firstText: String,
    secondText: String,
    onFirstClick: (Language, String) -> Unit,
    onSecondClick: (Language, String) -> Unit,
) {
    Log.e("SelectLanguages", "origin = $originLang, translate = $translateLang")
    Row(modifier = modifier) {
        val first = stringResource(originLang.getRes())
        Text(
            text = firstText,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable {
                    onFirstClick(
                        if (first == firstText) originLang else translateLang,
                        firstText
                    )
                },
            color = onSurfaceLight,
            style = AppTypography.displaySmall
        )

        Spacer(Modifier.width(8.dp))
        val second = stringResource(translateLang.getRes())
        Text(
            text = secondText,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .clickable {
                    onSecondClick(
                        if (second == secondText) translateLang else originLang,
                        secondText
                    )
                },
            color = onSurfaceLight,
            style = AppTypography.displaySmall
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        Surface {
            HomeScreen(
                state = HomeUIState(
                    inputText = "Katze",
                    originalLanguage = Language.GERMAN,
                    resLanguage = Language.RUSSIAN,
                )
            )
        }
    }
}

@Preview
@Composable
fun MainScreenWithButtonPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                HomeScreen(
                    state = HomeUIState().copy(
                        inputText = "Katze",
                        translatedText = "Котик",
                        altTranslates = listOf("Кот", "Кошак"),
                        originalLanguage = Language.GERMAN,
                        resLanguage = Language.RUSSIAN,
                    )
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}

@Preview
@Composable
fun MainScreenWithSavedButtonPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                HomeScreen(
                    state = HomeUIState().copy(
                        inputText = "Katze",
                        translatedText = "Котик",
                        altTranslates = listOf("Кот", "Кошак"),
                        originalLanguage = Language.GERMAN,
                        resLanguage = Language.RUSSIAN,
                        isWordSaved = true,
                    )
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(
                bgColor = Color(0xFFB9C3FF), navController = NavController(context)
            )
        }
        )
    }
}
