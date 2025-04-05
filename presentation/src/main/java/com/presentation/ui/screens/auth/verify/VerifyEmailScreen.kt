package com.presentation.ui.screens.auth.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.presentation.ui.AppTheme
import com.presentation.ui.AppTypography
import com.presentation.ui.bgColor
import com.presentation.ui.fieldColors
import com.presentation.ui.redDarkColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.BackgroundDecorAnimated
import com.presentation.ui.views.CountdownTimer
import com.presentation.ui.views.Loader
import com.presentation.ui.whiteColor

@Composable
fun VerifyEmailScreen(
    loading: Boolean = false,
    timerVisible: Boolean = true,
    enteredCode: String = "",
    error: VerifyError? = null,
    onCodeEntering: (String) -> Unit = {},
    verifyCode: () -> Unit = {},
    onTimeout: () -> Unit = {},
    resendCode: () -> Unit = {},
    back: () -> Unit = {}
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    )
    {
        BackgroundDecorAnimated()
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .basicMarquee(
                    repeatDelayMillis = 1,
                    velocity = 30.dp,
                    iterations = 0
                )
                .padding(horizontal = 18.dp, vertical = 24.dp)
        ) {
            Text(text = "Verify your email", color = whiteColor, style = AppTypography.displayLarge)
        }
        Spacer(Modifier.height(24.dp))

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(screenHeight * 0.6f)
            ) {
                Loader(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(80.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(screenHeight * 0.6f)
                .background(
                    whiteColor,
                    shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                "We sent the verification code to your email",
                style = AppTypography.displayLarge.copy(fontSize = TextUnit(22f, TextUnitType.Sp)),
                color = bgColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = enteredCode,
                onValueChange = { onCodeEntering(it) },
                singleLine = true,
                label = {
                    Text(
                        "Verification code",
                        style = AppTypography.titleSmall,
                    )
                },
                maxLines = 1,
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
                textStyle = AppTypography.titleSmall,
                isError = error == VerifyError.WRONG_CODE,
            )


            if (error != null) {
                Text(
                    text = stringResource(error.msg),
                    style = AppTypography.titleSmall.copy(color = redDarkColor),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (timerVisible) {
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        "You can resend the code after: ",
                        color = bgColor,
                        style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    CountdownTimer { onTimeout() }
                }
            } else {
                Text(
                    "Resend code",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { resendCode() },
                    color = bgColor,
                    style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Box(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    Text(
                        "Back",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { back() },
                        color = bgColor,
                        style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    CustomShadowButton(
                        "Continue",
                        onClick = verifyCode,
                        isEnabled = enteredCode.isNotEmpty()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun VerifyEmailPreview() {
    AppTheme {
        Surface {
            VerifyEmailScreen()
        }
    }
}