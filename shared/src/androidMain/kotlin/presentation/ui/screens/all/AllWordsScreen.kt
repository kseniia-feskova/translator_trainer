package presentation.ui.screens.all

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.views.Loader
import com.presentation.ui.views.SearchBarView
import com.presentation.ui.yellowColor
import domain.ALL_WORDS
import presentation.model.Level
import presentation.model.WordUI
import presentation.model.WordViewData
import presentation.test.smallListViews
import presentation.ui.views.BaseTopView
import presentation.ui.views.SwipeableWordWithStatusView

@Composable
fun AllWordsScreen(
    state: AllWordsUIState,
    searchQuery: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onDelete: (WordUI) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onRevealed: (WordViewData) -> Unit = {},
    onCollapsed: (WordViewData) -> Unit = {},
) {

    Column(modifier = Modifier.fillMaxSize()) {

        BaseTopView(
            title = ALL_WORDS,
            leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
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
                .background(Color.Transparent)
                .padding(horizontal = 12.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
        ) {
            items(state.words.toList()) {
                SwipeableWordWithStatusView(
                    modifier = Modifier.padding(vertical = 6.dp),
                    word = it,
                    onRemove = { onDelete(it.data) },
                    onRevealed = onRevealed,
                    onCollapsed = onCollapsed,
                )
            }
        }
    }
}


private val listOfStates = listOf(
    AllWordsUIState(loading = true),
    AllWordsUIState(words = smallListViews.take(3)),
    AllWordsUIState(words = smallListViews),
    AllWordsUIState(
        words = smallListViews.filter { it.data.resText == "Mutter" || it.data.originalText == "Mutter" },
        query = "Mutter"
    ),
    AllWordsUIState(words = smallListViews.filter { it.data.level == Level.NEW }),
    AllWordsUIState(words = smallListViews)

)

@Composable
fun StarsRow(modifier: Modifier = Modifier, stars: List<Boolean>) {
    Row(modifier = modifier) {
        stars.forEach {
            Icon(
                if (it) Icons.Default.Star else Icons.Outlined.Star,
                "Star",
                tint = if (it) yellowColor else darkColor,
            )
        }
    }

}

private class PreviewProvider : PreviewParameterProvider<AllWordsUIState> {
    override val values: Sequence<AllWordsUIState>
        get() = listOfStates.asSequence()
}

@Preview
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
