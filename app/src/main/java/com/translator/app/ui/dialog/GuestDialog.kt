package com.translator.app.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.translator.app.ui.AppTheme
import com.translator.app.ui.AppTypography
import com.translator.app.ui.darkColor
import com.translator.app.ui.whiteColor
import com.translator.app.ui.views.buttons.CustomShadowButton

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
                style = AppTypography.displayMedium.copy(color = darkColor)
            )
        },
        text = {
            Text(
                "Вы заходите как гость, а значит функционал будет ограничен.\nПо желанию, Вы сможете создать аккаунт и сберечь все данные.\nПриятного пользования.",
                style = AppTypography.titleLarge.copy(
                    color = darkColor,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
            )
        },
        confirmButton = {
            CustomShadowButton(
                text = "Войти",
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            )
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
