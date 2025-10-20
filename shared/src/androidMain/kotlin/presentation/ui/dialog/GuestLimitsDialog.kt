package presentation.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import presentation.ui.AppTypography
import com.presentation.ui.bgColor
import presentation.ui.views.buttons.CustomShadowButton
import com.presentation.ui.secondaryColor

@Composable
fun GuestLimitsDialog(
    modifier: Modifier = Modifier,
    toAccountSetting: () -> Unit = {},
    dismissDialog: () -> Unit = {}
) {
    AlertDialog(
        modifier = modifier,
        containerColor = secondaryColor,
        onDismissRequest = {
            dismissDialog()
        },
        confirmButton = {
            CustomShadowButton(
                text = stringResource(R.string.move_to_account), onClick = toAccountSetting
            )
        },
        dismissButton = {
            Text(
                stringResource(R.string.cancel_btn),
                modifier = Modifier.clickable { dismissDialog() }.fillMaxWidth().padding(top = 16.dp),
                textAlign = TextAlign.Center,
                color = bgColor,
                style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        },
        title = {
            Text(
                stringResource(R.string.guest_limits_title),
                style = MaterialTheme.typography.displayLarge.copy(
                    color = bgColor,
                    fontSize = TextUnit(22f, TextUnitType.Sp)
                )
            )
        },
        text = {
            Text(
                stringResource(R.string.guest_limits_description),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = bgColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            )
        },
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