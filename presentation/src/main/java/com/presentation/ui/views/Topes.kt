package com.presentation.ui.views


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.presentation.R
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.whiteColor
import java.net.URL

private const val MAX_WORDS = 30

@Composable
fun HomeTopView(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 6)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            ),
    ) {
        Text(
            text = title,
            style = AppTypography.displayLarge,
            color = whiteColor,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(24.dp)
                .width(LocalConfiguration.current.screenWidthDp.dp / 2)
        )

//
//        Row(
//            modifier = Modifier
//                .padding(vertical = 16.dp)
//                .align(Alignment.CenterHorizontally)
//                .fillMaxWidth()
//        ) {

//            CircleProgress(
//                modifier = Modifier
//                    .size(donutSize)
//                    .padding(16.dp),
//                value = countOfWords,
//                maxValue = MAX_WORDS
//            )
//
//            Column(
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//            ) {
//                Text(
//                    "Выучено слов сегодня",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    fontSize = TextUnit(16f, TextUnitType.Sp)
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    "$countOfWords из $MAX_WORDS",
//                    color = MaterialTheme.colorScheme.onSurface,
//                    style = MaterialTheme.typography.headlineLarge,
//                )
//            }
        //  }
    }
}

@Composable
fun AccountTopView(
    name: String,
    photo: URL? = null,
    onEditClicked: () -> Unit = {},
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageSize = screenHeight * 0.12f

    Log.e("AccountTopView", "Photo = $photo")
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Профиль",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        )

        AsyncImage(
            modifier = Modifier
                .size(imageSize)
                .border(
                    width = 4.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = CircleShape
                )
                .clip(CircleShape), // Делаем изображение круглым
            placeholder = painterResource(id = R.drawable.ic_account),
            model = photo,
            contentDescription = "Sample Image",
            contentScale = ContentScale.Crop
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Имя: ",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onEditClicked() }
            )
        }
    }


}

@Composable
fun BaseTopView(
    title: String,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leftIcon != null) {
            Icon(
                imageVector = leftIcon,
                contentDescription = "LeftIcon",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onLeftClick() }
            )
        }

        Text(
            text = title,
            style = AppTypography.displayLarge,
            color = whiteColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        )

        if (rightIcon != null) {
            Icon(
                imageVector = rightIcon,
                contentDescription = "RightIcon",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onRightClick() }
            )
        } else if (leftIcon != null) {
            Icon(
                imageVector = leftIcon,
                contentDescription = "RightIcon",
                modifier = Modifier.padding(16.dp),
                tint = Color.Transparent
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
fun TopViewPreview() {
    AppTheme {
        Surface {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HomeTopView("Let's translate words")
                BaseTopView("Новые карточки")
                BaseTopView("Новые карточки", rightIcon = Icons.Default.LibraryAdd)
                BaseTopView(
                    "Набор карточек №1",
                    leftIcon = Icons.Default.ArrowBackIosNew,
                    rightIcon = Icons.Default.Edit
                )
                BaseTopView(
                    "Статистика",
                    leftIcon = Icons.Default.ArrowBackIosNew,
                )
                AccountTopView(name = "test@gmail.com")
            }
        }
    }
}
