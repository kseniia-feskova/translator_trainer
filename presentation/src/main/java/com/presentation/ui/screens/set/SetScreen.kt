package com.presentation.ui.screens.set

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.presentation.R
import com.presentation.model.LessonType
import com.presentation.model.WordUI
import com.presentation.navigation.BottomNavigationBar
import com.presentation.test.smallList
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BaseTopView
import com.presentation.ui.views.CardsSet
import com.presentation.ui.views.ProgressForSet

@Composable
fun SetScreen(
    state: SetUIState,
    addWordToKnow: (WordUI) -> Unit = {},
    addWordToLearn: (WordUI) -> Unit = {},
    resetCardSet: () -> Unit = {},
    showCourseSelection: (Boolean) -> Unit = {},
    startCourse: (LessonType) -> Unit = {},
    navigateToEdit: () -> Unit = {},
    navigateUp: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = screenHeight * 0.25f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BaseTopView(
            title = state.name,
            rightIcon = Icons.Default.Edit,
            leftIcon = Icons.Default.ArrowBackIosNew,
            onRightClick = navigateToEdit,
            onLeftClick = navigateUp
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
                    onRightSwipe = {
                        addWordToKnow(it)

                    },
                    onLeftSwipe = {
                        addWordToLearn(it)

                    }
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Left",
                            tint = lightLilaColor
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Right",
                            tint = lightLilaColor
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
                        style = AppTypography.titleSmall.copy(color = bgColor),
                        fontSize = TextUnit(22f, TextUnitType.Sp)
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
            Column(
                modifier = Modifier
                    .height(screenHeight * 0.4f)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        lightLilaColor,
                        shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp)
                    )
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Text(
                    stringResource(R.string.select_lesson_title),
                    style = AppTypography.displayLarge.copy(
                        fontSize = TextUnit(
                            22f,
                            TextUnitType.Sp
                        )
                    ),
                    color = bgColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )


                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn {
                        items(LessonType.values()) { lesson ->
                            CustomShadowButton(
                                text = lesson.name,
                                onClick = {
                                    showCourseSelection(false)
                                    startCourse(lesson)
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Column(modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 32.dp)) {
                        Text(
                            stringResource(R.string.close_btn),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable { showCourseSelection(false) },
                            color = bgColor,
                            style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )

                    }
                }
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
                    )
                )
            }
        }, bottomBar = {
            BottomNavigationBar(navController = NavController(LocalContext.current))
        })
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
                    state = SetUIState(
                        words = null
                    )
                )
            }
        }, bottomBar = {
            BottomNavigationBar(navController = NavController(LocalContext.current))
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
                    )
                )
            }
        }, bottomBar = {
            BottomNavigationBar(navController = NavController(LocalContext.current))
        })
    }
}