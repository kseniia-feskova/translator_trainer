package com.presentation.ui.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.whiteColor

@Composable
fun GuestModeDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = whiteColor,
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Гостевой режим!",
                style = AppTypography.displayLarge.copy(color = bgColor)
            )
        },
        text = {
            Text(
                "Вы заходите как гость, а значит функционал будет ограничен.\nПо желанию, Вы сможете создать аккаунт и сберечь все данные.\nПриятного пользования.",
                style = AppTypography.titleLarge.copy(
                    color = bgColor,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
            )
        },
        confirmButton = {
            CustomShadowButton("Войти", onClick = onConfirm, modifier = Modifier.fillMaxWidth())
        }
    )
}

@Preview
@Composable
fun GuestModePreview(

) {
    AppTheme {
        GuestModeDialog({}) { }
    }
}
