package com.presentation.ui.screens.newset

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.R
import com.presentation.model.WordUI
import com.presentation.navigation.BottomNavigationBar
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.fieldColors
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BaseTopView
import com.presentation.ui.views.Loader
import com.presentation.ui.views.SelectingWordWithStatusView
import com.presentation.ui.whiteColor

@Composable
fun NewSetScreen(
    state: NewSetUIState,
    onNameChange: (String) -> Unit = {},
    onSaveCheckBoxChange: (Boolean) -> Unit = {},
    selectWord: (WordUI) -> Unit = {},
    saveSet: () -> Unit = {},
    searchQuery: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 120.dp)
        ) {
            BaseTopView(
                title = stringResource(R.string.new_set_title),
                leftIcon = Icons.Default.ArrowBackIosNew,
                onLeftClick = navigateUp
            )
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = state.name,
                onValueChange = { onNameChange(it) },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.set_name_label),
                        style = AppTypography.titleSmall
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                isError = state.error == NewSetError.TAKEN_SET_NAME || state.error == NewSetError.EMPTY_FIELDS,
            )

            if (state.error != null) {
                Text(
                    text = stringResource(state.error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 12.dp)
                )
            } else {
                Spacer(Modifier.height(20.dp))
            }
            Text(
                text = stringResource(R.string.selected_words_subtitle, state.countOfSelected),
                style = AppTypography.titleLarge.copy(
                    color = bgColor,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(horizontal = 20.dp)
            )


            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                value = state.query,
                onValueChange = { searchQuery(it) },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.search_label),
                        style = AppTypography.titleSmall
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        Icon(Icons.Default.Close, tint = bgColor, contentDescription = "Close",
                            modifier = Modifier.clickable { onClearClick() })
                    } else {
                        Icon(Icons.Default.Search, tint = bgColor, contentDescription = "Search")
                    }
                }
            )
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
            ) {
                items(state.words.keys.toList()) {
                    SelectingWordWithStatusView(
                        modifier = Modifier.padding(vertical = 6.dp),
                        word = it,
                        isSelected = state.words[it] ?: false,
                        onClick = selectWord
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = state.isSaveChecked,
                    onCheckedChange = onSaveCheckBoxChange,
                    colors = SwitchDefaults.colors().copy(
                        uncheckedTrackColor = lightLilaColor,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = whiteColor,
                        checkedTrackColor = bgColor,
                        checkedThumbColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.save_words_to_set_checkbox),
                    style = MaterialTheme.typography.titleSmall.copy(color = bgColor)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            CustomShadowButton(
                onClick = { saveSet() },
                text = stringResource(R.string.save_set_btn)
            )
        }
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                Loader(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

private val listOfStates = listOf(
    NewSetUIState(loading = true),
    NewSetUIState(words = smallList.take(3).toSelectingMap()),
    NewSetUIState(words = smallList.toSelectingMap()),
    NewSetUIState(
        words = smallList.filter { it.resText == "Mutter" || it.originalText == "Mutter" }
            .toSelectingMap(),
        query = "Mutter"
    ),
    NewSetUIState(words = smallList.toSelectingMap(), isSaveChecked = true),
    NewSetUIState(words = smallList.toSelectingMap(), name = "К центру Земли"),
    NewSetUIState(
        words = smallList.toSelectingMap(),
        name = "К центру Земли",
        error = NewSetError.INTERNET_CONNECTION_ERROR
    ),
)

private class PreviewProvider : PreviewParameterProvider<NewSetUIState> {
    override val values: Sequence<NewSetUIState>
        get() = listOfStates.asSequence()
}

@Preview
@Composable
fun NewSetScreenPreview(@PreviewParameter(PreviewProvider::class) state: NewSetUIState) {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                NewSetScreen(
                    state = state
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        })
    }
}

fun <T> List<T>.toSelectingMap(): Map<T, Boolean> {

    val map = mutableMapOf<T, Boolean>()
    this.forEach { map[it] = false }
    return map.toMap()
}