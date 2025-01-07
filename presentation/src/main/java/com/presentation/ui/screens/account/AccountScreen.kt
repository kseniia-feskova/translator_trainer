package com.presentation.ui.screens.account

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.presentation.navigation.BottomNavigationBar
import com.presentation.ui.AppTheme
import com.presentation.ui.redDarkColor
import com.presentation.ui.views.AccountTopView
import com.presentation.ui.views.ActionButton

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
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AccountTopView(
                name = state.name.toString(),
                photo = state.image,
                onEditClicked = onEditClicked
            )
            ActionButton(
                enabled = false,
                onClick = { addLanguage() },
                modifier = Modifier
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "Добавить язык",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }
            ActionButton(
                enabled = false,
                onClick = { changeTheme() },
                modifier = Modifier
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = "Изменить тему",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                )
            }

            Text(
                modifier = Modifier.clickable { logout() },
                text = "Выйти",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
            )


            Text(
                modifier = Modifier.clickable { deleteAccount() },
                text = "Удалить аккаунт",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = redDarkColor
                ),
            )
        }

        if (state.showDeleteDialog) {
            AlertDialog(modifier = Modifier.align(Alignment.Center), onDismissRequest = {
                dismissDialog()
            }, confirmButton = {
                Text(
                    text = "Согласен",
                    Modifier.clickable { deleteAccount() },
                    color = redDarkColor
                )
            }, dismissButton = {
                Text(text = "Отмена", Modifier.clickable { dismissDialog() })
            },
                title = {
                    Text(
                        "Удаление профиля", style = MaterialTheme.typography.titleMedium.copy(
                            color = redDarkColor
                        )
                    )
                },
                text = {
                    Text(
                        "Вы уверены, что хотите удалить свой аккаунт и все данные? Это действие нельзя будет отменить",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary
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
            AlertDialog(modifier = Modifier.align(Alignment.Center), onDismissRequest = {
                dismissDialog()
            }, confirmButton = {
                Text(
                    text = "Согласен",
                    Modifier.clickable { logout() },
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }, dismissButton = {
                Text(text = "Отмена", Modifier.clickable { dismissDialog() })
            },
                title = {
                    Text(
                        "Выйти из профиля", style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                text = {
                    Text(
                        "Вы уверены, что хотите выйти из своего профиля?",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        tint = MaterialTheme.colorScheme.onPrimary,
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