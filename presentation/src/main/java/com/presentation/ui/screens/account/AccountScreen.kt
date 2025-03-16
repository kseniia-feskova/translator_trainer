package com.presentation.ui.screens.account

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.R
import com.presentation.navigation.BottomNavigationBar
import com.presentation.test.dummyCourses
import com.presentation.ui.AppTheme
import com.presentation.ui.bgColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.screens.auth.CustomShadowButton
import com.presentation.ui.views.AccountTopView

@Composable
fun AccountScreen(
    state: AccountUIState,
    onEditClicked: () -> Unit = {},
    addLanguage: () -> Unit = {},
    changeTheme: () -> Unit = {},
    logout: () -> Unit = {},
    dismissDialog: () -> Unit = {},
    deleteAccount: () -> Unit = {},
    createAccount: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountTopView(
                name = if (state.guestData == null) state.name.toString() else "Guest",
                photo = state.image,
                onEditClicked = if (state.guestData == null) onEditClicked else null
            )

            if (state.guestData != null) {
                Spacer(modifier = Modifier.height(16.dp))
                GuestDataView(
                    modifier = Modifier
                        .padding(horizontal = 24.dp), state.guestData
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (state.guestData == null) {
                CustomShadowButton(
                    text = stringResource(R.string.add_course_btn), onClick = { addLanguage() },
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                )
            } else {
                CustomShadowButton(
                    text = stringResource(R.string.create_account_button),
                    onClick = { createAccount() },
                    modifier = Modifier
                        .padding(horizontal = 24.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomShadowButton(
                text = stringResource(R.string.change_theme_btn), onClick = { changeTheme() },
                modifier = Modifier
                    .padding(horizontal = 24.dp),
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.clickable { logout() },
                text = stringResource(R.string.logout_title),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = bgColor,
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold
                )
            )

            if (state.guestData == null) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier.clickable { deleteAccount() },
                    text = stringResource(R.string.delete_account_btn),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = redDarkColor,
                        fontSize = TextUnit(18f, TextUnitType.Sp),
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }

        if (state.showDeleteDialog) {
            AlertDialog(modifier = Modifier.align(Alignment.Center),
                containerColor = lightLilaColor.copy(alpha = 0.9f),
                onDismissRequest = {
                    dismissDialog()
                },
                confirmButton = {
                    Text(
                        text = stringResource(R.string.agree_btn),
                        Modifier.clickable { deleteAccount() },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = redDarkColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                },
                dismissButton = {
                    Text(
                        text = stringResource(R.string.cancel_btn),
                        Modifier.clickable { dismissDialog() },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = bgColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                },
                title = {
                    Text(
                        stringResource(R.string.delete_account_title),
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = redDarkColor,
                            fontSize = TextUnit(22f, TextUnitType.Sp)
                        )
                    )
                },
                text = {
                    Text(
                        stringResource(R.string.delete_account_subtitle),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = bgColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        tint = redDarkColor,
                        contentDescription = "Delete"
                    )
                }
            )
        }

        if (state.showLogoutDialog) {
            AlertDialog(modifier = Modifier.align(Alignment.Center),
                containerColor = lightLilaColor.copy(alpha = 0.9f),
                onDismissRequest = {
                    dismissDialog()
                },
                confirmButton = {
                    Text(
                        text = stringResource(R.string.agree_btn),
                        Modifier.clickable { logout() },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = redDarkColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                },
                dismissButton = {
                    Text(
                        text = stringResource(R.string.cancel_btn),
                        Modifier.clickable { dismissDialog() },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = bgColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                },
                title = {
                    Text(
                        stringResource(R.string.logout_title),
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = bgColor,
                            fontSize = TextUnit(22f, TextUnitType.Sp)
                        )
                    )
                },
                text = {
                    Text(
                        stringResource(R.string.logout_subtitle),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = bgColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(16f, TextUnitType.Sp)
                        )
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        tint = bgColor,
                        contentDescription = "Logout"
                    )
                }
            )
        }
    }
}

@Composable
fun GuestDataView(modifier: Modifier = Modifier, guestData: GuestData) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Text("Limits", style = MaterialTheme.typography.titleMedium.copy(color = bgColor))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Current course:", style = MaterialTheme.typography.titleSmall.copy(color = bgColor)
            )
            Icon(
                modifier = Modifier
                    .height(24.dp)
                    .border(
                        width = 1.dp,
                        color = fieldBorderColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp)),
                painter = painterResource(guestData.course.originalFlag),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Icon(
                modifier = Modifier
                    .height(24.dp)
                    .border(
                        width = 1.dp,
                        color = fieldBorderColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp)),
                painter = painterResource(guestData.course.translatedFlag),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Count of saved words:",
                style = MaterialTheme.typography.titleSmall.copy(color = bgColor)
            )
            Text(
                "${guestData.allWordsCount}/100",
                style = MaterialTheme.typography.titleSmall.copy(color = bgColor)
            )
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Count of words sets:",
                style = MaterialTheme.typography.titleSmall.copy(color = bgColor)
            )
            Text(
                "${guestData.allSetsCount}/3",
                style = MaterialTheme.typography.titleSmall.copy(color = bgColor)
            )
        }
    }
}


@Composable
@Preview
fun AccountScreenPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                AccountScreen(AccountUIState("KseniiaFeskova@Gmail.com", null, false))
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}

@Composable
@Preview
fun AccountScreenGuestPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                AccountScreen(
                    AccountUIState(
                        "KseniiaFeskova@Gmail.com",
                        null,
                        false,
                        guestData = GuestData(dummyCourses.first(), 10, 2)
                    )
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}


@Composable
@Preview
fun AccountScreenWithDialogPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                AccountScreen(
                    AccountUIState(
                        "KseniiaFeskova@Gmail.com",
                        null,
                        false,
                        showLogoutDialog = true
                    )
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}