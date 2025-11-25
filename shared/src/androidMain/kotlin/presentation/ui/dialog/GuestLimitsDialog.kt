package presentation.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.whiteColor
import presentation.ui.AppTypography
import presentation.ui.views.buttons.CustomShadowButton

@Composable
fun GuestLimitsDialog(
    modifier: Modifier = Modifier,
    toAccountSetting: () -> Unit = {},
    dismissDialog: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        containerColor = whiteColor,
        onDismissRequest = dismissDialog,
        title = {
            Text(
                stringResource(R.string.guest_limits_title),
                style = AppTypography.displayMedium.copy(color = darkColor)
            )
        },
        text = {
            Text(
                stringResource(R.string.guest_limits_description),
                style = AppTypography.titleLarge.copy(
                    color = darkColor,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
            )
        },
        confirmButton = {
            CustomShadowButton(
                text = stringResource(R.string.move_to_account),
                onClick = toAccountSetting,
                modifier = Modifier.fillMaxWidth()
            )
        },
        dismissButton = {
            Text(
                stringResource(R.string.cancel_btn),
                modifier = Modifier.clickable { dismissDialog() }.fillMaxWidth().padding(top = 16.dp),
                textAlign = TextAlign.Center,
                color = darkColor,
                style = AppTypography.displaySmall
            )
        }
    )
}

@Preview
@Composable
fun GuestLimitsDialogPreview() {
    AppTheme {
        Surface {
            GuestLimitsDialog()
        }
    }
}