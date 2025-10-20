package presentation.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.translatortrainer.shared.R
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.onPrimaryColorLight
val merriweatherLight = Font(R.font.merriweater_light, FontWeight.Light)

val MerriweatherFont = FontFamily(
    fonts = listOf(merriweatherLight)
)
val NunitoFont = FontFamily(
    fonts = listOf(
        Font(R.font.nunito_bold, FontWeight.Bold),
        Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
        Font(R.font.nunito_regular, FontWeight.Normal),
        Font(R.font.nunito_light, FontWeight.Light),
        Font(R.font.nunito_extra_light, FontWeight.ExtraLight),
        Font(R.font.nunito_black, FontWeight.Black)
    )
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = MerriweatherFont,
        fontSize = 40.sp,
        color = darkColor
    ),
    displayMedium = TextStyle(
        fontFamily = MerriweatherFont,
        fontSize = 24.sp,
        color = darkColor
    ),
    displaySmall = TextStyle(
        fontFamily = MerriweatherFont,
        fontSize = 16.sp,
        color = darkColor
    ),
    titleSmall = TextStyle(
        fontFamily = MerriweatherFont,
        fontSize = 14.sp,
        color = fieldBorderColor
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = onPrimaryColorLight
    ),
    titleMedium = TextStyle(
        fontFamily = NunitoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = onPrimaryColorLight
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = onPrimaryColorLight

    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = onPrimaryColorLight

    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = onPrimaryColorLight
    )
)