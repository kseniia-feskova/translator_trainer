package com.presentation.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.accentColor50
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier.then(modifier)
            .fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        content = content,
        border = if (enabled) BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary) else null
    )
}

@Composable
fun SecondButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    OutlinedButton(
        onClick = { onClick() },
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        enabled = true,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = accentColor50,
            disabledContainerColor = accentColor50,
            contentColor = secondaryColor,
        ),
        shape = RoundedCornerShape(12.dp),
        content = content,
        border = null
    )
}

@Composable
@Preview
fun ButtonsPreview() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier.background(primaryColor),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(onClick = { }, modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Action",
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onPrimary)
                    )

                }

                ActionButton(onClick = { }, enabled = false, modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Action",
                        style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.primary)
                    )

                }

                SecondButton(onClick = { }, modifier = Modifier.padding(16.dp)) {
                    Text("Second")

                }
            }
        }
    }
}