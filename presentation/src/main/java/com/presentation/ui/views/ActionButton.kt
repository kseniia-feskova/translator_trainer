package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.presentation.ui.accentColor50
import com.presentation.ui.primaryColor
import com.presentation.ui.secondaryColor

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.then(modifier)
            .fillMaxWidth(),
        enabled = true,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = secondaryColor,
            disabledContainerColor = accentColor50,
            contentColor = primaryColor
        ),
        shape = RoundedCornerShape(12.dp),
        content = content
    )
}

@Composable
fun SecondButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        enabled = true,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = accentColor50,
            disabledContainerColor = accentColor50,
            contentColor = secondaryColor
        ),
        shape = RoundedCornerShape(12.dp),
        content = content
    )
}

@Composable
@Preview
fun ButtonsPreview() {
    Column (
        modifier = Modifier.background(primaryColor),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(onClick = { }, modifier = Modifier.padding(16.dp)) {
            Text("Action")

        }

        SecondButton(onClick = { }, modifier = Modifier.padding(16.dp)) {
            Text("Second")

        }
    }
}