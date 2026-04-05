package com.translator.app.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.shared.R
import com.translator.app.ui.viewLightColor

@Composable
fun OrView() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = viewLightColor)
        )
        Spacer(Modifier.width(8.dp))
        Text(stringResource(R.string.or_view))
        Spacer(Modifier.width(8.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(2.dp)
                .background(color = viewLightColor)
        )
    }
}