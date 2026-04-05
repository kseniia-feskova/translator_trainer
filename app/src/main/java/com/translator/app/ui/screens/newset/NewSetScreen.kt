package com.translator.app.ui.screens.newset

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.List
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
import com.example.translatortrainer.shared.R
import com.translator.app.ui.AppTheme
import com.translator.app.ui.darkColor
import com.translator.app.ui.fieldColors
import com.translator.app.ui.gradientBrush
import com.translator.app.ui.redDarkColor
import com.presentation.ui.views.Loader
import com.translator.app.ui.yellowColor
import presentation.model.WordUI
import presentation.test.bigList
import presentation.test.smallList
import com.translator.app.ui.AppTypography
import com.translator.app.ui.dialog.GuestLimitsDialog
import com.translator.app.ui.views.SelectingWordWithStatusView
import com.translator.app.ui.views.buttons.CustomShadowButton

@Composable
fun NewSetScreen(
    state: NewSetUIState,
    onNameChange: (String) -> Unit = {},
    onSaveCheckBoxChange: (Boolean) -> Unit = {},
    selectWord: (WordUI) -> Unit = {},
    saveSet: () -> Unit = {},
    sortedBy: () -> Unit = {},
    goToAccount: () -> Unit = {},
    hideLimitsError: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
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
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
//            BaseTopView(
//                title = stringResource(R.string.new_set_title),
//                leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
//                onLeftClick = navigateUp
//            )
            Spacer(Modifier.height(8.dp))
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
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = state.isSaveChecked,
                    onCheckedChange = onSaveCheckBoxChange,
                    colors = SwitchDefaults.colors().copy(
                        uncheckedTrackColor = Color.White,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = darkColor,
                        checkedTrackColor = darkColor,
                        checkedThumbColor = yellowColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.save_words_to_set_checkbox),
                    style = MaterialTheme.typography.titleSmall.copy(color = darkColor)
                )
            }

            Spacer(Modifier.height(4.dp))

            Row {
                Text(
                    text = stringResource(R.string.selected_words_subtitle, state.countOfSelected),
                    style = AppTypography.titleLarge.copy(
                        color = darkColor,
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.List,
                    tint = darkColor,
                    contentDescription = "SortIcon",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clickable {
                            sortedBy()
                        }
                )
            }


            //Spacer(Modifier.height(8.dp))

//            SearchBarView(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp),
//                query = state.query,
//                onQueryChange = searchQuery,
//                onClearClick = onClearClick,
//            )

            Spacer(Modifier.height(4.dp))

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
        CustomShadowButton(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter),
            onClick = { saveSet() },
            text = stringResource(R.string.save_set_btn)
        )
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
    NewSetUIState(words = bigList.take(6).toSelectingMap()),
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
        })
    }
}

fun <T> List<T>.toSelectingMap(): Map<T, Boolean> {

    val map = mutableMapOf<T, Boolean>()
    this.forEach { map[it] = false }
    return map.toMap()
}