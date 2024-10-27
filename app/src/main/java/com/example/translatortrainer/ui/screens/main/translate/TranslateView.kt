package com.example.translatortrainer.ui.screens.main.translate

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.ui.secondaryColor
import com.example.translatortrainer.ui.screens.main.translate.model.TranslatorState

@Composable
fun TranslateView(
    modifier: Modifier = Modifier,
    state: TranslatorState = TranslatorState(),
    onTextChange: (String) -> Unit = {}
) {
    var textField1 by remember { mutableStateOf(state.inputText) }
    val textField2 by remember { mutableStateOf(state.translatedText) }

    Column(
        modifier = Modifier.then(modifier)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = "Немецкий",
            color = Color.White,
            fontSize = TextUnit(18f, TextUnitType.Sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds(),
            value = textField1,
            onValueChange = {
                onTextChange(it)
                textField1 = it
            },
            label = { Text("") },
            colors = TextFieldDefaults.colors().copy(
                disabledContainerColor = secondaryColor,
                unfocusedContainerColor = secondaryColor,
                focusedContainerColor = secondaryColor,
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Русский",
            color = Color.White,
            fontSize = TextUnit(18f, TextUnitType.Sp)
        )

        val glowColor by remember { mutableStateOf(secondaryColor) } // Зеленый цвет свечения
        val animatedGlow by animateColorAsState(
            targetValue = if (textField2.isNotEmpty()) glowColor else Color.Transparent,
            animationSpec = tween(durationMillis = 500), label = "" // Длительность анимации 500 мс
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    clip = false,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = animatedGlow,
                    spotColor = animatedGlow
                ),
            value = textField2,
            onValueChange = {},
            readOnly = true,
            label = { Text("") },
            colors = TextFieldDefaults.colors().copy(
                disabledContainerColor = secondaryColor,
                unfocusedContainerColor = secondaryColor,
                focusedContainerColor = secondaryColor,
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

private val listOfStates =
    listOf(TranslatorState(), TranslatorState("Katze"), TranslatorState("Katze", "Кошка"))

private class PreviewProvider : PreviewParameterProvider<TranslatorState> {
    override val values: Sequence<TranslatorState>
        get() = listOfStates.asSequence()
}

@Preview(backgroundColor = 0xFF5633D1, showBackground = true)
@Composable
fun TranslateViewPreview(@PreviewParameter(PreviewProvider::class) state: TranslatorState) {
    Box(modifier = Modifier.padding(vertical = 16.dp)) {
        TranslateView(state = state)
    }
}