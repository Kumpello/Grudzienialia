package com.kumpello.grudzienialia.data.remote

class User constructor(val email: String, val userId: String){
    lateinit var nick: String
    lateinit var friends: List<User>
}