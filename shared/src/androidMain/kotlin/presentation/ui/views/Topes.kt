package presentation.ui.views


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.whiteColor
import java.net.URL

@Composable
fun HomeTopView(
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp, top = 10.dp)
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
    onEditClicked: (() -> Unit)? = {},
) {

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageSize = screenHeight * 0.12f

    Log.e("AccountTopView", "Photo = $photo")
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                color = bgColor,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(bottom = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.account_title),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        AsyncImage(
            modifier = Modifier
                .size(imageSize)
                .border(
                    width = 4.dp,
                    color = whiteColor,
                    shape = CircleShape
                )
                .clip(CircleShape), // Делаем изображение круглым
            placeholder = painterResource(id = R.drawable.ic_account),
            error = painterResource(id = R.drawable.ic_account),
            model = photo,
            contentDescription = "Sample Image",
            contentScale = ContentScale.Crop
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (onEditClicked != null) {
                Text(
                    text = stringResource(R.string.name_label),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        color = whiteColor
                    )
                )
            }
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = whiteColor,
                    fontSize = TextUnit(20f, TextUnitType.Sp)
                )
            )
            if (onEditClicked != null) {
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
                BaseTopView("Новые карточки", rightIcon = Icons.Default.Add)
                BaseTopView(
                    "Набор карточек №1",
                    leftIcon = Icons.Default.ArrowBack,
                    rightIcon = Icons.Default.Edit
                )
                BaseTopView(
                    "Статистика",
                    leftIcon = Icons.Default.ArrowBack,
                )
                AccountTopView(name = "test@gmail.com")
//
//                LessonTopView(3)
//                LessonTopView(2)
//                LessonTopView(1)
            }
        }
    }
}


@Composable
fun LessonTopView(
    lives: Int,
    onPauseClicked: () -> Unit = {}
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
        Row(modifier = Modifier.padding(16.dp)) {
            repeat(lives) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "LeftIcon",
                    tint = Color.Red,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        Text(
            text = "",
            style = AppTypography.displayLarge,
            color = whiteColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        )

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "RightIcon",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onPauseClicked() },
            tint = whiteColor
        )
    }
}