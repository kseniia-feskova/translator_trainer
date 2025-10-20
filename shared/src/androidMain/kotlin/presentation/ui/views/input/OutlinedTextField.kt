package presentation.ui.views.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.translatortrainer.shared.R
import com.presentation.ui.darkColor
import com.presentation.ui.fieldColors
import presentation.ui.AppTypography

@Composable
fun OutlinedTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isEnabled: Boolean = true
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(
                text = label,//stringResource(R.string.email_label),
                style = AppTypography.titleSmall,
            )
        },
        onValueChange = onValueChange,
        isError = isError,//state.error == AuthError.EMPTY_FIELDS && state.email.isEmpty()
        enabled = isEnabled,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = fieldColors(),
        textStyle = AppTypography.titleSmall.copy(fontSize = 16.sp),
    )
}

@Composable
fun OutlinedTextFieldWithVisibility(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    isEnabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        enabled = isEnabled,
        label = {
            Text(
                text = label,
                style = AppTypography.titleSmall,
            )
        },
        singleLine = true,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                painter = painterResource(if (passwordVisible) R.drawable.ic_visible_pass else (R.drawable.ic_hide_pass)),
                contentDescription = label,
                tint = darkColor
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = fieldColors(),
        textStyle = AppTypography.titleSmall.copy(fontSize = 16.sp),
        //(state.error == AuthError.EMPTY_FIELDS && state.password.isEmpty()) || state.error == AuthError.WRONG_PASSWORD,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
    )
}