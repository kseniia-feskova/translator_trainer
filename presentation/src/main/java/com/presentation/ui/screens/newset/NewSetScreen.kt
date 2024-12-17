package com.presentation.ui.screens.newset

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.presentation.model.WordUI
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.indicatorColorLight
import com.presentation.ui.onPrimaryColorLight
import com.presentation.ui.onSurfaceLight
import com.presentation.ui.primaryColorLight
import com.presentation.ui.views.ActionButton
import com.presentation.ui.views.BasicTopView
import com.presentation.ui.views.Loader
import com.presentation.ui.views.SearchBarView
import com.presentation.ui.views.SelectingWordWithStatusView

data class NewSetUIState(
    val loading: Boolean = false,
    val name: String = "",
    val isSaveChecked: Boolean = false,
    val query: String = "",
    val words: Map<WordUI, Boolean> = mapOf(),
    val isActionEnable: Boolean = false
)

@Composable
fun NewSetScreen(
    state: NewSetUIState,
    onNameChange: (String) -> Unit = {},
    onSaveCheckBoxChange: (Boolean) -> Unit = {},
    selectWord: (WordUI) -> Unit = {},
    saveSet: () -> Unit = {},
    searchQuery: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 64.dp)
        ) {
            BasicTopView(
                title = "Новый набор",
                leftIcon = Icons.Default.ArrowBackIosNew,
                onLeftClick = navigateUp
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                value = state.name,
                onValueChange = { onNameChange(it) },
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
                        "Имя набора",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Switch(
                    checked = state.isSaveChecked,
                    onCheckedChange = onSaveCheckBoxChange,
                    colors = SwitchDefaults.colors().copy(
                        uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.onSurface,
                        checkedThumbColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Сохранять новые слова в этот набор",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "info",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            SearchBarView(
                Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp, bottom = 8.dp),
                state.query,
                onQueryChange = searchQuery,
                onClearClick = onClearClick,
                onFilterClick = onFilterClick
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

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
            ) {
                items(state.words.keys.toList()) {
                    Log.e(
                        "SelectingWordWithStatusView",
                        "Word = $it, selected = ${state.words[it]}"
                    )
                    SelectingWordWithStatusView(
                        modifier = Modifier.padding(vertical = 6.dp),
                        word = it,
                        isSelected = state.words[it] ?: false,
                        onClick = selectWord
                    )
                }
            }
        }

        ActionButton(
            onClick = { saveSet() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp),
            enabled = state.isActionEnable
        ) {
            Text(
                text = "Сохранить набор",
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )
        }
    }
}

private val listOfStates = listOf(
    NewSetUIState(loading = true),
    NewSetUIState(words = smallList.take(3).toSelectingMap()),
    NewSetUIState(words = smallList.toSelectingMap(), isActionEnable = true),
    NewSetUIState(
        words = smallList.filter { it.resText == "Mutter" || it.originalText == "Mutter" }
            .toSelectingMap(),
        query = "Mutter"
    ),
    NewSetUIState(words = smallList.toSelectingMap(), isSaveChecked = true),
    NewSetUIState(words = smallList.toSelectingMap(), name = "Путишествие к центру Земли"),

    )

private class PreviewProvider : PreviewParameterProvider<NewSetUIState> {
    override val values: Sequence<NewSetUIState>
        get() = listOfStates.asSequence()
}

@Preview()
@Composable
fun TranslateViewPreview(@PreviewParameter(PreviewProvider::class) state: NewSetUIState) {
    AppTheme {
        Surface {
            NewSetScreen(
                state = state
            )
        }
    }
}

fun <T> List<T>.toSelectingMap(): Map<T, Boolean> {

    val map = mutableMapOf<T, Boolean>()
    this.forEach { map[it] = false }
    return map.toMap()
}