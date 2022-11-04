package com.kumpello.grudzienialia.ui.screens.login.mainScreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kumpello.grudzienialia.domain.usecase.FirebaseAuthentication
import com.kumpello.grudzienialia.ui.navigation.MainRoutes
import com.kumpello.grudzienialia.ui.screens.application.applicationScreen.ApplicationActivity
import com.kumpello.grudzienialia.ui.screens.login.forgotPasswordScreen.ForgotPassword
import com.kumpello.grudzienialia.ui.screens.login.loginScreen.LoginPage
import com.kumpello.grudzienialia.ui.screens.login.signUpScreen.SignUpPage
import com.kumpello.grudzienialia.ui.screens.login.splashScreen.Splash
import com.kumpello.grudzienialia.ui.theme.GrudzienialiaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainActivityViewModel by viewModels()
        this.viewModel = viewModel
        val activity = this
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.firebaseAuthentication.setActivity(activity)
            }
        }
        if (viewModel.firebaseAuthentication.isUserLogged()) {
            this.startActivity(Intent(this, ApplicationActivity::class.java))
        }
        setContent {
            GrudzienialiaTheme {
                MainNavigation(activity)
            }
        }
    }

    fun getFirebase(): FirebaseAuthentication {
        return viewModel.firebaseAuthentication
    }
}

@Composable
fun MainNavigationGraph(activity: MainActivity) {
    val navController = rememberNavController()
    val firebaseAuthentication = activity.getFirebase()

    NavHost(navController = navController, startDestination = MainRoutes.Splash.route) {

        composable(MainRoutes.Splash.route) {
            Splash(navController)
        }

        composable(MainRoutes.Login.route) {
            LoginPage(navController, firebaseAuthentication)
        }

        composable(MainRoutes.SignUp.route) {
            SignUpPage(navController, firebaseAuthentication)
        }

        composable(MainRoutes.ForgotPassword.route) {
            ForgotPassword(navController, firebaseAuthentication)
        }
    }
}

@Composable
fun MainNavigation(activity: MainActivity) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainNavigationGraph(activity)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GrudzienialiaTheme {
        MainNavigation(MainActivity())
    }
}
