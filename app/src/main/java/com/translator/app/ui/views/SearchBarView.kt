package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.translator.app.ui.AppTheme
import com.translator.app.ui.fieldColors
import com.translator.app.ui.AppTypography

@Composable
fun SearchBarView(
    modifier: Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onClearClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent),
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = AppTypography.titleMedium,
            shape = RoundedCornerShape(32.dp),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon",
                        modifier = Modifier
                            .clickable { onClearClick() }
                            .padding(end = 8.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Filter Icon",
                        modifier = Modifier
                            .clickable { onFilterClick() }
                            .padding(end = 8.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = fieldColors()
        )
    }
}

@Preview
@Composable
fun SearchBarViewPreview() {
    AppTheme {
        SearchBarView(
            Modifier.padding(16.dp),
            query = "Wort",
        )
    }
}