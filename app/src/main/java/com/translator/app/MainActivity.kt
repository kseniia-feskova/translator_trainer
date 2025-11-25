package com.translator.app


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.presentation.ui.AppTheme
import com.presentation.ui.darkColor
import com.presentation.ui.yellowColor
import com.translator.app.di.databaseModule
import com.translator.app.di.networkModule
import com.translator.app.di.preferencesModule
import com.translator.app.di.repositoryModule
import com.translator.app.di.translateModule
import com.translator.app.di.viewModelModule
import com.translator.app.network.hasInternet
import kotlinx.coroutines.launch
import mapper.toDomain
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import presentation.model.FirebaseUser
import presentation.model.LessonType
import presentation.navigation.BottomNavigationBar
import presentation.navigation.LeafScreen
import presentation.navigation.TranslatorAppContainer
import presentation.ui.AppTypography
import presentation.ui.screens.auth.navigateToAuth
import presentation.viewmodel.MainViewModel
import useCaseModule

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    private lateinit var credentialManager: CredentialManager
    private lateinit var launcherClassic: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin()
        credentialManager = CredentialManager.create(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route?.lowercase()
            val isNetworkConnected by viewModel.isNetworkConnected.collectAsState(hasInternet(context = this))

            LaunchedEffect(Unit) {
                viewModel.isUserAuthorized.collect {
                    if (!it) {
                        navController.navigateToAuth(
                            NavOptions.Builder()
                                .setPopUpTo(
                                    navController.graph.startDestinationId,
                                    inclusive = true
                                )
                                .setLaunchSingleTop(true)
                                .build()
                        )
                    }
                }
            }
            val shouldShowBottomBar = when {
                currentRoute?.contains(LeafScreen.NewSet.route) ?: true -> false
                currentRoute.contains(LeafScreen.Login.route) -> false
                currentRoute.contains(LeafScreen.VerifyEmail.route) -> false
                currentRoute.contains(LeafScreen.SelectCourse.route) -> false
                currentRoute.contains(LeafScreen.Splash.route) -> false
                currentRoute.contains(LeafScreen.Splash.route) -> false
                currentRoute.contains(LeafScreen.BubbleLesson("").route) -> false
                currentRoute.contains(LeafScreen.SuccessLesson(LessonType.BUBBLE, 0).route) -> false

                else -> true
            }
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    topBar = {
                        if (!isNetworkConnected) {
                            Text(
                                "No internet connection",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = yellowColor.copy(alpha = 0.5f))
                                    .padding(12.dp),
                                color = darkColor,
                                style = AppTypography.displaySmall
                            )
                        }
                    },
                    content = { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            TranslatorAppContainer(
                                navController,
                                { onSuccess -> onGoogleLoginClick(onSuccess = onSuccess) }
                            )
                        }
                    },
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            BottomNavigationBar(navController = navController)
                        }
                    }

                )
            }
        }
    }

    fun onGoogleLoginClick(onSuccess: (FirebaseUser?) -> Unit) {
        lifecycleScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result =
                    credentialManager.getCredential(
                        request = request,
                        context = this@MainActivity
                    )
                handleCredential(result.credential, onSuccess)

            } catch (e: NoCredentialException) {
                startClassicGoogleSignIn(onSuccess)
            } catch (e: Exception) {
                Log.e("GoogleAuth", "Ошибка авторизации", e)
            }
        }
    }


    private fun handleCredential(
        credential: Credential,
        onSuccess: (FirebaseUser?) -> Unit
    ) {

        try {
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCred = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleCred.idToken
                firebaseAuthWithGoogle(idToken, onSuccess)
            } else {
                Log.e("GoogleAuth", "Not a Google ID Token credential: ${credential.type}")
            }

        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error while parsing Google credential", e)
        }
    }

    private fun startClassicGoogleSignIn(onSuccess: (FirebaseUser?) -> Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        launcherClassic = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.idToken?.let { firebaseAuthWithGoogle(it, onSuccess) }
                } catch (e: ApiException) {
                    Log.e("GoogleAuth", "Ошибка Google Sign-In", e)
                }
            }
        }
        launcherClassic.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String, onSuccess: (FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                onSuccess(authResult.user?.toDomain())
            }
            .addOnFailureListener { e ->
                Log.e("GoogleAuth", "Ошибка Firebase Auth", e)
            }
    }

}


fun MainActivity.startKoin() {
    org.koin.core.context.startKoin {
        androidLogger(Level.ERROR)
        androidContext(applicationContext)
        modules(
            listOf(
                networkModule,
                translateModule,
                repositoryModule,
                databaseModule,
                useCaseModule,
                viewModelModule,
                preferencesModule
            )
        )
    }
}