package com.translator.app.ui.views

import android.annotation.SuppressLint
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.zIndex
import com.translator.app.ui.darkColor
import com.translator.app.ui.whiteColor
import com.translator.app.ui.yellowColor

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Circles(modifier: Modifier, isRegister: Boolean) {
    val transition = updateTransition(targetState = isRegister, label = "CirclesTransition")
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val circleSize = screenWidth.value / 1.5f
    val center = 0f//screenWidth.value/2
    val circleOffset = circleSize * 1.25f
    val offset1 by transition.animateOffset(
        label = "lightLilaColor",
        transitionSpec = { tween(durationMillis = 1000, easing = EaseInOutQuad) }) { state ->
        if (state) Offset(center + circleOffset, 0f) else Offset(
            center - circleOffset,
            0f
        )  // 1 → 3
    }
    val offset2 by transition.animateOffset(
        label = "whiteColor",
        transitionSpec = { tween(durationMillis = 1000, easing = EaseInOutQuad) }) { state ->
        if (state) Offset(center, 0f) else Offset(center + circleOffset, 0f)   // 2 → 1
    }
    val offset3 by transition.animateOffset(
        label = "darkColor",
        transitionSpec = { tween(durationMillis = 1000, easing = EaseInOutQuad) }) { state ->
        if (state) Offset(center - circleOffset, 0f) else Offset(
            center,
            0f
        ) // 3 → 2 + поднятие вверх
    }
    Box(
        modifier = modifier
            .blur(12.dp)
            .offset { if (!isRegister) (-offset1 / 2f).round() else (-offset3 / 2f).round() }
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .padding(16.dp)
                .offset { offset1.round() }
                .background(darkColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .padding(16.dp)
                .offset { offset2.round() }
                .zIndex(if (isRegister) 1f else 0f)
                .background(whiteColor, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(circleSize.dp)
                .padding(16.dp)
                .offset { offset3.round() }
                .background(yellowColor, shape = CircleShape)
        )
    }
}