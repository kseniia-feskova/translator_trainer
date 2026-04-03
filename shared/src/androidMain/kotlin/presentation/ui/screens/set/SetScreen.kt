package presentation.ui.screens.set

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.gradientBrush
import com.presentation.ui.redDarkColor
import com.presentation.ui.views.ProgressForSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import presentation.model.LessonType
import presentation.model.WordUI
import presentation.test.smallList
import presentation.ui.AppTypography
import presentation.ui.views.BaseTopView
import presentation.ui.views.CardsSet
import presentation.ui.views.buttons.CustomShadowButton

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SetScreen(
    state: SetUIState,
    events: SharedFlow<SetUIEvent>,
    addWordToKnow: (WordUI) -> Unit = {},
    addWordToLearn: (WordUI) -> Unit = {},
    resetCardSet: () -> Unit = {},
    showCourseSelection: (Boolean) -> Unit = {},
    startCourse: (LessonType) -> Unit = {},
    navigateToEdit: () -> Unit = {},
    navigateUp: () -> Unit = {},
    downloadSet: () -> Unit = {},
    exportCsvToUri: (Uri, Context) -> Unit = { _, _ -> }
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            exportCsvToUri(it, context)
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                SetUIEvent.RequestExport -> {
                    launcher.launch("${state.name}.csv")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        BaseTopView(
            title = state.name,
            onRightClick = navigateToEdit,
            onLeftClick = navigateUp
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_download), contentDescription = "download",
            tint = darkColor,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .size(30.dp)
                .align(Alignment.TopEnd)
                .clickable { downloadSet() }
        )
        Column(modifier = Modifier.align(Alignment.Center)) {
            if (state.words != null) {
                CardsSet(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 20.dp),
                    cardHeight = cardHeight,
                    state.words.first,
                    state.words.second,
                    onRightSwipe = { addWordToKnow(it) },
                    onLeftSwipe = { addWordToLearn(it) }
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Left",
                            tint = darkColor
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = "Right",
                            tint = darkColor
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                        .fillMaxWidth()
                        .zIndex(2f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.sorted_words),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        style = AppTypography.titleLarge.copy(color = darkColor)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomShadowButton(
                        onClick = { resetCardSet() },
                        text = stringResource(R.string.sort_again_btn),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ProgressForSet(
                modifier = Modifier.padding(horizontal = 20.dp),
                current = state.knowWords,
                all = state.allWords
            )

            if (state.error != null) {
                Text(
                    text = stringResource(state.error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        CustomShadowButton(
            onClick = { showCourseSelection(true) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            text = stringResource(R.string.study_btn)
        )

        AnimatedVisibility(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.BottomCenter),
            visible = state.selectLessonVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            ),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(1000))
        ) {
            SelectLessonType(
                modifier = Modifier
                    .height(screenHeight * 0.5f)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                showCourseSelection = showCourseSelection,
                startCourse = startCourse
            )
        }
    }
}

@Composable
fun SelectLessonType(
    modifier: Modifier,
    showCourseSelection: (Boolean) -> Unit = {},
    startCourse: (LessonType) -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(
                brush = gradientBrush,
                shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp)
            )
            .border(
                width = 1.dp,
                color = bgColor,
                shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp)
            )
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        Text(
            stringResource(R.string.select_lesson_title),
            style = AppTypography.displaySmall,
            color = darkColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(LessonType.entries.toTypedArray()) { lesson ->
                    CustomShadowButton(
                        text = lesson.btnName,
                        onClick = {
                            showCourseSelection(false)
                            startCourse(lesson)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 32.dp)
            ) {
                Text(
                    stringResource(R.string.close_btn),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { showCourseSelection(false) },
                    color = darkColor,
                    style = AppTypography.displaySmall
                )

            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000999)
@Composable
fun CardSetScreenPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("CardSetScreenPreview", "paddings = $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                SetScreen(
                    state = SetUIState(
                        name = "New set",
                        words = Pair(
                            smallList.last(),
                            smallList.first()
                        )
                    ),
                    events = MutableStateFlow<SetUIEvent>(SetUIEvent.RequestExport).asSharedFlow()
                )
            }
        }
        )
    }
}


@Preview
@Composable
fun EmptyCardSetScreenPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("CardSetScreenPreview", "paddings = $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                SetScreen(
                    state = SetUIState(words = null),
                    events = MutableStateFlow<SetUIEvent>(SetUIEvent.RequestExport).asSharedFlow()
                )
            }
        })
    }
}

@Preview
@Composable
fun CardSetScreenWithLessonsPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("CardSetScreenPreview", "paddings = $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                SetScreen(
                    state = SetUIState(
                        name = "New set",
                        words = Pair(
                            smallList.last(),
                            smallList.first()
                        ),
                        selectLessonVisible = true
                    ),
                    events = MutableStateFlow<SetUIEvent>(SetUIEvent.RequestExport).asSharedFlow()
                )
            }
        })
    }
}