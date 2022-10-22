package com.kumpello.grudzienialia.ui.navigation

sealed class MainRoutes(val route: String) {
    object Login : MainRoutes("Login")
    object ForgotPassword : MainRoutes("Forgot Password")
    object SignUp : MainRoutes("SignUp")
    object Splash : MainRoutes("splash")
}