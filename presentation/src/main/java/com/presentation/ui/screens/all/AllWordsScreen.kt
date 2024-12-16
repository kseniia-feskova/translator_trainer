package com.presentation.ui.screens.all

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.presentation.model.Level
import com.presentation.model.WordUI
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.views.BasicTopView
import com.presentation.ui.views.Loader
import com.presentation.ui.views.SearchBarView
import com.presentation.ui.views.WordMenuView
import com.presentation.ui.views.WordWithStatusView
import com.presentation.utils.ALL_WORDS

@Composable
fun AllWordsScreen(
    state: AllWordsUIState,
    searchQuery: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onWordSelected: (WordUI, Offset) -> Unit = { _, _ -> },
    onDismissRequest: () -> Unit = {},
    onEdit: (WordUI) -> Unit = {},
    onDelete: (WordUI) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {

    Column(modifier = Modifier.padding(bottom = 64.dp)) {

        BasicTopView(
            title = ALL_WORDS,
            leftIcon = Icons.Default.ArrowBackIosNew,
            onLeftClick = onBackPressed
        )
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
            items(state.words) {
                WordWithStatusView(
                    modifier = Modifier.padding(vertical = 6.dp),
                    word = it,
                    onLongClicked = onWordSelected
                )
            }
        }
        if (state.selectedItem != null && state.popupOffset != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .clickable(
                        onClick = { onDismissRequest() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
            WordMenuView(
                popupOffset = state.popupOffset,
                selectedItem = state.selectedItem,
                onDismissRequest = onDismissRequest,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}


private val listOfStates = listOf(
    AllWordsUIState(loading = true),
    AllWordsUIState(words = smallList.take(3)),
    AllWordsUIState(words = smallList),
    AllWordsUIState(
        words = smallList.filter { it.resText == "Mutter" || it.originalText == "Mutter" },
        query = "Mutter"
    ),
    AllWordsUIState(words = smallList.filter { it.level == Level.NEW }),
    AllWordsUIState(words = smallList, selectedItem = smallList[3], popupOffset = Offset(12f, 59f))

)

@Composable
fun StarsRow(modifier: Modifier = Modifier, stars: List<Boolean>) {
    Row(modifier = modifier) {
        stars.forEach {
            Icon(
                if (it) Icons.Default.Star else Icons.Default.StarOutline,
                "Star",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

}

private class PreviewProvider : PreviewParameterProvider<AllWordsUIState> {
    override val values: Sequence<AllWordsUIState>
        get() = listOfStates.asSequence()
}

@Preview()
@Composable
fun TranslateViewPreview(@PreviewParameter(PreviewProvider::class) state: AllWordsUIState) {
    AppTheme {
        Surface {
            AllWordsScreen(
                state = state
            )
        }
    }
}
