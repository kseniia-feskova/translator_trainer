package presentation.ui.screens.account

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.translatortrainer.shared.R
import com.presentation.ui.AppTheme
import com.presentation.ui.bgColor
import com.presentation.ui.darkColor
import com.presentation.ui.fieldBorderColor
import com.presentation.ui.lightLilaColor
import com.presentation.ui.redDarkColor
import com.presentation.ui.whiteColor
import com.presentation.utils.GUEST_MAX_WORDS
import presentation.navigation.BottomNavigationBar
import presentation.test.dummyCourses
import presentation.ui.AppTypography
import presentation.ui.screens.auth.AuthScreenState
import presentation.ui.screens.auth.AuthUIState
import presentation.ui.screens.auth.RegisterForm
import presentation.ui.views.AccountTopView
import presentation.ui.views.buttons.CustomShadowButton

@Composable
fun AccountScreen(
    name: String = "",
    image: Uri? = null,
    guestData: GuestData? = null,
    authState: Boolean = false,
    authUIState: AuthUIState? = null,
    btnsState: AccountBtnsState = AccountBtnsState(),
    onEditClicked: () -> Unit = {},
    addLanguage: () -> Unit = {},
    changeTheme: () -> Unit = {},
    logout: () -> Unit = {},
    dismissDialog: () -> Unit = {},
    deleteAccount: () -> Unit = {},
    createAccount: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onAuthClicked: () -> Unit = {},
    onAuthClose: () -> Unit = {},
    onGoogleSignClick: () -> Unit = {},
) {
    val isGuest by rememberSaveable(guestData) {
        mutableStateOf(guestData != null)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isGuest) {
                AccountTopView(
                    name = "Guest",
                    photo = image,
                    onEditClicked = null
                )
            }

            if (!isGuest) {
                AccountTopView(
                    name = name,
                    photo = image,
                    onEditClicked = onEditClicked
                )
            }

            if (isGuest) {
                Spacer(modifier = Modifier.height(16.dp))
                GuestDataView(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    guestData
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (isGuest) {
                CustomShadowButton(
                    text = stringResource(R.string.create_account_button),
                    onClick = createAccount,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }
            if (!isGuest) {
                CustomShadowButton(
                    text = stringResource(R.string.add_course_btn),
                    onClick = addLanguage,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomShadowButton(
                text = stringResource(R.string.change_theme_btn),
                onClick = changeTheme,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.clickable(onClick = logout),
                text = stringResource(R.string.logout_title),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = darkColor,
                    fontSize = TextUnit(16f, TextUnitType.Sp),
                    fontWeight = FontWeight.SemiBold
                )
            )

            if (!isGuest) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.clickable(onClick = deleteAccount),
                    text = stringResource(R.string.delete_account_btn),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = redDarkColor,
                        fontSize = TextUnit(16f, TextUnitType.Sp),
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }

        if (btnsState.showDeleteDialog) {
            DeleteDialog(
                modifier = Modifier.align(Alignment.Center),
                dismissDialog = dismissDialog,
                deleteAccount = deleteAccount
            )
        }

        if (btnsState.showLogoutDialog) {
            LogoutDialog(
                modifier = Modifier,
                dismissDialog = dismissDialog,
                logout = logout,
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.BottomCenter),
            visible = btnsState.showAuthScreen && authState,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(1000)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(800)
            )
        ) {
            RegisterSection(
                authUIState?: AuthUIState(screenState = AuthScreenState.REGISTER),
                onEmailChanged,
                onPasswordChanged,
                onAuthClicked,
                onAuthClose,
                onGoogleSignClick = onGoogleSignClick
            )
        }
    }
}

@Composable
fun DeleteDialog(
    modifier: Modifier,
    dismissDialog: () -> Unit,
    deleteAccount: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
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
                imageVector = Icons.Default.Delete,
                tint = redDarkColor,
                contentDescription = "Delete"
            )
        }
    )
}

@Composable
fun LogoutDialog(modifier: Modifier, dismissDialog: () -> Unit, logout: () -> Unit) {
    AlertDialog(
        modifier = modifier,
        containerColor = whiteColor,
        onDismissRequest = dismissDialog,
        title = {
            Text(
                stringResource(R.string.logout_title),
                style = AppTypography.displayMedium.copy(color = darkColor)
            )
        },
        text = {
            Text(
                stringResource(R.string.logout_subtitle),
                style = AppTypography.titleLarge.copy(
                    color = darkColor,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
            )
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
                    color = darkColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            )
        }
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun RegisterSection(
    state: AuthUIState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onAuthClicked: () -> Unit,
    onAuthClose: () -> Unit,
    onGoogleSignClick: () -> Unit
) {
    val screenHeight = (LocalConfiguration.current.screenHeightDp * 0.8).dp

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(screenHeight)
                .border(
                    width = 1.dp,
                    color = bgColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            RegisterForm(
                state = state,
                onEmailChanged,
                onPasswordChanged,
                onAuthClicked,
                guestMode = true,
                onCreateAccountClicked = { onAuthClose() },
                onGoogleSignClick = onGoogleSignClick ,
            )
        }
    }
}


@Composable
fun GuestDataView(modifier: Modifier = Modifier, guestData: GuestData?) {
    if (guestData == null) {
        Log.e("GuestDataView", "data is null")
        return
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Text("Limits", style = MaterialTheme.typography.titleMedium.copy(color = darkColor))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Current course:", style = MaterialTheme.typography.titleSmall.copy(color = darkColor)
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
                style = MaterialTheme.typography.titleSmall.copy(color = darkColor)
            )
            Text(
                "${guestData.allWordsCount}/$GUEST_MAX_WORDS",
                style = MaterialTheme.typography.titleSmall.copy(color = darkColor)
            )
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Count of words sets:",
                style = MaterialTheme.typography.titleSmall.copy(color = darkColor)
            )
            Text(
                "${guestData.allSetsCount}/3",
                style = MaterialTheme.typography.titleSmall.copy(color = darkColor)
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
                AccountScreen("KseniiaFeskova@Gmail.com", null)
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
                    "KseniiaFeskova@Gmail.com",
                    null,
                    guestData = GuestData(dummyCourses.first(), 10, 2)
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
fun AccountScreenGuestAuthPreview() {
    AppTheme {
        Scaffold(content = { paddings ->
            Log.e("Preview", "paddings $paddings")
            Box(modifier = Modifier.padding(paddings)) {
                AccountScreen(
                    "KseniiaFeskova@Gmail.com",
                    null,
                    guestData = GuestData(dummyCourses.first(), 10, 2),
                    btnsState = AccountBtnsState(showAuthScreen = true),
                    authState = true
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
                    "KseniiaFeskova@Gmail.com",
                    null,
                    btnsState = AccountBtnsState(showLogoutDialog = true),
                )
            }
        }, bottomBar = {
            val context = LocalContext.current
            BottomNavigationBar(navController = NavController(context))
        }
        )
    }
}