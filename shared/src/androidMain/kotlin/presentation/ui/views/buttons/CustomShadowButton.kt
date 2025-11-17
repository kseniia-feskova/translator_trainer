package presentation.ui.views.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.whiteColor
import com.presentation.ui.yellowColor
import presentation.ui.AppTypography

@Composable
fun CustomShadowButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true,
) {
    Box(modifier = modifier) {
        if (isEnabled) {
            Spacer(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .height(48.dp)
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(30.dp),
                        color = fieldBorderColor
                    )
            )
        }
        Button(
            onClick = { onClick() },
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(if (isEnabled) yellowColor else whiteColor)
                .border(
                    1.dp,
                    color = fieldBorderColor,
                    shape = RoundedCornerShape(30.dp)
                ),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = yellowColor,
                disabledContainerColor = whiteColor
            )
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = icon,
                    contentDescription = "Icon",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                modifier = Modifier.padding(bottom = 2.dp),
                text = text,
                color = darkColor,
                style = AppTypography.titleSmall
            )
        }
    }
}

@Preview
@Composable
fun CustomShadowButtonPreview(
) {
    Surface() {
        Column {
            CustomShadowButton(text = "Test button")
            Spacer(modifier = Modifier.height(12.dp))
            CustomShadowButton(isEnabled = false, text = "Test button")
        }
    }
}