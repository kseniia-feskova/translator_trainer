package com.example.translatortrainer.ui.core

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.ui.accentColor50
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.ui.secondaryColor

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable() (RowScope.() -> Unit)
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier.then(modifier)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 24.dp),
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