package com.kumpello.grudzienialia.ui.navigation

sealed class BottomBarRoutes(var title:String, var icon:Int, var screen_route:String){

    object Calendar : BottomBarRoutes("Home", 0,"calendar")
    object AddPost: BottomBarRoutes("Post", 1,"add_post")
    object Friends: BottomBarRoutes("My Network",2,"friends")
}