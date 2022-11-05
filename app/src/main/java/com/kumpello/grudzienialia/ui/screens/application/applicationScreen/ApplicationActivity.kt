package com.kumpello.grudzienialia.ui.screens.application.applicationScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kumpello.grudzienialia.R
import com.kumpello.grudzienialia.ui.navigation.BottomBarRoutes
import com.kumpello.grudzienialia.ui.screens.application.addPostScreen.AddPost
import com.kumpello.grudzienialia.ui.screens.application.calendarScreen.Calendar
import com.kumpello.grudzienialia.ui.screens.application.friendsScreen.Friends
import com.kumpello.grudzienialia.ui.screens.login.mainScreen.MainActivityViewModel

class ApplicationActivity : AppCompatActivity() {
    lateinit var viewModel: ApplicationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ApplicationActivityViewModel by viewModels()
        this.viewModel = viewModel
        val activity = this

        setContent{
            ApplicationScreenView()
        }
    }

    @Composable
    fun ApplicationScreenView(){
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigation(navController) },
            content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
                Box(modifier = Modifier.padding(padding)) {
                    ApplicationNavigationGraph(navController = navController)
                }
            },
            backgroundColor = colorResource(androidx.appcompat.R.color.primary_material_dark) // Set background color to avoid the white flashing when you switch between screens
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        ApplicationScreenView()
    }

    @Composable
    fun ApplicationNavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = BottomBarRoutes.Calendar.screen_route) {
            composable(BottomBarRoutes.Calendar.screen_route) {
                Calendar()
            }
            composable(BottomBarRoutes.AddPost.screen_route) {
                AddPost()
            }
            composable(BottomBarRoutes.Friends.screen_route) {
                Friends()
            }
        }
    }

    @Composable
    fun BottomNavigation(navController: NavController) {
        val items = listOf(
            BottomBarRoutes.Calendar,
            BottomBarRoutes.Friends,
            BottomBarRoutes.AddPost,
        )
        androidx.compose.material.BottomNavigation(
            backgroundColor = colorResource(id = androidx.appcompat.R.color.primary_material_dark),
            contentColor = Color.White
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(0.4f),
                    alwaysShowLabel = true,
                    selected = currentRoute == item.screen_route,
                    onClick = {
                        navController.navigate(item.screen_route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BottomNavigationBarPreview() {
        //BottomNavigation()
    }

}