package com.example.translatortrainer.ui.screens.set

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.translatortrainer.ui.greenColor
import com.example.translatortrainer.ui.primaryColor
import com.example.translatortrainer.ui.secondaryColor

@Composable
fun TopViewCardSet(knowWords: Int = 10, allWords: Int = 27) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = secondaryColor,
                shape = RoundedCornerShape(
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ),
    ) {
        Spacer(modifier = Modifier.padding(6.dp))
        TopViewCard()
        Text(
            "лёгкий", modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = TextUnit(14f, TextUnitType.Sp),
            fontWeight = FontWeight.SemiBold,
            color = greenColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Вы знаете $knowWords из $allWords слов ",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            color = primaryColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProgressForSet(
            modifier = Modifier
                .padding(horizontal = 20.dp), knowWords,
            allWords
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TopViewCard(modifier: Modifier = Modifier, name: String = "Набор слов №1") {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Menu,
            contentDescription = "Menu",
            Modifier
                .background(primaryColor, shape = CircleShape)
                .align(Alignment.CenterVertically)
                .padding(8.dp)
                .clickable { },
            tint = secondaryColor
        )
        Text(
            text = name,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(22f, TextUnitType.Sp),
            color = primaryColor,
        )

        Icon(
            Icons.Default.Edit,
            contentDescription = "Edit",
            Modifier
                .align(Alignment.Top)
                .padding(8.dp)
                .clickable { },
            tint = primaryColor
        )
    }
}