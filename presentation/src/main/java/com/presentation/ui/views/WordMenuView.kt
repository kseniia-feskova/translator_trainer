package com.presentation.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.presentation.model.WordUI
import com.presentation.ui.redDarkColor
import com.presentation.utils.toPx


@Composable
fun WordMenuView(
    popupOffset: Offset?,
    selectedItem: WordUI?,
    onDismissRequest: () -> Unit = {},
    onEdit: (WordUI) -> Unit = {},
    onDelete: (WordUI) -> Unit = {}
) {
    if (popupOffset != null) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(
                popupOffset.x.toInt(),
                (popupOffset.y - 30.dp.toPx()).toInt()
            ),
            onDismissRequest = { onDismissRequest() },
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Изменить",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItem?.let(onEdit)
                                onDismissRequest()
                            }
                            .padding(vertical = 12.dp)
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.tertiary)
                    Text(
                        text = "Удалить",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedItem?.let(onDelete)
                                onDismissRequest()
                            }
                            .padding(vertical = 12.dp),
                        color = redDarkColor
                    )
                }
            }
        }
    }
}