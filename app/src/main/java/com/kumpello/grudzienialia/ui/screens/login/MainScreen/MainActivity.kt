package com.kumpello.grudzienialia.ui.screens.login.MainScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kumpello.grudzienialia.ui.navigation.MainRoutes
import com.kumpello.grudzienialia.ui.screens.login.ForgotPasswordScreen.ForgotPassword
import com.kumpello.grudzienialia.ui.screens.login.LoginScreen.LoginPage
import com.kumpello.grudzienialia.ui.screens.login.SignUpScreen.SignUp
import com.kumpello.grudzienialia.ui.screens.login.splashScreen.Splash
import com.kumpello.grudzienialia.ui.theme.GrudzienialiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GrudzienialiaTheme {
                // A surface container using the 'background' color from the theme
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigationGraph(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = MainRoutes.Splash.route) {

        composable(MainRoutes.Splash.route) {
            Splash(navController = navController)
        }


        composable(MainRoutes.Login.route) {
            LoginPage(navController = navController)
        }

        composable(MainRoutes.SignUp.route) {
            SignUp(navController = navController)
        }

        composable(MainRoutes.ForgotPassword.route) { navBackStack ->
            ForgotPassword(navController = navController)
        }
    }
}

@Composable
fun MainNavigation() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainNavigationGraph()
        }
    }
}