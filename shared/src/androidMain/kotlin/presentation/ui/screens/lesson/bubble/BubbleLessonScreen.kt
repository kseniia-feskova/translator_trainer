package presentation.ui.screens.lesson.bubble

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.lightLilaColor
import presentation.ui.views.LessonTopView
import com.presentation.ui.whiteColor
import com.presentation.utils.toPx
import presentation.model.WordUI
import presentation.test.smallList
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun WordUI.toBubbles(): List<BubbleData> {
    return listOf(
        BubbleData(id, originalText, bgColor = darkColor, textColor = Color.White),
        BubbleData(id, resText, bgColor = lightLilaColor, textColor = darkColor)
    )
}

data class BubbleData(
    val wordId: String,
    val word: String,
    val bgColor: Color,
    val textColor: Color
)

@SuppressLint("ConfigurationScreenWidthHeight")
@Preview
@Composable
fun BubbleScreenPreview() {
    AppTheme {
        Scaffold {
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp.toPx()
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp.toPx()
            Box(modifier = Modifier.padding(it)) {
                val list = mutableListOf<BubbleData>()
                list.addAll(smallList.first().toBubbles())
                list.addAll(smallList[1].toBubbles())
                list.addAll(smallList[2].toBubbles())
                val bubbles = list.map {
                    getBubble(
                        screenWidthPx = screenWidth,
                        screenHeightPx = screenHeight,
                        sizeKoef = (it.word.length + 5) * 20f,
                        color = it.bgColor,
                        word = it.word,
                        wordId = it.wordId,
                        textColor = it.textColor
                    )
                }.shuffled()
                BubbleLessonScreen(bubbles, isLessonCompleted = false, false, false, 3)
            }
        }
    }
}

@Preview
@Composable
fun OnPausePreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(paddingValues = it)) {
                OnPauseScreen()
            }
        }
    }
}


@Preview
@Composable
fun OnFailPreview() {
    AppTheme {
        Scaffold {
            Box(modifier = Modifier.padding(paddingValues = it)) {
                OnFailScreen()
            }
        }
    }
}

@Composable
fun BubbleLessonScreen(
    mBubbles: List<BubbleWitText>,
    isLessonCompleted: Boolean,
    isLessonFailed: Boolean,
    isPaused: Boolean,
    lives: Int,
    initializeBubbles: (Float, Float) -> Unit = { _, _ -> },
    onBubbleClick: (String) -> Unit = {},
    onPauseClick: () -> Unit = {},
    reload: () -> Unit = {},
    navigateUp: () -> Unit = {},
    navigateToSuccess: () -> Unit = { }

) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp.toPx()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp.toPx()

    var emptyBubbles by remember {
        mutableStateOf(
            getEmptyBubbles(
                screenWidthPx = screenWidth,
                screenHeightPx = screenHeight,
                count = 15
            )
        )
    }

    // Локальная копия пузырьков для анимации
    var animatedBubbles by remember { mutableStateOf(emptyList<BubbleWitText>()) }
    var isPausedAnimation by remember { mutableStateOf(isPaused) }

    // Обновляем копию, если mBubbles изменились
    LaunchedEffect(mBubbles) {
        Log.e(
            "BubbleLessonScreen",
            "mBubbles = ${mBubbles.size}, animatedBubbles = ${animatedBubbles.size}"
        )
        if (animatedBubbles.isEmpty()) {
            animatedBubbles = mBubbles
        } else {
            animatedBubbles.forEach { animatedBubble ->
                val updated =
                    mBubbles.find { it.wordId == animatedBubble.wordId && it.word == animatedBubble.word }
                if (updated != null) {
                    animatedBubble.isSelected = updated.isSelected
                    animatedBubble.isWrong = updated.isWrong
                    animatedBubble.isVisible = updated.isVisible
                }
            }
        }
    }

    LaunchedEffect(isPaused) {
        isPausedAnimation = isPaused
    }
    var lastFrameTimeNanos = 0L

    LaunchedEffect(Unit) {
        initializeBubbles(screenWidth, screenHeight)
    }

    LaunchedEffect(Unit) {
        while (!isPausedAnimation) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTimeNanos == 0L) {
                    lastFrameTimeNanos = frameTimeNanos
                    return@withFrameNanos
                }

                val deltaTime = (frameTimeNanos - lastFrameTimeNanos) / 1_000_000_000f
                lastFrameTimeNanos = frameTimeNanos
                animatedBubbles = animatedBubbles.mapNotNull { point ->
                    if (!point.isSelected) {
                        point.updatePosition(
                            screenWidth,
                            screenHeight,
                            deltaTime,
                            bounced = true
                        )
                    } else point
                }

                emptyBubbles = emptyBubbles.mapNotNull { point ->
                    point.updatePosition(screenWidth, screenHeight, deltaTime, bounced = true)
                }
            }
        }
    }


    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    animatedBubbles.find { it.contains(tapOffset) }?.let {
                        if (it.isVisible) {
                            onBubbleClick(it.id)
                        }
                    }
                }
            }
    ) {
        if (isLessonCompleted) {
            navigateToSuccess()
        }
        BubbleCanvas(animatedBubbles, emptyBubbles)
        LessonTopView(lives) {
            onPauseClick()
        }
        if (isPaused) {
            OnPauseScreen(onContinue = {
                onPauseClick()
            }, navigateUp)
        }

        if (isLessonFailed) {
            OnFailScreen(tryAgain = {
                animatedBubbles = emptyList()
                emptyBubbles = getEmptyBubbles(
                    screenWidthPx = screenWidth,
                    screenHeightPx = screenHeight,
                    count = 15
                )
                reload()

            }, onCloseLesson = navigateUp)
        }
    }
}

@Composable
fun OnPauseScreen(
    onContinue: () -> Unit = {},
    onCloseLesson: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = "Pause",
                tint = whiteColor,
                modifier = Modifier.size(64.dp)
            )

            Text("Paused", style = AppTypography.displayLarge)

            Spacer(Modifier.height(48.dp))

            Text(
                "Continue",
                style = AppTypography.titleLarge.copy(color = whiteColor),
                modifier = Modifier.clickable {
                    onContinue()
                })
            Spacer(Modifier.height(18.dp))
            Text(
                "End lesson",
                style = AppTypography.titleLarge.copy(color = darkColor),
                modifier = Modifier.clickable {
                    onCloseLesson()
                })
        }
    }
}

@Composable
fun OnFailScreen(
    tryAgain: () -> Unit = {},
    onCloseLesson: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Fail",
                tint = whiteColor,
                modifier = Modifier.size(64.dp)
            )

            Text("You failed", style = AppTypography.displayLarge)

            Spacer(Modifier.height(48.dp))

            Text(
                "Try again",
                style = AppTypography.titleLarge.copy(color = whiteColor),
                modifier = Modifier.clickable {
                    tryAgain()
                })
            Spacer(Modifier.height(18.dp))
            Text(
                "Main menu",
                style = AppTypography.titleLarge.copy(color = darkColor),
                modifier = Modifier.clickable {
                    onCloseLesson()
                })
        }
    }
}

@Composable
fun BubbleCanvas(bubbles: List<BubbleWitText>, emptyBubbles: List<Bubble>) {
    //.e("BubbleCanvas", "bubbles = $bubbles")
    Canvas(Modifier.fillMaxSize()) {
        emptyBubbles.forEach { bubble ->
            drawCircle(
                color = bubble.color,
                radius = bubble.size / 2f,
                center = Offset(bubble.x, bubble.y),
            )
        }
        bubbles.forEach { bubble ->
            if (bubble.isVisible) {
                if (bubble.isWrong == true) {
                    drawCircle(
                        color = Color.Red.copy(alpha = 0.5f),
                        radius = bubble.size / 2f + 12f,
                        center = Offset(bubble.x, bubble.y),
                    )
                } else if (bubble.isWrong == false) {
                    drawCircle(
                        color = Color.Green.copy(alpha = 0.5f),
                        radius = bubble.size / 2f + 12f,
                        center = Offset(bubble.x, bubble.y),
                    )
                }
                if (bubble.isSelected) {
                    drawCircle(
                        color = Color.Blue.copy(alpha = 0.6f),
                        radius = bubble.size / 2f + 8f,
                        center = Offset(bubble.x, bubble.y),
                    )
                }
                drawCircle(
                    color = bubble.color,
                    radius = bubble.size / 2f,
                    center = Offset(bubble.x, bubble.y),
                )

                drawIntoCanvas { canvas ->
                    val paint = android.graphics.Paint().apply {
                        color = bubble.textColor.toArgb()
                        textSize = bubble.textSize
                        textAlign = android.graphics.Paint.Align.CENTER
                        typeface = Typeface.DEFAULT
                    }
                    val textY = bubble.y + bubble.textSize / 3
                    canvas.nativeCanvas.drawText(bubble.word, bubble.x, textY, paint)
                }
            }
        }
    }
}


fun getBubble(
    screenWidthPx: Float,
    screenHeightPx: Float,
    sizeKoef: Float,
    color: Color,
    word: String,
    textColor: Color,
    wordId: String
): BubbleWitText {
    return BubbleWitText(
        id = wordId + word,
        x = screenWidthPx / 2,
        y = screenHeightPx / 2,
        speed = (60..300).random().toFloat(),
        size = sizeKoef,
        color = color,
        word = word,
        wordId = wordId,
        angle = (0..360).random().toFloat(),
        textColor = textColor
    )
}

fun getEmptyBubbles(
    count: Int,
    screenWidthPx: Float,
    screenHeightPx: Float,
): List<Bubble> {
    val list = mutableListOf<Bubble>()
    val colors = listOf(lightLilaColor, bgColor, Color.White)
    while (list.size < count) {
        list.add(
            Bubble(
                x = screenWidthPx / 2,
                y = screenHeightPx / 2,
                //x = (0..(screenWidthPx).roundToInt()).random().toFloat(),
//                y = (0..(screenHeightPx).roundToInt()).random()
//                    .toFloat(),
                speed = (60..300).random().toFloat(),
                size = ((screenWidthPx * 0.08f).roundToInt()..(screenWidthPx * 0.15f).roundToInt()).random()
                    .toFloat(),
                color = colors[(0..2).random()],
                angle = (0..360).random().toFloat()
            )
        )
    }
    return list.toList()
}


data class BubbleWitText(
    val id: String,
    val x: Float,
    val y: Float,
    val size: Float, // Радиус пузыря
    val word: String,
    val speed: Float = 3f,
    val angle: Float, // Угол движения в градусах
    var isSelected: Boolean = false,
    var isWrong: Boolean? = null,
    var isVisible: Boolean = true,
    val wordId: String,
    val color: Color = Color.Blue,
    val textColor: Color
) {
    val textSize: Float
        get() = 40f //  size / 4f // Размер текста зависит от размера пузыря

    fun updatePosition(
        screenWidth: Float,
        screenHeight: Float,
        deltaTime: Float,
        bounced: Boolean = true
    ): BubbleWitText? {
        val angleInRadians = Math.toRadians(angle.toDouble())

        // Вычисляем смещение по x и y
        val dx = (speed * deltaTime * cos(angleInRadians)).toFloat()
        val dy = (speed * deltaTime * sin(angleInRadians)).toFloat()

        // Обновляем координаты
        var newX = x + dx
        var newY = y + dy

        var newAngle = angle

        // Обработка столкновений с границами экрана (отражение)
        if (newX <= 0 || newX >= screenWidth) {
            if (bounced) {
                newX = newX.coerceIn(0f, screenWidth)
                newAngle = 180 - newAngle
            } else return null
        }
        if (newY <= 0 || newY >= screenHeight) {
            if (bounced) {
                newY = newY.coerceIn(0f, screenHeight)
                newAngle = -newAngle
            } else return null
        }

        // Возвращаем новый объект с обновленными значениями
        return copy(x = newX, y = newY, angle = (newAngle + 360) % 360)
    }

    fun contains(point: Offset): Boolean {
        val dx = point.x - x
        val dy = point.y - y
        return dx * dx + dy * dy <= (size / 2) * (size / 2)
    }
}

data class Bubble(
    val x: Float,
    val y: Float,
    val size: Float, // Радиус пузыря
    val speed: Float = 3f,
    val angle: Float, // Угол движения в градусах
    val color: Color = Color.Blue
) {
    fun updatePosition(
        screenWidth: Float,
        screenHeight: Float,
        deltaTime: Float,
        bounced: Boolean = true
    ): Bubble? {
        val angleInRadians = Math.toRadians(angle.toDouble())

        // Вычисляем смещение по x и y
        val dx = (speed * deltaTime * cos(angleInRadians)).toFloat()
        val dy = (speed * deltaTime * sin(angleInRadians)).toFloat()

        // Обновляем координаты
        var newX = x + dx
        var newY = y + dy

        var newAngle = angle

        // Обработка столкновений с границами экрана (отражение)
        if (newX <= 0 || newX >= screenWidth) {
            if (bounced) {
                newX = newX.coerceIn(0f, screenWidth)
                newAngle = 180 - newAngle
            } else return null
        }
        if (newY <= 0 || newY >= screenHeight) {
            if (bounced) {
                newY = newY.coerceIn(0f, screenHeight)
                newAngle = -newAngle
            } else return null
        }

        // Возвращаем новый объект с обновленными значениями
        return copy(x = newX, y = newY, angle = (newAngle + 360) % 360)
    }

}
