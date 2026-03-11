package presentation.ui.views

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.views.CustomRadioButton
import kotlinx.coroutines.launch
import presentation.model.WordUI
import presentation.model.WordViewData
import presentation.test.smallList
import presentation.ui.AppTypography
import presentation.ui.screens.all.StarsRow
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableWordWithStatusView(
    modifier: Modifier = Modifier,
    word: WordViewData,
    onRevealed: (WordViewData) -> Unit = {},
    onCollapsed: (WordViewData) -> Unit = {},
    onRemove: (WordViewData) -> Unit = {},
    onEdit: (WordViewData) -> Unit = {}
) {
    SwipeableItemWithActions(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(Color.White),
        isRevealed = word.isOptionRevealed,
        onExpanded = { onRevealed(word) },
        onCollapsed = { onCollapsed(word) },
        actions = {
            ActionIcon(
                onClick = { onRemove(word) },
                backgroundColor = redDarkColor,
                icon = Icons.Default.Delete,
                modifier = Modifier.fillMaxHeight()
            )
            ActionIcon(
                onClick = { onEdit(word) },
                backgroundColor = darkColor,
                icon = Icons.Default.Edit,
                modifier = Modifier.fillMaxHeight()
            )
        }
    ) {
        WordWithStatus(Modifier, word.data)
    }
}

@Composable
fun WordWithStatus(
    modifier: Modifier = Modifier,
    word: WordUI
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 20.dp)
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = word.originalText,
                style = AppTypography.titleLarge.copy(color = darkColor)
            )
            Text(
                text = word.resText,
                style = AppTypography.titleSmall.copy(color = darkColor)
            )
        }

        StarsRow(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(12.dp),
            stars = word.level.convertToStars()
        )
    }
}

@Composable
fun SelectingWordWithStatusView(
    modifier: Modifier = Modifier,
    word: WordUI,
    isSelected: Boolean,
    onClick: (WordUI) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        CustomRadioButton(
            modifier = Modifier.size(32.dp),
            selected = isSelected,
            onClick = { onClick(word) },
            unselectedColor = lightLilaColor,
            selectedColor = darkColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onClick(word) })
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = word.originalText,
                    style = AppTypography.titleLarge.copy(color = darkColor)
                )
                Text(text = word.resText, style = AppTypography.titleSmall.copy(color = darkColor))
            }

            StarsRow(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(12.dp),
                stars = word.level.convertToStars()
            )
        }
    }
}


@Composable
@Preview
fun SwipeableWordWithStatusPreview() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier.background(darkColor)
            ) {
                //WordWithStatusView(modifier = Modifier.padding(12.dp), word = smallList.first())

                SelectingWordWithStatusView(
                    modifier = Modifier.padding(12.dp),
                    word = smallList.first(),
                    isSelected = false
                )

                SwipeableWordWithStatusView(
                    modifier = Modifier.padding(12.dp),
                    word = WordViewData(smallList.first(), false),
                )

                WordWithStatus(
                    modifier = Modifier.padding(12.dp),
                    word = smallList.first(),
                )
            }
        }
    }
}

@Composable
fun ActionIcon(
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = Color.White
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Composable
fun SwipeableItemWithActions(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var contextMenuWidth by remember {
        mutableFloatStateOf(0f)
    }
    val offset = remember { Animatable(initialValue = 0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if (isRevealed) {
            offset.animateTo(contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier.onSizeChanged {
                contextMenuWidth = it.width.toFloat()
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(true) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset =
                                    (offset.value + dragAmount).coerceIn(0f, contextMenuWidth)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}