package com.kumpello.grudzienialia.ui.screens.login.mainScreen

import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
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
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var firebaseAuthentication: FirebaseAuthentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (firebaseAuthentication.isUserLogged()) {
            this.startActivity(Intent(this, ApplicationActivity::class.java))
        }
        setContent {
            GrudzienialiaTheme {
                // A surface container using the 'background' color from the theme
                MainNavigation()
            }
        }
    }

    fun getFirebase(): FirebaseAuthentication {
        return firebaseAuthentication
    }
}

@Composable
fun MainNavigationGraph(){
    val navController = rememberNavController()
    val firebaseAuthentication = MainActivity().getFirebase()

    NavHost(navController = navController, startDestination = MainRoutes.Splash.route) {

        composable(MainRoutes.Splash.route) {
            Splash(navController = navController)
        }


        composable(MainRoutes.Login.route) {
            LoginPage(navController = navController, firebaseAuthentication = firebaseAuthentication)
        }

        composable(MainRoutes.SignUp.route) {
            SignUpPage(navController = navController, firebaseAuthentication = firebaseAuthentication)
        }

        composable(MainRoutes.ForgotPassword.route) {
            ForgotPassword(navController = navController, firebaseAuthentication = firebaseAuthentication)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GrudzienialiaTheme {
        MainNavigation()
    }
}
