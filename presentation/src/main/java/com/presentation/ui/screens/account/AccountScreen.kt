package com.presentation.ui.screens.account

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.R
import com.presentation.navigation.BottomNavigationBar
import com.presentation.ui.AppTheme
import com.presentation.ui.bgColor
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
    deleteAccount: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountTopView(
                name = state.name.toString(),
                photo = state.image,
                onEditClicked = onEditClicked
            )
            Spacer(modifier = Modifier.height(32.dp))
            CustomShadowButton(
                text = stringResource(R.string.add_course_btn), onClick = { addLanguage() },
                modifier = Modifier
                    .padding(horizontal = 24.dp),
            )

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
                        stringResource(R.string.logout_title), style = MaterialTheme.typography.displayLarge.copy(
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